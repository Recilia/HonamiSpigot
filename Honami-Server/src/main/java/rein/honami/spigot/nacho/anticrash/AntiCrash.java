package rein.honami.spigot.nacho.anticrash;

import rein.honami.spigot.protocol.PacketListener;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketPlayInCustomPayload;
import net.minecraft.server.PlayerConnection;

public class AntiCrash implements PacketListener {
	@Override
	public boolean onReceivedPacket(PlayerConnection playerConnection, Packet packet) {
		if (packet instanceof PacketPlayInCustomPayload) {
			PacketDataSerializer ab = ((PacketPlayInCustomPayload) packet).b();
			if (ab.refCnt() < 1) {
				playerConnection.getNetworkManager().close(new ChatMessage("Wrong ref count!"));
				return false;
			}
			if (ab.readableBytes() > 25780) {
				playerConnection.getNetworkManager().close(new ChatMessage("Readable bytes exceeds limit!"));
				return false;
			}

		}
		return true;
	}
}
