package org.bukkit.help;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class HelpTopic {
	protected String name;
	protected String shortText;
	protected String fullText;
	protected String amendedPermission;

	public abstract boolean canSee(CommandSender player);

	public void amendCanSee(String amendedPermission) {
		this.amendedPermission = amendedPermission;
	}

	public String getName() {
		return name;
	}

	public String getShortText() {
		return shortText;
	}

	public String getFullText(CommandSender forWho) {
		return fullText;
	}

	public void amendTopic(String amendedShortText, String amendedFullText) {
		shortText = applyAmendment(shortText, amendedShortText);
		fullText = applyAmendment(fullText, amendedFullText);
	}

	protected String applyAmendment(String baseText, String amendment) {
		if (amendment == null) {
			return baseText;
		} else {
			return amendment.replaceAll("<text>", baseText);
		}
	}
}
