package org.bukkit.conversations;

import org.bukkit.ChatColor;

public abstract class ValidatingPrompt implements Prompt {
	public ValidatingPrompt() {
		super();
	}

	public Prompt acceptInput(ConversationContext context, String input) {
		if (isInputValid(context, input)) {
			return acceptValidatedInput(context, input);
		} else {
			String failPrompt = getFailedValidationText(context, input);
			if (failPrompt != null) {
				context.getForWhom().sendRawMessage(ChatColor.RED + failPrompt);
			}
			
			return this;
		}
	}

	public boolean blocksForInput(ConversationContext context) {
		return true;
	}

	protected abstract boolean isInputValid(ConversationContext context, String input);

	protected abstract Prompt acceptValidatedInput(ConversationContext context, String input);

	protected String getFailedValidationText(ConversationContext context, String invalidInput) {
		return null;
	}
}
