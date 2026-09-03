package org.bukkit.permissions;

import org.bukkit.entity.Player;

public interface ServerOperator {

	public boolean isOp();

	public void setOp(boolean value);
}
