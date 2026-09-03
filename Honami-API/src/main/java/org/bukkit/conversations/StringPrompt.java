package org.bukkit.conversations;

public abstract class StringPrompt implements Prompt {

	public boolean blocksForInput(ConversationContext context) {
		return true;
	}
}
