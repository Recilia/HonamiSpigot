package rein.honami.spigot.protocol;

import rein.honami.spigot.config.HonamiConfig;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.HandshakeListener;
import net.minecraft.server.LegacyPingHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PacketDecoder;
import net.minecraft.server.PacketEncoder;
import net.minecraft.server.PacketPrepender;
import net.minecraft.server.PacketSplitter;
import net.minecraft.server.ServerConnection;

public class MinecraftPipeline extends ChannelInitializer<SocketChannel> {
	private final ServerConnection serverConnection;

	public MinecraftPipeline(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	@Override
	protected void initChannel(SocketChannel channel) {
		try {
			ChannelConfig config = channel.config();
			config.setOption(ChannelOption.TCP_NODELAY, HonamiConfig.enableTCPNODELAY);
			config.setOption(ChannelOption.TCP_FASTOPEN, HonamiConfig.modeTcpFastOpen);
			config.setOption(ChannelOption.TCP_FASTOPEN_CONNECT,HonamiConfig.enableTcpFastOpen);
			config.setOption(ChannelOption.IP_TOS, 0x18); 
			config.setAllocator(ByteBufAllocator.DEFAULT);
		} catch (Exception ignored) {
		}

		channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30))
				.addLast("legacy_query", new LegacyPingHandler(serverConnection))
				.addLast("splitter", new PacketSplitter())
				.addLast("decoder", new PacketDecoder(EnumProtocolDirection.SERVERBOUND))
				.addLast("prepender", PacketPrepender.INSTANCE)
				.addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND));
		NetworkManager networkmanager = new NetworkManager(EnumProtocolDirection.SERVERBOUND);
		this.serverConnection.pending.add(networkmanager); 
		channel.pipeline().addLast("packet_handler", networkmanager);
		networkmanager.a(new HandshakeListener(this.serverConnection.server, networkmanager));
        io.papermc.paper.network.ChannelInitializeListenerHolder.callListeners(channel); 
	}
}
