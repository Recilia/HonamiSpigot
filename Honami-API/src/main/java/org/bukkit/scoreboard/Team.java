package org.bukkit.scoreboard;

import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.potion.PotionEffectType;

public interface Team {

	String getName() throws IllegalStateException;

	String getDisplayName() throws IllegalStateException;

	void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException;

	String getPrefix() throws IllegalStateException;

	void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException;

	String getSuffix() throws IllegalStateException;

	void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException;

	boolean allowFriendlyFire() throws IllegalStateException;

	void setAllowFriendlyFire(boolean enabled) throws IllegalStateException;

	boolean canSeeFriendlyInvisibles() throws IllegalStateException;

	void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException;

	NameTagVisibility getNameTagVisibility() throws IllegalArgumentException;

	void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException;

	@Deprecated
	Set<OfflinePlayer> getPlayers() throws IllegalStateException;

	Set<String> getEntries() throws IllegalStateException;

	int getSize() throws IllegalStateException;

	Scoreboard getScoreboard();

	@Deprecated
	void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException;

	void addEntry(String entry) throws IllegalStateException, IllegalArgumentException;

	@Deprecated
	boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException;

	boolean removeEntry(String entry) throws IllegalStateException, IllegalArgumentException;

	void unregister() throws IllegalStateException;

	@Deprecated
	boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException;

	boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException;
}
