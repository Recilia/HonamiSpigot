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

import java.util.Queue;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.EvictingQueue;

@SuppressWarnings("UnusedDeclaration")
public final class Timings {

	private static final int MAX_HISTORY_FRAMES = 12;
	public static final Timing NULL_HANDLER = new NullTimingHandler();
	static boolean timingsEnabled = false;
	static boolean verboseEnabled = false;
	private static int historyInterval = -1;
	private static int historyLength = -1;

	private Timings() {
	}

	public static Timing of(Plugin plugin, String name) {
		Timing pluginHandler = null;
		if (plugin != null) {
			pluginHandler = ofSafe(plugin.getName(), "Combined Total", TimingsManager.PLUGIN_GROUP_HANDLER);
		}
		return of(plugin, name, pluginHandler);
	}

	public static Timing of(Plugin plugin, String name, Timing groupHandler) {
		Preconditions.checkNotNull(plugin, "Plugin can not be null");
		return TimingsManager.getHandler(plugin.getName(), name, groupHandler, true);
	}

	public static Timing ofStart(Plugin plugin, String name) {
		return ofStart(plugin, name, null);
	}

	public static Timing ofStart(Plugin plugin, String name, Timing groupHandler) {
		Timing timing = of(plugin, name, groupHandler);
		timing.startTimingIfSync();
		return timing;
	}

	public static boolean isTimingsEnabled() {
		return timingsEnabled;
	}

	public static void setTimingsEnabled(boolean enabled) {
		timingsEnabled = enabled;
		reset();
	}

	public static boolean isVerboseTimingsEnabled() {
		return timingsEnabled;
	}

	public static void setVerboseTimingsEnabled(boolean enabled) {
		verboseEnabled = enabled;
		TimingsManager.needsRecheckEnabled = true;
	}

	public static int getHistoryInterval() {
		return historyInterval;
	}

	public static void setHistoryInterval(int interval) {
		historyInterval = Math.max(20 * 60, interval);
		
		if (historyLength != -1) {
			setHistoryLength(historyLength);
		}
	}

	public static int getHistoryLength() {
		return historyLength;
	}

	public static void setHistoryLength(int length) {
		
		int maxLength = historyInterval * MAX_HISTORY_FRAMES;

		

		if (System.getProperty("timings.bypassMax") != null) {
			maxLength = Integer.MAX_VALUE;
		}
		historyLength = Math.max(Math.min(maxLength, length), historyInterval);
		Queue<TimingHistory> oldQueue = TimingsManager.HISTORY;
		int frames = (getHistoryLength() / getHistoryInterval());
		if (length > maxLength) {
			Bukkit.getLogger().log(Level.WARNING,
					"Timings Length too high. Requested " + length + ", max is " + maxLength
							+ ". To get longer history, you must increase your interval. Set Interval to "
							+ Math.ceil(length / MAX_HISTORY_FRAMES) + " to achieve this length.");
		}
		TimingsManager.HISTORY = EvictingQueue.create(frames);
		TimingsManager.HISTORY.addAll(oldQueue);
	}

	public static void reset() {
		TimingsManager.reset();
	}

	public static void generateReport(CommandSender sender) {
		if (sender == null) {
			sender = Bukkit.getConsoleSender();
		}
		TimingsExport.reportTimings(sender);
	}

	static TimingHandler ofSafe(String name) {
		return ofSafe(null, name, null);
	}

	static Timing ofSafe(Plugin plugin, String name) {
		Timing pluginHandler = null;
		if (plugin != null) {
			pluginHandler = ofSafe(plugin.getName(), "Combined Total", TimingsManager.PLUGIN_GROUP_HANDLER);
		}
		return ofSafe(plugin != null ? plugin.getName() : "Minecraft - Invalid Plugin", name, pluginHandler);
	}

	static TimingHandler ofSafe(String name, Timing groupHandler) {
		return ofSafe(null, name, groupHandler);
	}

	static TimingHandler ofSafe(String groupName, String name, Timing groupHandler) {
		return TimingsManager.getHandler(groupName, name, groupHandler, false);
	}
}
