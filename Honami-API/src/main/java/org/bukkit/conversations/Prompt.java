package org.bukkit.conversations;

public interface Prompt extends Cloneable {

	static final Prompt END_OF_CONVERSATION = null;

	String getPromptText(ConversationContext context);

	boolean blocksForInput(ConversationContext context);

	Prompt acceptInput(ConversationContext context, String input);
}
