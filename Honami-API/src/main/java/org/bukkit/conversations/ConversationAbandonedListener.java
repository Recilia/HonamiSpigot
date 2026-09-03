package org.bukkit.conversations;

import java.util.EventListener;

public interface ConversationAbandonedListener extends EventListener {

	public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent);
}
