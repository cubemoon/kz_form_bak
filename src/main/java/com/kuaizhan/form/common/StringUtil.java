package com.kuaizhan.form.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

	private static final int DefaultCharBufferSize = 8192;
	
	public static boolean isNotEmpty(String s) {
		return !s.isEmpty();
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static int size(String s) {
		return s == null ? 0 : s.length();
	}

	public static String toStringOrNull(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	public static String toStringOrEmpty(Object obj) {
		if (obj == null) {
			return null;
		}
		String s = obj.toString();
		return s == null ? "" : s;
	}

	public static String join(String s, String[] strings) {
		if (strings == null || strings.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(strings[0]);
		for (int i = 1; i < strings.length; ++i) {
			sb.append(s);
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	public static String join(String s, List<String> strings) {
		if (strings == null || strings.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = strings.iterator();
		sb.append(iter.next());
		while (iter.hasNext()) {
			sb.append(s);
			sb.append(iter.next());
		}
		return sb.toString();
	}

	public static String fromInputStream(InputStream in) throws IOException {
		return fromInputStream(in, null);
	}

	public static String fromInputStream(InputStream in, String charset) throws IOException {
		Reader r = null;
		if (charset != null) {
			r = new BufferedReader(new InputStreamReader(in, charset));
		} else {
			r = new BufferedReader(new InputStreamReader(in));
		}

		StringBuffer sb = new StringBuffer();
		char[] buf = new char[DefaultCharBufferSize];
		int charsRead;
		while ((charsRead = r.read(buf)) != -1) {
			sb.append(buf, 0, charsRead);
		}

		return sb.toString();
	}

	public static String readableSize(long size) {
		if (size < 1024L) {
			return String.format("%dB", size);
		}
		if (size < 1024L * 1024) {
			return String.format("%.2fKB", size / 1024.0);
		}
		if (size < 1024L * 1024 * 1024) {
			return String.format("%.2fMB", size / (1024.0 * 1024));
		}
		return ">1.0GB";
	}

}
