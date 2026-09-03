package org.bukkit.craftbukkit.help;

public class HelpTopicAmendment {
	private final String topicName;
	private final String shortText;
	private final String fullText;
	private final String permission;

	public HelpTopicAmendment(String topicName, String shortText, String fullText, String permission) {
		this.fullText = fullText;
		this.shortText = shortText;
		this.topicName = topicName;
		this.permission = permission;
	}

	public String getFullText() {
		return fullText;
	}

	public String getShortText() {
		return shortText;
	}

	public String getTopicName() {
		return topicName;
	}

	public String getPermission() {
		return permission;
	}
}
