package org.bukkit;

import java.util.Date;

public interface BanEntry {

	public String getTarget();

	public Date getCreated();

	public void setCreated(Date created);

	public String getSource();

	public void setSource(String source);

	public Date getExpiration();

	public void setExpiration(Date expiration);

	public String getReason();

	public void setReason(String reason);

	public void save();
}
