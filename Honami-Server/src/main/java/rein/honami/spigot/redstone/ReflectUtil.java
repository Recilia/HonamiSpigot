package rein.honami.spigot.redstone;

import java.lang.reflect.Field;

public class ReflectUtil {

	public static <T> T getOfT(Object obj, Class<T> type) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (type.equals(field.getType())) {
				return get(obj, field, type);
			}
		}

		return null;
	}

	public static <T> T get(Object obj, Field field, Class<T> type) {
		try {
			field.setAccessible(true);
			return type.cast(field.get(obj));
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}