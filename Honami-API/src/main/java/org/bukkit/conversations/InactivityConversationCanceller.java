package org.bukkit.conversations;

import org.bukkit.plugin.Plugin;

public class InactivityConversationCanceller implements ConversationCanceller {
	protected Plugin plugin;
	protected int timeoutSeconds;
	protected Conversation conversation;
	private int taskId = -1;

	public InactivityConversationCanceller(Plugin plugin, int timeoutSeconds) {
		this.plugin = plugin;
		this.timeoutSeconds = timeoutSeconds;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
		startTimer();
	}

	public boolean cancelBasedOnInput(ConversationContext context, String input) {
		
		stopTimer();
		startTimer();
		return false;
	}

	public ConversationCanceller clone() {
		return new InactivityConversationCanceller(plugin, timeoutSeconds);
	}

	private void startTimer() {
		taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (conversation.getState() == Conversation.ConversationState.UNSTARTED) {
					startTimer();
				} else if (conversation.getState() == Conversation.ConversationState.STARTED) {
					cancelling(conversation);
					conversation.abandon(
							new ConversationAbandonedEvent(conversation, InactivityConversationCanceller.this));
				}
			}
		}, timeoutSeconds * 20);
	}

	private void stopTimer() {
		if (taskId != -1) {
			plugin.getServer().getScheduler().cancelTask(taskId);
			taskId = -1;
		}
	}

	protected void cancelling(Conversation conversation) {

	}
}
