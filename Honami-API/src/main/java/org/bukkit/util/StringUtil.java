package org.bukkit.util;

import java.util.Collection;

import org.apache.commons.lang.Validate;

public class StringUtil {

	public static <T extends Collection<? super String>> T copyPartialMatches(final String token,
			final Iterable<String> originals, final T collection)
			throws UnsupportedOperationException, IllegalArgumentException {
		Validate.notNull(token, "Search token cannot be null");
		Validate.notNull(collection, "Collection cannot be null");
		Validate.notNull(originals, "Originals cannot be null");

		for (String string : originals) {
			if (startsWithIgnoreCase(string, token)) {
				collection.add(string);
			}
		}

		return collection;
	}

	public static boolean startsWithIgnoreCase(final String string, final String prefix)
			throws IllegalArgumentException, NullPointerException {
		Validate.notNull(string, "Cannot check a null string for a match");
		if (string.length() < prefix.length()) {
			return false;
		}
		return string.regionMatches(true, 0, prefix, 0, prefix.length());
	}
}
