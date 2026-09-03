package rein.honami.spigot.util;

public class Utils {
	public static String repeat(String string, int count) {
		return new String(new char[count]).replace("\0", string);
	}
}
