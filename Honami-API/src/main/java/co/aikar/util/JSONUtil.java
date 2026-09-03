package co.aikar.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings({ "rawtypes", "SuppressionAnnotation" })
public final class JSONUtil {
	private JSONUtil() {
	}

	public static JSONPair pair(String key, Object obj) {
		return new JSONPair(key, obj);
	}

	public static JSONPair pair(long key, Object obj) {
		return new JSONPair(String.valueOf(key), obj);
	}

	public static Map createObject(JSONPair... data) {
		return appendObjectData(new LinkedHashMap(), data);
	}

	public static Map appendObjectData(Map parent, JSONPair... data) {
		for (JSONPair JSONPair : data) {
			parent.put(JSONPair.key, JSONPair.val);
		}
		return parent;
	}

	public static List toArray(Object... data) {
		return Lists.newArrayList(data);
	}

	public static <E> List toArrayMapper(E[] collection, Function<E, Object> mapper) {
		return toArrayMapper(Lists.newArrayList(collection), mapper);
	}

	public static <E> List toArrayMapper(Iterable<E> collection, Function<E, Object> mapper) {
		List array = Lists.newArrayList();
		for (E e : collection) {
			Object object = mapper.apply(e);
			if (object != null) {
				array.add(object);
			}
		}
		return array;
	}

	public static <E> Map toObjectMapper(E[] collection, Function<E, JSONPair> mapper) {
		return toObjectMapper(Lists.newArrayList(collection), mapper);
	}

	public static <E> Map toObjectMapper(Iterable<E> collection, Function<E, JSONPair> mapper) {
		Map object = Maps.newLinkedHashMap();
		for (E e : collection) {
			JSONPair JSONPair = mapper.apply(e);
			if (JSONPair != null) {
				object.put(JSONPair.key, JSONPair.val);
			}
		}
		return object;
	}

	@SuppressWarnings("PublicInnerClass")
	public static class JSONPair {
		final String key;
		final Object val;

		JSONPair(String key, Object val) {
			this.key = key;
			this.val = val;
		}
	}
}
