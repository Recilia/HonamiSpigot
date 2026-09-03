package org.bukkit.conversations;

public class ManuallyAbandonedConversationCanceller implements ConversationCanceller {
	public void setConversation(Conversation conversation) {
		throw new UnsupportedOperationException();
	}

	public boolean cancelBasedOnInput(ConversationContext context, String input) {
		throw new UnsupportedOperationException();
	}

	public ConversationCanceller clone() {
		throw new UnsupportedOperationException();
	}
}
