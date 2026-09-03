package org.bukkit.map;

import java.util.HashMap;

public class MapFont {

	private final HashMap<Character, CharacterSprite> chars = new HashMap<Character, CharacterSprite>();
	private int height = 0;
	protected boolean malleable = true;

	public void setChar(char ch, CharacterSprite sprite) {
		if (!malleable) {
			throw new IllegalStateException("this font is not malleable");
		}

		chars.put(ch, sprite);
		if (sprite.getHeight() > height) {
			height = sprite.getHeight();
		}
	}

	public CharacterSprite getChar(char ch) {
		return chars.get(ch);
	}

	public int getWidth(String text) {
		if (!isValid(text)) {
			throw new IllegalArgumentException("text contains invalid characters");
		}

		if (text.length() == 0) {
			return 0;
		}

		int result = 0;
		for (int i = 0; i < text.length(); ++i) {
			result += chars.get(text.charAt(i)).getWidth();
		}
		result += text.length() - 1; 

		return result;
	}

	public int getHeight() {
		return height;
	}

	public boolean isValid(String text) {
		for (int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			if (ch == '\u00A7' || ch == '\n')
				continue;
			if (chars.get(ch) == null)
				return false;
		}
		return true;
	}

	public static class CharacterSprite {

		private final int width;
		private final int height;
		private final boolean[] data;

		public CharacterSprite(int width, int height, boolean[] data) {
			this.width = width;
			this.height = height;
			this.data = data;

			if (data.length != width * height) {
				throw new IllegalArgumentException("size of data does not match dimensions");
			}
		}

		public boolean get(int row, int col) {
			if (row < 0 || col < 0 || row >= height || col >= width)
				return false;
			return data[row * width + col];
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}

}
