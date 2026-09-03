package org.bukkit.conversations;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class FixedSetPrompt extends ValidatingPrompt {

	protected List<String> fixedSet;

	public FixedSetPrompt(String... fixedSet) {
		super();
		this.fixedSet = Arrays.asList(fixedSet);
	}

	private FixedSetPrompt() {
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		return fixedSet.contains(input);
	}

	protected String formatFixedSet() {
		return "[" + StringUtils.join(fixedSet, ", ") + "]";
	}
}
