package com.kuaizhan.form.common;

import java.util.Map;

public class MapUtil {

	public static <K, T> boolean isEmpty(Map<K, T> map) {
		return map == null || map.isEmpty();
	}

	public static <K, T> T get(Map<K, T> map, K key, T defaultValue) {
		T value = map.get(key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
	
}
