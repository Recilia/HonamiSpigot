package org.bukkit.block;

public interface Sign extends BlockState {

	public String[] getLines();

	public String getLine(int index) throws IndexOutOfBoundsException;

	public void setLine(int index, String line) throws IndexOutOfBoundsException;
}
