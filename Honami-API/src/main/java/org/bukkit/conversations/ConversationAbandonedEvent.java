package org.bukkit.conversations;

import java.util.EventObject;

public class ConversationAbandonedEvent extends EventObject {

	private ConversationContext context;
	private ConversationCanceller canceller;

	public ConversationAbandonedEvent(Conversation conversation) {
		this(conversation, null);
	}

	public ConversationAbandonedEvent(Conversation conversation, ConversationCanceller canceller) {
		super(conversation);
		this.context = conversation.getContext();
		this.canceller = canceller;
	}

	public ConversationCanceller getCanceller() {
		return canceller;
	}

	public ConversationContext getContext() {
		return context;
	}

	public boolean gracefulExit() {
		return canceller == null;
	}
}
