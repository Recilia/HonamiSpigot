package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ConversationFactory {

	protected Plugin plugin;
	protected boolean isModal;
	protected boolean localEchoEnabled;
	protected ConversationPrefix prefix;
	protected Prompt firstPrompt;
	protected Map<Object, Object> initialSessionData;
	protected String playerOnlyMessage;
	protected List<ConversationCanceller> cancellers;
	protected List<ConversationAbandonedListener> abandonedListeners;

	public ConversationFactory(Plugin plugin) {
		this.plugin = plugin;
		isModal = true;
		localEchoEnabled = true;
		prefix = new NullConversationPrefix();
		firstPrompt = Prompt.END_OF_CONVERSATION;
		initialSessionData = new HashMap<Object, Object>();
		playerOnlyMessage = null;
		cancellers = new ArrayList<ConversationCanceller>();
		abandonedListeners = new ArrayList<ConversationAbandonedListener>();
	}

	public ConversationFactory withModality(boolean modal) {
		isModal = modal;
		return this;
	}

	public ConversationFactory withLocalEcho(boolean localEchoEnabled) {
		this.localEchoEnabled = localEchoEnabled;
		return this;
	}

	public ConversationFactory withPrefix(ConversationPrefix prefix) {
		this.prefix = prefix;
		return this;
	}

	public ConversationFactory withTimeout(int timeoutSeconds) {
		return withConversationCanceller(new InactivityConversationCanceller(plugin, timeoutSeconds));
	}

	public ConversationFactory withFirstPrompt(Prompt firstPrompt) {
		this.firstPrompt = firstPrompt;
		return this;
	}

	public ConversationFactory withInitialSessionData(Map<Object, Object> initialSessionData) {
		this.initialSessionData = initialSessionData;
		return this;
	}

	public ConversationFactory withEscapeSequence(String escapeSequence) {
		return withConversationCanceller(new ExactMatchConversationCanceller(escapeSequence));
	}

	public ConversationFactory withConversationCanceller(ConversationCanceller canceller) {
		cancellers.add(canceller);
		return this;
	}

	public ConversationFactory thatExcludesNonPlayersWithMessage(String playerOnlyMessage) {
		this.playerOnlyMessage = playerOnlyMessage;
		return this;
	}

	public ConversationFactory addConversationAbandonedListener(ConversationAbandonedListener listener) {
		abandonedListeners.add(listener);
		return this;
	}

	public Conversation buildConversation(Conversable forWhom) {
		
		if (playerOnlyMessage != null && !(forWhom instanceof Player)) {
			return new Conversation(plugin, forWhom, new NotPlayerMessagePrompt());
		}

		Map<Object, Object> copiedInitialSessionData = new HashMap<Object, Object>();
		copiedInitialSessionData.putAll(initialSessionData);

		Conversation conversation = new Conversation(plugin, forWhom, firstPrompt, copiedInitialSessionData);
		conversation.setModal(isModal);
		conversation.setLocalEchoEnabled(localEchoEnabled);
		conversation.setPrefix(prefix);

		for (ConversationCanceller canceller : cancellers) {
			conversation.addConversationCanceller(canceller.clone());
		}

		for (ConversationAbandonedListener listener : abandonedListeners) {
			conversation.addConversationAbandonedListener(listener);
		}

		return conversation;
	}

	private class NotPlayerMessagePrompt extends MessagePrompt {

		public String getPromptText(ConversationContext context) {
			return playerOnlyMessage;
		}

		@Override
		protected Prompt getNextPrompt(ConversationContext context) {
			return Prompt.END_OF_CONVERSATION;
		}
	}
}
