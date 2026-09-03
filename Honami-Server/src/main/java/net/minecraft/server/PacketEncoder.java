package net.minecraft.server;

import java.io.IOException;

import rein.honami.spigot.exception.ExploitException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

	
	
	private final EnumProtocolDirection c;

	public PacketEncoder(EnumProtocolDirection enumprotocoldirection) {
		this.c = enumprotocoldirection;
	}

	protected void a(ChannelHandlerContext ctx, Packet<?> packet, ByteBuf bytebuf) throws Exception {
		Integer packetId = ((EnumProtocol) ctx.channel().attr(NetworkManager.c).get()).a(this.c, packet);

		if (packetId == null) {
			throw new IOException("Can't serialize unregistered packet");
		} else {
			PacketDataSerializer serializer = new PacketDataSerializer(bytebuf);
			serializer.b(packetId);

			try {
				packet.b(serializer);
			} catch (ExploitException ex) {
				System.out.println("Exploit exception: " + ctx.channel().attr(NetworkManager.ATTRIBUTE_PROTOCOL).get());
			}

		}
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf)
			throws Exception {
		this.a(channelHandlerContext, packet, byteBuf);
	}
}
