package org.bukkit.map;

import java.util.List;

import org.bukkit.World;

public interface MapView {

	public static enum Scale {
		CLOSEST(0), CLOSE(1), NORMAL(2), FAR(3), FARTHEST(4);

		private byte value;

		private Scale(int value) {
			this.value = (byte) value;
		}

		@Deprecated
		public static Scale valueOf(byte value) {
			switch (value) {
			case 0:
				return CLOSEST;
			case 1:
				return CLOSE;
			case 2:
				return NORMAL;
			case 3:
				return FAR;
			case 4:
				return FARTHEST;
			default:
				return null;
			}
		}

		@Deprecated
		public byte getValue() {
			return value;
		}
	}

	@Deprecated
	public short getId();

	public boolean isVirtual();

	public Scale getScale();

	public void setScale(Scale scale);

	public int getCenterX();

	public int getCenterZ();

	public void setCenterX(int x);

	public void setCenterZ(int z);

	public World getWorld();

	public void setWorld(World world);

	public List<MapRenderer> getRenderers();

	public void addRenderer(MapRenderer renderer);

	public boolean removeRenderer(MapRenderer renderer);

}
