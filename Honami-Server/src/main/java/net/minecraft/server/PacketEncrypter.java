package net.minecraft.server;

import java.util.List; 

import com.velocitypowered.natives.encryption.VelocityCipher; 
import com.velocitypowered.natives.util.MoreByteBufUtils; 

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder; 

public class PacketEncrypter extends MessageToMessageEncoder<ByteBuf> { 
	private final VelocityCipher cipher; 

	public PacketEncrypter(VelocityCipher ciper) { 
		this.cipher = ciper; 
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
			throws Exception { 
								
		ByteBuf compatible = MoreByteBufUtils.ensureCompatible(channelHandlerContext.alloc(), cipher, byteBuf);
		try {
			cipher.process(compatible);
			list.add(compatible);
		} catch (Exception e) {
			compatible.release(); 
			throw e;
		}
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		cipher.close();
	}
	
}