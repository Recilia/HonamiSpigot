package org.bukkit.conversations;

public interface ConversationCanceller extends Cloneable {

	public void setConversation(Conversation conversation);

	public boolean cancelBasedOnInput(ConversationContext context, String input);

	public ConversationCanceller clone();
}
