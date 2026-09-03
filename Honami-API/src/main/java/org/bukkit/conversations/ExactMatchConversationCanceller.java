package org.bukkit.conversations;

public class ExactMatchConversationCanceller implements ConversationCanceller {
	private String escapeSequence;

	public ExactMatchConversationCanceller(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}

	public void setConversation(Conversation conversation) {
	}

	public boolean cancelBasedOnInput(ConversationContext context, String input) {
		return input.equals(escapeSequence);
	}

	public ConversationCanceller clone() {
		return new ExactMatchConversationCanceller(escapeSequence);
	}
}
