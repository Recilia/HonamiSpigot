package net.minecraft.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class PacketPrepender extends MessageToMessageEncoder<ByteBuf> {
	public static final PacketPrepender INSTANCE = new PacketPrepender();

	public PacketPrepender() {
	}

	public static void writeVarInt(ByteBuf buf, int value) {
		while (true) {
			if ((value & 0xFFFFFF80) == 0) {
				buf.writeByte(value);
				return;
			}

			buf.writeByte(value & 0x7F | 0x80);
			value >>>= 7;
		}
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		ByteBuf lengthBuf = ctx.alloc().buffer(5);
		writeVarInt(lengthBuf, in.readableBytes());
		out.add(lengthBuf);
		out.add(in.retain());
	}

}