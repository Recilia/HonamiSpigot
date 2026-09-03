package net.minecraft.server;

import java.util.zip.Deflater;

import com.velocitypowered.natives.compression.VelocityCompressor; 
import com.velocitypowered.natives.util.MoreByteBufUtils; 

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {
	private final byte[] encodeBuf; 
	private final Deflater deflater;
	private final com.velocitypowered.natives.compression.VelocityCompressor compressor; 
	private int threshold;

	public PacketCompressor(int compressionThreshold) {
		
		this(null, compressionThreshold);
	}

	public PacketCompressor(VelocityCompressor compressor, int compressionThreshold) {
		this.threshold = compressionThreshold;
		if (compressor == null) {
			this.encodeBuf = new byte[8192];
			this.deflater = new Deflater();
		} else {
			this.encodeBuf = null;
			this.deflater = null;
		}
		this.compressor = compressor;
		
	}

	@Override
	protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
		int var4 = var2.readableBytes();
		PacketDataSerializer var5 = new PacketDataSerializer(var3);
		if (var4 < this.threshold) {
			var5.b(0);
			var5.writeBytes(var2);
		} else {
			
			if (this.deflater != null) {
				byte[] var6 = new byte[var4];
				var2.readBytes(var6);
				var5.b(var6.length);
				this.deflater.setInput(var6, 0, var4);
				this.deflater.finish();

				while (!this.deflater.finished()) {
					int var7 = this.deflater.deflate(this.encodeBuf);
					var5.writeBytes(this.encodeBuf, 0, var7);
				}

				this.deflater.reset();
				return;
			}

			var5.writeVarInt(var4);
			ByteBuf compatibleIn = MoreByteBufUtils.ensureCompatible(var1.alloc(), this.compressor, var2);
			try {
				this.compressor.deflate(compatibleIn, var3);
			} finally {
				compatibleIn.release();
			}
			
		}
	}

	@Override
	protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) throws Exception {
		if (this.compressor != null) {

			

			

			

			int initialBufferSize = msg.readableBytes() + 1;
			return com.velocitypowered.natives.util.MoreByteBufUtils.preferredBuffer(ctx.alloc(), this.compressor,
					initialBufferSize);
		}

		return super.allocateBuffer(ctx, msg, preferDirect);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if (this.compressor != null) {
			this.compressor.close();
		}
	}

	public void a(int var1) {
		
		this.setThreshold(var1);
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
}
