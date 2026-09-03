package org.bukkit.craftbukkit.help;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpTopic;

import com.google.common.base.Charsets;

public class HelpYamlReader {

	private YamlConfiguration helpYaml;
	private final char ALT_COLOR_CODE = '&';
	private final Server server;

	public HelpYamlReader(Server server) {
		this.server = server;

		File helpYamlFile = new File("help.yml");
		YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream("configurations/help.yml"), Charsets.UTF_8));

		try {
			helpYaml = YamlConfiguration.loadConfiguration(helpYamlFile);
			helpYaml.options().copyDefaults(true);
			helpYaml.setDefaults(defaultConfig);

			try {
				if (!helpYamlFile.exists()) {
					helpYaml.save(helpYamlFile);
				}
			} catch (IOException ex) {
				server.getLogger().log(Level.SEVERE, "Could not save " + helpYamlFile, ex);
			}
		} catch (Exception ex) {
			server.getLogger().severe(
					"Failed to load help.yml. Verify the yaml indentation is correct. Reverting to default help.yml.");
			helpYaml = defaultConfig;
		}
	}

	public List<HelpTopic> getGeneralTopics() {
		List<HelpTopic> topics = new LinkedList<HelpTopic>();
		ConfigurationSection generalTopics = helpYaml.getConfigurationSection("general-topics");
		if (generalTopics != null) {
			for (String topicName : generalTopics.getKeys(false)) {
				ConfigurationSection section = generalTopics.getConfigurationSection(topicName);
				String shortText = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("shortText", ""));
				String fullText = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("fullText", ""));
				String permission = section.getString("permission", "");
				topics.add(new CustomHelpTopic(topicName, shortText, fullText, permission));
			}
		}
		return topics;
	}

	public List<HelpTopic> getIndexTopics() {
		List<HelpTopic> topics = new LinkedList<HelpTopic>();
		ConfigurationSection indexTopics = helpYaml.getConfigurationSection("index-topics");
		if (indexTopics != null) {
			for (String topicName : indexTopics.getKeys(false)) {
				ConfigurationSection section = indexTopics.getConfigurationSection(topicName);
				String shortText = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("shortText", ""));
				String preamble = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("preamble", ""));
				String permission = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("permission", ""));
				List<String> commands = section.getStringList("commands");
				topics.add(new CustomIndexHelpTopic(server.getHelpMap(), topicName, shortText, permission, commands,
						preamble));
			}
		}
		return topics;
	}

	public List<HelpTopicAmendment> getTopicAmendments() {
		List<HelpTopicAmendment> amendments = new LinkedList<HelpTopicAmendment>();
		ConfigurationSection commandTopics = helpYaml.getConfigurationSection("amended-topics");
		if (commandTopics != null) {
			for (String topicName : commandTopics.getKeys(false)) {
				ConfigurationSection section = commandTopics.getConfigurationSection(topicName);
				String description = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("shortText", ""));
				String usage = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE,
						section.getString("fullText", ""));
				String permission = section.getString("permission", "");
				amendments.add(new HelpTopicAmendment(topicName, description, usage, permission));
			}
		}
		return amendments;
	}

	public List<String> getIgnoredPlugins() {
		return helpYaml.getStringList("ignore-plugins");
	}

	public boolean commandTopicsInMasterIndex() {
		return helpYaml.getBoolean("command-topics-in-master-index", true);
	}
}
