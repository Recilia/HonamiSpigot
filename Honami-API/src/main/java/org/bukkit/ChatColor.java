package org.bukkit;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

import com.google.common.collect.Maps;

public enum ChatColor {

	BLACK('0', 0x00) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.BLACK;
		}
	},

	DARK_BLUE('1', 0x1) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.DARK_BLUE;
		}
	},

	DARK_GREEN('2', 0x2) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.DARK_GREEN;
		}
	},

	DARK_AQUA('3', 0x3) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.DARK_AQUA;
		}
	},

	DARK_RED('4', 0x4) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.DARK_RED;
		}
	},

	DARK_PURPLE('5', 0x5) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.DARK_PURPLE;
		}
	},

	GOLD('6', 0x6) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.GOLD;
		}
	},

	GRAY('7', 0x7) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.GRAY;
		}
	},

	DARK_GRAY('8', 0x8) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.DARK_GRAY;
		}
	},

	BLUE('9', 0x9) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.BLUE;
		}
	},

	GREEN('a', 0xA) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.GREEN;
		}
	},

	AQUA('b', 0xB) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.AQUA;
		}
	},

	RED('c', 0xC) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.RED;
		}
	},

	LIGHT_PURPLE('d', 0xD) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;
		}
	},

	YELLOW('e', 0xE) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.YELLOW;
		}
	},

	WHITE('f', 0xF) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.WHITE;
		}
	},

	MAGIC('k', 0x10, true) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.MAGIC;
		}
	},

	BOLD('l', 0x11, true) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.BOLD;
		}
	},

	STRIKETHROUGH('m', 0x12, true) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.STRIKETHROUGH;
		}
	},

	UNDERLINE('n', 0x13, true) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.UNDERLINE;
		}
	},

	ITALIC('o', 0x14, true) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.ITALIC;
		}
	},

	RESET('r', 0x15) {
		@Override
		public net.md_5.bungee.api.ChatColor asBungee() {
			return net.md_5.bungee.api.ChatColor.RESET;
		}
	};

	public static final char COLOR_CHAR = '\u00A7';
	private static final Pattern STRIP_COLOR_PATTERN = Pattern
			.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

	private final int intCode;
	private final char code;
	private final boolean isFormat;
	private final String toString;
	private final static Map<Integer, ChatColor> BY_ID = Maps.newHashMap();
	private final static Map<Character, ChatColor> BY_CHAR = Maps.newHashMap();

	private ChatColor(char code, int intCode) {
		this(code, intCode, false);
	}

	private ChatColor(char code, int intCode, boolean isFormat) {
		this.code = code;
		this.intCode = intCode;
		this.isFormat = isFormat;
		this.toString = new String(new char[] { COLOR_CHAR, code });
	}

	public net.md_5.bungee.api.ChatColor asBungee() {
		return net.md_5.bungee.api.ChatColor.RESET;
	};

	public char getChar() {
		return code;
	}

	@Override
	public String toString() {
		return toString;
	}

	public boolean isFormat() {
		return isFormat;
	}

	public boolean isColor() {
		return !isFormat && this != RESET;
	}

	public static ChatColor getByChar(char code) {
		return BY_CHAR.get(code);
	}

	public static ChatColor getByChar(String code) {
		Validate.notNull(code, "Code cannot be null");
		Validate.isTrue(code.length() > 0, "Code must have at least one char");

		return BY_CHAR.get(code.charAt(0));
	}

	public static String stripColor(final String input) {
		if (input == null) {
			return null;
		}

		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
				b[i] = ChatColor.COLOR_CHAR;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}

	public static String getLastColors(String input) {
		String result = "";
		int length = input.length();

		for (int index = length - 1; index > -1; index--) {
			char section = input.charAt(index);
			if (section == COLOR_CHAR && index < length - 1) {
				char c = input.charAt(index + 1);
				ChatColor color = getByChar(c);

				if (color != null) {
					result = color.toString() + result;

					if (color.isColor() || color.equals(RESET)) {
						break;
					}
				}
			}
		}

		return result;
	}

	static {
		for (ChatColor color : values()) {
			BY_ID.put(color.intCode, color);
			BY_CHAR.put(color.code, color);
		}
	}
}
