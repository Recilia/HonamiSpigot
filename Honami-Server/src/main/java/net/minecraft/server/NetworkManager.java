package net.minecraft.server;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Queue;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.velocitypowered.natives.compression.VelocityCompressor; 
import com.velocitypowered.natives.util.Natives; 
import rein.honami.spigot.Honami;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.exception.ExploitException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import rein.honami.spigot.util.CryptException;

public class NetworkManager extends SimpleChannelInboundHandler<Packet> {

	private static final Logger LOGGER = LogManager.getLogger();
	public static final Marker ROOT_MARKER = MarkerManager.getMarker("NETWORK");
	public static final Marker PACKET_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.ROOT_MARKER);
	public static final AttributeKey<EnumProtocol> ATTRIBUTE_PROTOCOL = AttributeKey.valueOf("protocol");
	public static final AttributeKey<EnumProtocol> c = ATTRIBUTE_PROTOCOL;
	
	public static final LazyInitVar<NioEventLoopGroup> NETWORK_WORKER_GROUP = new LazyInitVar<>(
			() -> new NioEventLoopGroup(0,
					(new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build()));
	public static final LazyInitVar<EpollEventLoopGroup> NETWORK_EPOLL_WORKER_GROUP = new LazyInitVar<>(
			() -> new EpollEventLoopGroup(0,
					(new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build()));
	public static final LazyInitVar<DefaultEventLoopGroup> LOCAL_WORKER_GROUP = new LazyInitVar<>(
			() -> new DefaultEventLoopGroup(0,
					(new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build()));

	private final EnumProtocolDirection h;
	private final Queue<NetworkManager.QueuedPacket> i = Queues.newConcurrentLinkedQueue();
	public Channel channel;
	
	public SocketAddress l;
	public java.util.UUID spoofedUUID;
	public com.mojang.authlib.properties.Property[] spoofedProfile;
	public boolean preparing = true;
	
	private PacketListener m;
	private IChatBaseComponent n;
	private boolean o;

	private boolean shouldCheckPacket = false;

	public boolean isEncrypted() {
		return this.o;
	} 

	private boolean p;

	public boolean isDisconnectionHandled() {
		return this.p;
	} 

	public void setDisconnectionHandled(boolean handled) {
		this.p = handled;
	} 

	volatile boolean canFlush = true;
	private final java.util.concurrent.atomic.AtomicInteger packetWrites = new java.util.concurrent.atomic.AtomicInteger();
	private int flushPacketsStart;
	private final Object flushLock = new Object();

	public void disableAutomaticFlush() {
		synchronized (this.flushLock) {
			this.flushPacketsStart = this.packetWrites.get(); 
			this.canFlush = false;
		}
	}

	public void enableAutomaticFlush() {
		synchronized (this.flushLock) {
			this.canFlush = true;
			if (this.packetWrites.get() != this.flushPacketsStart) { 
				this.flush(); 
			}
		}
	}

	private void flush() {
		if (this.channel.eventLoop().inEventLoop()) {
			this.channel.flush();
		} 
			
	}

	public NetworkManager(EnumProtocolDirection enumprotocoldirection) {
		this.h = enumprotocoldirection;
	}

	@Override
	public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
		super.channelActive(channelhandlercontext);
		this.channel = channelhandlercontext.channel();
		this.l = this.channel.remoteAddress();
		
		this.preparing = false;

		try {
			this.setProtocol(EnumProtocol.HANDSHAKING);
		} catch (Throwable throwable) {
			NetworkManager.LOGGER.fatal(throwable);
		}

	}

	public void setProtocol(EnumProtocol protocol) {
		a(protocol);
	}

	public void a(EnumProtocol protocol) {
		this.channel.attr(NetworkManager.ATTRIBUTE_PROTOCOL).set(protocol);
		this.channel.config().setAutoRead(true);
	}

	@Override
	public void channelInactive(ChannelHandlerContext channelhandlercontext) throws Exception {
		this.close(new ChatMessage("disconnect.endOfStream"));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) throws Exception {
		ChatMessage chatmessage;

		if (throwable instanceof DecoderException) {
			DecoderException decoderException = ((DecoderException) throwable);
			if (decoderException.getCause() instanceof ExploitException) {
				Bukkit.getLogger().warning("Server crash detected...");
				if (this.getPacketListener() != null && this.getPacketListener() instanceof PlayerConnection) {
					PlayerConnection playerConnection = (PlayerConnection) this.getPacketListener();
					CraftPlayer player = playerConnection.getPlayer();
					if (player != null) {
						Bukkit.getLogger().warning(
								player.getName() + " has tried to crash the server... " + decoderException.getCause());
					}
				}
			}
		}

		if (throwable instanceof TimeoutException) {
			chatmessage = new ChatMessage("disconnect.timeout");
		} else {
			chatmessage = new ChatMessage("disconnect.genericReason", "Internal Exception: " + throwable);
		}

		this.close(chatmessage);

	}

	protected void a(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if (this.channel.isOpen()) {
			if (this.m instanceof PlayerConnection) {
				try {
					for (rein.honami.spigot.protocol.PacketListener packetListener : Honami.getInstance()
							.getPacketListeners()) {
						if (!packetListener.onReceivedPacket((PlayerConnection) this.m, packet)) {
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				packet.a(this.m);
			} catch (CancelledPacketHandleException cancelledpackethandleexception) {
				;
			}
		}
	}

	public void a(PacketListener packetlistener) {
		Validate.notNull(packetlistener, "packetListener");

		this.m = packetlistener;
	}

	public void handle(Packet packet) {
		if (this.isConnected()) {
			this.sendPacketQueue();

			if (!shouldCheckPacket) {

				if (this.packetWrites.get() > 5) {
					shouldCheckPacket = true;
				}
			} else {   
				
		        if (HonamiConfig.asyncKnockback && (packet instanceof PacketPlayOutEntityVelocity || packet instanceof PacketPlayOutPosition || packet instanceof PacketPlayInFlying.PacketPlayInPosition || packet instanceof PacketPlayInFlying)) {
		        	
		        	Honami.getInstance().getKnockbackThread().addPacket(packet, this, null);
		            return;
		        }
			}
	        
			this.dispatchPacket(packet, null, Boolean.TRUE);
		} else {
			
			this.i.add(new NetworkManager.QueuedPacket(packet));
		}

	}

	public void a(Packet packet, GenericFutureListener<? extends Future<? super Void>> listener,
			GenericFutureListener<? extends Future<? super Void>>... listeners) {
		if (this.isConnected()) {
			this.sendPacketQueue();
			this.dispatchPacket(packet, ArrayUtils.insert(0, listeners, listener), Boolean.TRUE);
		} else {
			
			this.i.add(new NetworkManager.QueuedPacket(packet, ArrayUtils.insert(0, listeners, listener)));
		}

	}

	public EntityPlayer getPlayer() {
		if (getPacketListener() instanceof PlayerConnection) {
			return ((PlayerConnection) getPacketListener()).player;
		} else {
			return null;
		}
	}

	public void dispatchPacket(final Packet<?> packet,
			final GenericFutureListener<? extends Future<? super Void>>[] listeners, Boolean flushConditional) {
		this.packetWrites.getAndIncrement(); 
		boolean effectiveFlush = flushConditional == null ? this.canFlush : flushConditional;
		final boolean flush = effectiveFlush || packet instanceof PacketPlayOutKeepAlive
				|| packet instanceof PacketPlayOutKickDisconnect; 
		final EnumProtocol enumprotocol = EnumProtocol.getProtocolForPacket(packet);
		final EnumProtocol enumprotocol1 = this.channel.attr(NetworkManager.ATTRIBUTE_PROTOCOL).get();
		if (enumprotocol1 != enumprotocol) {
			this.channel.config().setAutoRead(false);
		}
		if (this.channel.eventLoop().inEventLoop()) {
			if (enumprotocol != enumprotocol1) {
				this.setProtocol(enumprotocol);
			}
			ChannelFuture channelfuture = flush ? this.channel.writeAndFlush(packet) : this.channel.write(packet);
			if (listeners != null) {
				channelfuture.addListeners(listeners);
			}
			channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		} else {
			
			Runnable choice1 = null;
			AbstractEventExecutor.LazyRunnable choice2 = null;

			
			
			if (flush) {
				choice1 = () -> {
					if (enumprotocol != enumprotocol1) {
						this.setProtocol(enumprotocol);
					}
					try {
						ChannelFuture channelfuture1 = this.channel.writeAndFlush(packet); 
						if (listeners != null) {
							channelfuture1.addListeners(listeners);
						}
						channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
					} catch (Exception e) {
						LOGGER.error("NetworkException: " + getPlayer(), e);
						close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));
						;
					}
				};
			} else {
				
				choice2 = () -> {
					if (enumprotocol != enumprotocol1) {
						this.setProtocol(enumprotocol);
					}
					try {

						
						ChannelFuture channelfuture1 = this.channel
								.write(packet); 
						if (listeners != null) {
							channelfuture1.addListeners(listeners);
						}
						channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
					} catch (Exception e) {
						LOGGER.error("NetworkException: " + getPlayer(), e);
						close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));
						;
					}
				};
			}
			this.channel.eventLoop().execute(choice1 != null ? choice1 : choice2);
			
		}
	}

	private void a(final Packet packet,
			final GenericFutureListener<? extends Future<? super Void>>[] agenericfuturelistener) {
		this.dispatchPacket(packet, agenericfuturelistener, Boolean.TRUE);
	}

	private void sendPacketQueue() {
		if (this.i.isEmpty()) {
			return; 
		}
		if (this.channel != null && this.channel.isActive()) {
			
			boolean needsFlush = this.canFlush;
			boolean hasWrotePacket = false;
			Iterator<QueuedPacket> iterator = this.i.iterator();
			while (iterator.hasNext()) {
				QueuedPacket queued = iterator.next();
				Packet packet = queued.a;
				if (hasWrotePacket && (needsFlush || this.canFlush)) {
					flush();
				}
				iterator.remove();
				this.dispatchPacket(packet, queued.b,
						(!iterator.hasNext() && (needsFlush || this.canFlush)) ? Boolean.TRUE : Boolean.FALSE);
				hasWrotePacket = true;
			}
		}
	}

	private void m() {
		this.sendPacketQueue();
	}

	public void tick() {
		this.sendPacketQueue();
		if (this.m instanceof IUpdatePlayerListBox) {
			((IUpdatePlayerListBox) this.m).c();
		}

		this.channel.flush();
	}

	public void a() {
		this.tick();
	}

	public SocketAddress getSocketAddress() {
		return this.l;
	}

	public void close(IChatBaseComponent ichatbasecomponent) {
		this.i.clear(); 
		
		this.preparing = false;
		
		if (this.channel.isOpen()) {
			this.channel.close(); 
			this.n = ichatbasecomponent;
		}
	}

	public boolean c() {
		return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
	}

	

	public void setupEncryption(javax.crypto.SecretKey key) throws CryptException {
		if (!this.o) {
			try {
				com.velocitypowered.natives.encryption.VelocityCipher decryption = com.velocitypowered.natives.util.Natives.cipher
						.get().forDecryption(key);
				com.velocitypowered.natives.encryption.VelocityCipher encryption = com.velocitypowered.natives.util.Natives.cipher
						.get().forEncryption(key);

				this.o = true;
				this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(decryption));
				this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(encryption));
			} catch (java.security.GeneralSecurityException e) {
				throw new CryptException(e);
			}
		}
	}

	public boolean isConnected() {
		return this.channel != null && this.channel.isOpen();
	}

	public boolean g() {
		return this.isConnected();
	}

	public boolean h() {
		return this.channel == null;
	}

	public PacketListener getPacketListener() {
		return this.m;
	}

	public IChatBaseComponent j() {
		return this.n;
	}

	public void k() {
		this.channel.config().setAutoRead(false);
	}

	public void a(int i) {
		
		this.setupCompression(i);
	}

	public void setupCompression(int compressionThreshold) {
		
		if (compressionThreshold >= 0) {
			VelocityCompressor compressor = Natives.compress.get().create(-1); 
			if (this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
				((PacketDecompressor) this.channel.pipeline().get("decompress")).a(compressionThreshold);
			} else {
				this.channel.pipeline().addBefore("decoder", "decompress",
						new PacketDecompressor(compressor, compressionThreshold)); 
			}

			if (this.channel.pipeline().get("compress") instanceof PacketCompressor) {
				((PacketCompressor) this.channel.pipeline().get("decompress")).a(compressionThreshold);
			} else {
				this.channel.pipeline().addBefore("encoder", "compress",
						new PacketCompressor(compressor, compressionThreshold)); 
			}
		} else {
			if (this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
				this.channel.pipeline().remove("decompress");
			}

			if (this.channel.pipeline().get("compress") instanceof PacketCompressor) {
				this.channel.pipeline().remove("compress");
			}
		}
	}

	public void handleDisconnection() {
		if (this.channel != null && !this.channel.isOpen()) {
			if (!this.isDisconnectionHandled()) {
				this.setDisconnectionHandled(true);
				if (this.j() != null) {
					this.getPacketListener().a(this.j());
				} else if (this.getPacketListener() != null) {
					this.getPacketListener().a(new ChatComponentText("Disconnected"));
				}
				this.i.clear(); 
			} else {
				NetworkManager.LOGGER.warn("handleDisconnection() called twice");
			}

		}
	}

	public void l() {
		this.handleDisconnection();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet object) throws Exception { 

																												

																												

																												

																												

																												
		if (g()) {
			this.a(channelhandlercontext, object);
		}
	}

	static class QueuedPacket {
		private final Packet a; 
		private final GenericFutureListener<? extends Future<? super Void>>[] b; 

		@SafeVarargs
		public QueuedPacket(Packet packet, GenericFutureListener<? extends Future<? super Void>>... listeners) {
			this.a = packet;
			this.b = listeners;
		}
	}

	public SocketAddress getRawAddress() {
		return this.channel.remoteAddress();
	}
	
}
