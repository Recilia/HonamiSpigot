package net.minecraft.server;

import java.net.InetAddress;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

public class HandshakeListener implements PacketHandshakingInListener {

	private static final com.google.gson.Gson gson = new com.google.gson.Gson(); 
	
	private static final Map<InetAddress, Long> throttleTracker = new Object2LongOpenHashMap<>();
	private static int throttleCounter = 0;

	private final MinecraftServer a;
	private final NetworkManager b;

	public HandshakeListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
		this.a = minecraftserver;
		this.b = networkmanager;
	}

	@Override
	public void a(PacketHandshakingInSetProtocol packethandshakinginsetprotocol) {
		switch (packethandshakinginsetprotocol.a()) {
		case LOGIN: {
			this.b.a(EnumProtocol.LOGIN);
			ChatComponentText text;

			try {
				long currentTime = System.currentTimeMillis();
				long connectionThrottle = MinecraftServer.getServer().server.getConnectionThrottle();
				InetAddress address = ((java.net.InetSocketAddress) this.b.getSocketAddress()).getAddress();

				synchronized (throttleTracker) {
					if (throttleTracker.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress())
							&& currentTime - throttleTracker.get(address) < connectionThrottle) {
						throttleTracker.put(address, currentTime);
						text = new ChatComponentText("Connection throttled! Please wait before reconnecting.");
						this.b.handle(new PacketLoginOutDisconnect(text));
						this.b.close(text);
						return;
					}

					throttleTracker.put(address, currentTime);
					throttleCounter++;
					if (throttleCounter > 200) {
						throttleCounter = 0;

						throttleTracker.entrySet().removeIf(entry -> entry.getValue() > connectionThrottle);
					}
				}
			} catch (Throwable t) {
				org.apache.logging.log4j.LogManager.getLogger().debug("Failed to check connection throttle", t);
			}

			if (packethandshakinginsetprotocol.b() > 47) {
				text = new ChatComponentText(
						java.text.MessageFormat.format(org.spigotmc.SpigotConfig.outdatedServerMessage, "1.8.8")); 
				this.b.handle(new PacketLoginOutDisconnect(text));
				this.b.close(text);
			} else if (packethandshakinginsetprotocol.b() < 47) {
				text = new ChatComponentText(
						java.text.MessageFormat.format(org.spigotmc.SpigotConfig.outdatedClientMessage, "1.8.8")); 
				this.b.handle(new PacketLoginOutDisconnect(text));
				this.b.close(text);
			} else {
				this.b.a(new LoginListener(this.a, this.b));
				
				boolean proxyLogicEnabled = org.spigotmc.SpigotConfig.bungee;
				boolean handledByEvent = false;
				
				if (com.destroystokyo.paper.event.player.PlayerHandshakeEvent.getHandlerList()
						.getRegisteredListeners().length != 0) { 
					com.destroystokyo.paper.event.player.PlayerHandshakeEvent event = new com.destroystokyo.paper.event.player.PlayerHandshakeEvent(
							packethandshakinginsetprotocol.hostname, !proxyLogicEnabled);
					org.bukkit.Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						
						if (event.isFailed()) {
							text = new ChatComponentText(event.getFailMessage());
							this.b.handle(new PacketLoginOutDisconnect(text));
							this.b.close(text);
							return;
						}

						packethandshakinginsetprotocol.hostname = event.getServerHostname();
						this.b.l = new java.net.InetSocketAddress(event.getSocketAddressHostname(),
								((java.net.InetSocketAddress) this.b.getSocketAddress()).getPort());
						this.b.spoofedUUID = event.getUniqueId();
						this.b.spoofedProfile = gson.fromJson(event.getPropertiesJson(),
								com.mojang.authlib.properties.Property[].class);
						handledByEvent = true; 
					}
				}
				
				if (!handledByEvent && proxyLogicEnabled) {

					
					
					String[] split = packethandshakinginsetprotocol.hostname.split("\00");
					if (split.length == 3 || split.length == 4) {
						packethandshakinginsetprotocol.hostname = split[0];
						b.l = new java.net.InetSocketAddress(split[1],
								((java.net.InetSocketAddress) b.getSocketAddress()).getPort());
						b.spoofedUUID = com.mojang.util.UUIDTypeAdapter.fromString(split[2]);
					} else {
						text = new ChatComponentText(
								"If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
						this.b.handle(new PacketLoginOutDisconnect(text));
						this.b.close(text);
						return;
					}
					if (split.length == 4) {
						b.spoofedProfile = gson.fromJson(split[3], com.mojang.authlib.properties.Property[].class);
					}
				}
				
				((LoginListener) this.b.getPacketListener()).hostname = packethandshakinginsetprotocol.hostname + ":"
						+ packethandshakinginsetprotocol.port; 
			}
			break;
		}
		case STATUS: {
			this.b.a(EnumProtocol.STATUS);
			this.b.a(new PacketStatusListener(this.a, this.b));
			break;
		}
		default:
			throw new UnsupportedOperationException("Invalid intention " + packethandshakinginsetprotocol.a());
		}
	}

	@Override
	public void a(IChatBaseComponent iChatBaseComponent) {
		
	}
}
