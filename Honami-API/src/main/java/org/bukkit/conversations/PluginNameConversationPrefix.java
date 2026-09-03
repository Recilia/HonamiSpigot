package org.bukkit.conversations;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class PluginNameConversationPrefix implements ConversationPrefix {

	protected String separator;
	protected ChatColor prefixColor;
	protected Plugin plugin;

	private String cachedPrefix;

	public PluginNameConversationPrefix(Plugin plugin) {
		this(plugin, " > ", ChatColor.LIGHT_PURPLE);
	}

	public PluginNameConversationPrefix(Plugin plugin, String separator, ChatColor prefixColor) {
		this.separator = separator;
		this.prefixColor = prefixColor;
		this.plugin = plugin;

		cachedPrefix = prefixColor + plugin.getDescription().getName() + separator + ChatColor.WHITE;
	}

	public String getPrefix(ConversationContext context) {
		return cachedPrefix;
	}
}
