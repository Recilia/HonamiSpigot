package org.bukkit.scoreboard;

import java.util.Set;

import org.bukkit.OfflinePlayer;

public interface Scoreboard {

	Objective registerNewObjective(String name, String criteria) throws IllegalArgumentException;

	Objective getObjective(String name) throws IllegalArgumentException;

	Set<Objective> getObjectivesByCriteria(String criteria) throws IllegalArgumentException;

	Set<Objective> getObjectives();

	Objective getObjective(DisplaySlot slot) throws IllegalArgumentException;

	@Deprecated
	Set<Score> getScores(OfflinePlayer player) throws IllegalArgumentException;

	Set<Score> getScores(String entry) throws IllegalArgumentException;

	@Deprecated
	void resetScores(OfflinePlayer player) throws IllegalArgumentException;

	void resetScores(String entry) throws IllegalArgumentException;

	@Deprecated
	Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException;

	Team getEntryTeam(String entry) throws IllegalArgumentException;

	Team getTeam(String teamName) throws IllegalArgumentException;

	Set<Team> getTeams();

	Team registerNewTeam(String name) throws IllegalArgumentException;

	@Deprecated
	Set<OfflinePlayer> getPlayers();

	Set<String> getEntries();

	void clearSlot(DisplaySlot slot) throws IllegalArgumentException;
}
