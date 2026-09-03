package org.bukkit.help;

import java.util.Collection;
import java.util.List;

public interface HelpMap {

	public HelpTopic getHelpTopic(String topicName);

	public Collection<HelpTopic> getHelpTopics();

	public void addTopic(HelpTopic topic);

	public void clear();

	public void registerHelpTopicFactory(Class<?> commandClass, HelpTopicFactory<?> factory);

	public List<String> getIgnoredPlugins();
}
