package org.bukkit.plugin.messaging;

import java.util.Set;

import org.bukkit.plugin.Plugin;

public interface PluginMessageRecipient {

	public void sendPluginMessage(Plugin source, String channel, byte[] message);

	public Set<String> getListeningPluginChannels();
}
