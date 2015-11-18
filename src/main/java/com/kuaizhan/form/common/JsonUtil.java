package com.kuaizhan.form.common;

import com.google.gson.Gson;

public class JsonUtil {

	private final static Gson gson = new Gson();

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> type) {
		return gson.fromJson(json, type);
	}

	private static class CodeMsg {
		@SuppressWarnings("unused")
		int code;

		@SuppressWarnings("unused")
		String msg;

		CodeMsg(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
	}

	public static String errJson(int code, String msg) {
		return JsonUtil.toJson(new CodeMsg(code, msg));
	}
}
