package org.bukkit.conversations;

public class NullConversationPrefix implements ConversationPrefix {

	public String getPrefix(ConversationContext context) {
		return "";
	}
}
