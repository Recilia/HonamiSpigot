package org.bukkit.conversations;

public interface Conversable {

	public boolean isConversing();

	public void acceptConversationInput(String input);

	public boolean beginConversation(Conversation conversation);

	public void abandonConversation(Conversation conversation);

	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details);

	public void sendRawMessage(String message);
}
