package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;

public class Conversation {

	private Prompt firstPrompt;
	private boolean abandoned;
	protected Prompt currentPrompt;
	protected ConversationContext context;
	protected boolean modal;
	protected boolean localEchoEnabled;
	protected ConversationPrefix prefix;
	protected List<ConversationCanceller> cancellers;
	protected List<ConversationAbandonedListener> abandonedListeners;

	public Conversation(Plugin plugin, Conversable forWhom, Prompt firstPrompt) {
		this(plugin, forWhom, firstPrompt, new HashMap<Object, Object>());
	}

	public Conversation(Plugin plugin, Conversable forWhom, Prompt firstPrompt,
			Map<Object, Object> initialSessionData) {
		this.firstPrompt = firstPrompt;
		this.context = new ConversationContext(plugin, forWhom, initialSessionData);
		this.modal = true;
		this.localEchoEnabled = true;
		this.prefix = new NullConversationPrefix();
		this.cancellers = new ArrayList<ConversationCanceller>();
		this.abandonedListeners = new ArrayList<ConversationAbandonedListener>();
	}

	public Conversable getForWhom() {
		return context.getForWhom();
	}

	public boolean isModal() {
		return modal;
	}

	void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isLocalEchoEnabled() {
		return localEchoEnabled;
	}

	public void setLocalEchoEnabled(boolean localEchoEnabled) {
		this.localEchoEnabled = localEchoEnabled;
	}

	public ConversationPrefix getPrefix() {
		return prefix;
	}

	void setPrefix(ConversationPrefix prefix) {
		this.prefix = prefix;
	}

	void addConversationCanceller(ConversationCanceller canceller) {
		canceller.setConversation(this);
		this.cancellers.add(canceller);
	}

	public List<ConversationCanceller> getCancellers() {
		return cancellers;
	}

	public ConversationContext getContext() {
		return context;
	}

	public void begin() {
		if (currentPrompt == null) {
			abandoned = false;
			currentPrompt = firstPrompt;
			context.getForWhom().beginConversation(this);
		}
	}

	public ConversationState getState() {
		if (currentPrompt != null) {
			return ConversationState.STARTED;
		} else if (abandoned) {
			return ConversationState.ABANDONED;
		} else {
			return ConversationState.UNSTARTED;
		}
	}

	public void acceptInput(String input) {
		try { 
			if (currentPrompt != null) {

				if (localEchoEnabled) {
					context.getForWhom().sendRawMessage(prefix.getPrefix(context) + input);
				}

				for (ConversationCanceller canceller : cancellers) {
					if (canceller.cancelBasedOnInput(context, input)) {
						abandon(new ConversationAbandonedEvent(this, canceller));
						return;
					}
				}

				currentPrompt = currentPrompt.acceptInput(context, input);
				outputNextPrompt();
			}
			
		} catch (Throwable t) {
			org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.SEVERE, "Error handling conversation prompt", t);
		}
		
	}

	public synchronized void addConversationAbandonedListener(ConversationAbandonedListener listener) {
		abandonedListeners.add(listener);
	}

	public synchronized void removeConversationAbandonedListener(ConversationAbandonedListener listener) {
		abandonedListeners.remove(listener);
	}

	public void abandon() {
		abandon(new ConversationAbandonedEvent(this, new ManuallyAbandonedConversationCanceller()));
	}

	public synchronized void abandon(ConversationAbandonedEvent details) {
		if (!abandoned) {
			abandoned = true;
			currentPrompt = null;
			context.getForWhom().abandonConversation(this);
			for (ConversationAbandonedListener listener : abandonedListeners) {
				listener.conversationAbandoned(details);
			}
		}
	}

	public void outputNextPrompt() {
		if (currentPrompt == null) {
			abandon(new ConversationAbandonedEvent(this));
		} else {
			context.getForWhom().sendRawMessage(prefix.getPrefix(context) + currentPrompt.getPromptText(context));
			if (!currentPrompt.blocksForInput(context)) {
				currentPrompt = currentPrompt.acceptInput(context, null);
				outputNextPrompt();
			}
		}
	}

	public enum ConversationState {
		UNSTARTED, STARTED, ABANDONED
	}
}
