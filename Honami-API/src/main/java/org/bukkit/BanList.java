package org.bukkit;

import java.util.Date;
import java.util.Set;

public interface BanList {

	public enum Type {

		NAME,

		IP,;
	}

	public BanEntry getBanEntry(String target);

	public BanEntry addBan(String target, String reason, Date expires, String source);

	public Set<BanEntry> getBanEntries();

	public boolean isBanned(String target);

	public void pardon(String target);
}
