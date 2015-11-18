package com.kuaizhan.form.common;

public class BytesUtil {

	public static int indexOf(byte[] array, byte c) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == c) {
				return i;
			}
		}
		return -1;
	}

	public static byte[] subBytes(byte[] array, int from, int to) {
		if (from < 0 || to < 0 || to > array.length || from >= to) {
			throw new IllegalArgumentException("Invalid from, to");
		}

		byte[] bytes = new byte[to - from];
		for (int c = 0, i = from; i < to; ++c, ++i) {
			bytes[c] = array[i];
		}
		return bytes;
	}

	public static byte[] subBytes(byte[] array, int from) {
		return subBytes(array, from, array.length);
	}

}
