package org.bukkit.map;

import java.awt.Image;

public interface MapCanvas {

	public MapView getMapView();

	public MapCursorCollection getCursors();

	public void setCursors(MapCursorCollection cursors);

	public void setPixel(int x, int y, byte color);

	public byte getPixel(int x, int y);

	public byte getBasePixel(int x, int y);

	public void drawImage(int x, int y, Image image);

	public void drawText(int x, int y, MapFont font, String text);

}
