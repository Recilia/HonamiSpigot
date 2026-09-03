/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package co.aikar.timings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;

import com.google.common.base.Function;
import com.google.common.collect.EvictingQueue;

import co.aikar.util.LoadingMap;

public final class TimingsManager {
	static final Map<TimingIdentifier, TimingHandler> TIMING_MAP = Collections
			.synchronizedMap(LoadingMap.newHashMap(new Function<TimingIdentifier, TimingHandler>() {
				@Override
				public TimingHandler apply(TimingIdentifier id) {
					return (id.protect ? new UnsafeTimingHandler(id) : new TimingHandler(id));
				}
			}, 256, .5F));
	public static final FullServerTickHandler FULL_SERVER_TICK = new FullServerTickHandler();
	public static final TimingHandler TIMINGS_TICK = Timings.ofSafe("Timings Tick", FULL_SERVER_TICK);
	public static final Timing PLUGIN_GROUP_HANDLER = Timings.ofSafe("Plugins");
	public static List<String> hiddenConfigs = new ArrayList<String>();
	public static boolean privacy = false;

	static final Collection<TimingHandler> HANDLERS = new ArrayDeque<TimingHandler>();
	static final ArrayDeque<TimingHistory.MinuteReport> MINUTE_REPORTS = new ArrayDeque<TimingHistory.MinuteReport>();

	static EvictingQueue<TimingHistory> HISTORY = EvictingQueue.create(12);
	static TimingHandler CURRENT;
	static long timingStart = 0;
	static long historyStart = 0;
	static boolean needsFullReset = false;
	static boolean needsRecheckEnabled = false;

	private TimingsManager() {
	}

	static void reset() {
		needsFullReset = true;
	}

	static void tick() {
		if (Timings.timingsEnabled) {
			boolean violated = FULL_SERVER_TICK.isViolated();

			for (TimingHandler handler : HANDLERS) {
				if (handler.isSpecial()) {
					
					continue;
				}
				handler.processTick(violated);
			}

			TimingHistory.playerTicks += Bukkit.getOnlinePlayers().size();
			TimingHistory.timedTicks++;
			
		}
	}

	static void stopServer() {
		Timings.timingsEnabled = false;
		recheckEnabled();
	}

	static void recheckEnabled() {
		synchronized (TIMING_MAP) {
			for (TimingHandler timings : TIMING_MAP.values()) {
				timings.checkEnabled();
			}
		}
		needsRecheckEnabled = false;
	}

	static void resetTimings() {
		if (needsFullReset) {

			synchronized (TIMING_MAP) {
				for (TimingHandler timings : TIMING_MAP.values()) {
					timings.reset(true);
				}
			}
			Bukkit.getLogger().log(Level.INFO, "Timings Reset");
			HISTORY.clear();
			needsFullReset = false;
			needsRecheckEnabled = false;
			timingStart = System.currentTimeMillis();
		} else {

			for (TimingHandler timings : HANDLERS) {
				timings.reset(false);
			}
		}

		HANDLERS.clear();
		MINUTE_REPORTS.clear();

		TimingHistory.resetTicks(true);
		historyStart = System.currentTimeMillis();
	}

	static TimingHandler getHandler(String group, String name, Timing parent, boolean protect) {
		return TIMING_MAP.get(new TimingIdentifier(group, name, parent, protect));
	}

	public static Timing getCommandTiming(String pluginName, Command command) {
		Plugin plugin = null;
		final Server server = Bukkit.getServer();
		if (!("minecraft".equals(pluginName) || "bukkit".equals(pluginName) || "Spigot".equals(pluginName)
				|| server == null)) {
			plugin = server.getPluginManager().getPlugin(pluginName);
			if (plugin == null) {
				
				plugin = getPluginByClassloader(command.getClass());
			}
		}
		if (plugin == null) {
			return Timings.ofSafe("Command: " + pluginName + ":" + command.getTimingName());
		}

		return Timings.ofSafe(plugin, "Command: " + pluginName + ":" + command.getTimingName());
	}

	public static Plugin getPluginByClassloader(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		final ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader instanceof PluginClassLoader) {
			PluginClassLoader pluginClassLoader = (PluginClassLoader) classLoader;
			return pluginClassLoader.getPlugin();
		}
		return null;
	}
}
