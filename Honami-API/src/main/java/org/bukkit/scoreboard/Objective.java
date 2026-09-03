package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

public interface Objective {

	String getName() throws IllegalStateException;

	String getDisplayName() throws IllegalStateException;

	void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException;

	String getCriteria() throws IllegalStateException;

	boolean isModifiable() throws IllegalStateException;

	Scoreboard getScoreboard();

	void unregister() throws IllegalStateException;

	void setDisplaySlot(DisplaySlot slot) throws IllegalStateException;

	DisplaySlot getDisplaySlot() throws IllegalStateException;

	@Deprecated
	Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException;

	Score getScore(String entry) throws IllegalArgumentException, IllegalStateException;
}
