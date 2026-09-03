package org.bukkit.plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PluginLogger extends Logger {
	private String pluginName;

	public PluginLogger(Plugin context) {
		super(context.getClass().getCanonicalName(), null);
		String prefix = context.getDescription().getPrefix();
		pluginName = prefix != null ? new StringBuilder().append("[").append(prefix).append("] ").toString()
				: "[" + context.getDescription().getName() + "] ";
		setParent(context.getServer().getLogger());
		setLevel(Level.ALL);
	}

	@Override
	public void log(LogRecord logRecord) {
		logRecord.setMessage(pluginName + logRecord.getMessage());
		super.log(logRecord);
	}

}
