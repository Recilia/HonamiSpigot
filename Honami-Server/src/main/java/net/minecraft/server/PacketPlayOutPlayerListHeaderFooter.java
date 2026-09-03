package net.minecraft.server;

import java.io.IOException;

public class PacketPlayOutPlayerListHeaderFooter implements Packet<PacketListenerPlayOut> {

	public net.md_5.bungee.api.chat.BaseComponent[] header, footer; 

	private IChatBaseComponent a;
	private IChatBaseComponent b;

	public PacketPlayOutPlayerListHeaderFooter() {
	}

	public PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent ichatbasecomponent) {
		this.a = ichatbasecomponent;
	}

	@Override
	public void a(PacketDataSerializer serializer) throws IOException {
		this.a = serializer.d();
		this.b = serializer.d();
	}

	@Override
	public void b(PacketDataSerializer serializer) throws IOException {
		
		if (this.header != null) {
			serializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(this.header));
		} else {
			serializer.a(this.a);
		}

		if (this.footer != null) {
			serializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(this.footer));
		} else {
			serializer.a(this.b);
		}
		
	}

	@Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}

	

	
}
