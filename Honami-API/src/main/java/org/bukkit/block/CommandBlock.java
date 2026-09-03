package org.bukkit.block;

public interface CommandBlock extends BlockState {

	public String getCommand();

	public void setCommand(String command);

	public String getName();

	public void setName(String name);
}
