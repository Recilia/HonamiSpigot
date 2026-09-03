package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

public interface Score {

	@Deprecated
	OfflinePlayer getPlayer();

	String getEntry();

	Objective getObjective();

	int getScore() throws IllegalStateException;

	void setScore(int score) throws IllegalStateException;

	

	boolean isScoreSet() throws IllegalStateException;

	

	Scoreboard getScoreboard();
}
