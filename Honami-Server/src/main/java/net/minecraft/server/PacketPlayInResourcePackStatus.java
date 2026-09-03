package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInResourcePackStatus implements Packet<PacketListenerPlayIn> {

	public String a; 
	public PacketPlayInResourcePackStatus.EnumResourcePackStatus b; 

	public PacketPlayInResourcePackStatus() {
	}

	@Override
	public void a(PacketDataSerializer serializer) throws IOException {
		this.a = serializer.c(40);
		this.b = serializer.a(PacketPlayInResourcePackStatus.EnumResourcePackStatus.class);
	}

	@Override
	public void b(PacketDataSerializer serializer) throws IOException {
		serializer.a(this.a);
		serializer.a(this.b);
	}

	@Override
	public void a(PacketListenerPlayIn packetlistenerplayin) {
		packetlistenerplayin.a(this);
	}

	public static enum EnumResourcePackStatus {

		SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;

		private EnumResourcePackStatus() {
		}
	}
}
