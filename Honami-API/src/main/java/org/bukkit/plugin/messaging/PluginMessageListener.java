package org.bukkit.plugin.messaging;

import org.bukkit.entity.Player;

public interface PluginMessageListener {

	public void onPluginMessageReceived(String channel, Player player, byte[] message);
}
