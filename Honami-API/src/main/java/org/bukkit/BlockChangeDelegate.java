package org.bukkit;

public interface BlockChangeDelegate {

	@Deprecated
	public boolean setRawTypeId(int x, int y, int z, int typeId);

	@Deprecated
	public boolean setRawTypeIdAndData(int x, int y, int z, int typeId, int data);

	@Deprecated
	public boolean setTypeId(int x, int y, int z, int typeId);

	@Deprecated
	public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data);

	@Deprecated
	public int getTypeId(int x, int y, int z);

	public int getHeight();

	public boolean isEmpty(int x, int y, int z);
}
