package org.bukkit.plugin.messaging;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Messenger {

	public static final int MAX_MESSAGE_SIZE = 32766;

	public static final int MAX_CHANNEL_SIZE = 20;

	public boolean isReservedChannel(String channel);

	public void registerOutgoingPluginChannel(Plugin plugin, String channel);

	public void unregisterOutgoingPluginChannel(Plugin plugin, String channel);

	public void unregisterOutgoingPluginChannel(Plugin plugin);

	public PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin plugin, String channel,
			PluginMessageListener listener);

	public void unregisterIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener);

	public void unregisterIncomingPluginChannel(Plugin plugin, String channel);

	public void unregisterIncomingPluginChannel(Plugin plugin);

	public Set<String> getOutgoingChannels();

	public Set<String> getOutgoingChannels(Plugin plugin);

	public Set<String> getIncomingChannels();

	public Set<String> getIncomingChannels(Plugin plugin);

	public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin plugin);

	public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(String channel);

	public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin plugin, String channel);

	public boolean isRegistrationValid(PluginMessageListenerRegistration registration);

	public boolean isIncomingChannelRegistered(Plugin plugin, String channel);

	public boolean isOutgoingChannelRegistered(Plugin plugin, String channel);

	public void dispatchIncomingMessage(Player source, String channel, byte[] message);
}
