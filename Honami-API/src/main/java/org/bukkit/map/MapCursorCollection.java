package org.bukkit.map;

import java.util.ArrayList;
import java.util.List;

public final class MapCursorCollection {
	private List<MapCursor> cursors = new ArrayList<MapCursor>();

	public int size() {
		return cursors.size();
	}

	public MapCursor getCursor(int index) {
		return cursors.get(index);
	}

	public boolean removeCursor(MapCursor cursor) {
		return cursors.remove(cursor);
	}

	public MapCursor addCursor(MapCursor cursor) {
		cursors.add(cursor);
		return cursor;
	}

	public MapCursor addCursor(int x, int y, byte direction) {
		return addCursor(x, y, direction, (byte) 0, true);
	}

	@Deprecated
	public MapCursor addCursor(int x, int y, byte direction, byte type) {
		return addCursor(x, y, direction, type, true);
	}

	@Deprecated
	public MapCursor addCursor(int x, int y, byte direction, byte type, boolean visible) {
		return addCursor(new MapCursor((byte) x, (byte) y, direction, type, visible));
	}

}
