package org.bukkit.command;

import org.bukkit.Server;
import org.bukkit.permissions.Permissible;

public interface CommandSender extends Permissible {

	public void sendMessage(String message);

	public void sendMessage(String[] messages);

	public Server getServer();

	public String getName();

	

	default void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
		this.sendMessage(component.toLegacyText());
	}

	default void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
		this.sendMessage(new net.md_5.bungee.api.chat.TextComponent(components).toLegacyText());
	}
	
}
