/*
 * 创建日期 2005-12-13
 *
 */
package com.kuaizhan.form.common;

import java.io.UnsupportedEncodingException;

import spark.Request;

/**
 * 
 * HTTP requst参数读取辅助类 实现各种常用数据类型的参数转换及空值处理
 */

public class RequestUtils {

	private static String NULLSTRING = "";

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final String TARGET_ENCODING = "UTF-8";

	/**
	 * 对HTTP接收的参数进行编码转换
	 * 
	 * @param request
	 * @param name
	 * @param encoding
	 * @param defautlValue
	 * @return
	 */
	public static String getEncodedParameter(Request request,
			String name, String encoding, String defautlValue) {
		String temp = request.queryParams(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defautlValue;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			temp = new String(temp.getBytes(DEFAULT_ENCODING), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return defautlValue;
		}
		return temp;
	}

	public static String getEncodedParameter(Request request,
			String name, String encoding) {
		return getEncodedParameter(request, name, encoding, null);
	}

	/**
	 * 取得HTTP参数，值为空字符串时返加null
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String queryParams(Request request, String name) {
		return getEncodedParameter(request, name, TARGET_ENCODING, null);
	}

	/**
	 * 取得HTTP参数，值为空字符串或null时返回默认值
	 * 
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String queryParams(Request request, String name,
			String defaultValue) {
		return getEncodedParameter(request, name, TARGET_ENCODING, defaultValue);
	}

	/**
	 * 对HTTP接收的参数数组进行编码转换
	 * 
	 * @param request
	 * @param name
	 * @param encoding
	 * @return
	 */
	public static String[] getEncodedParameters(Request request,
			String name, String encoding) {

		String[] temp = null;//FIXME
		if (temp == null) {
			return null;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
					temp[i] = new String(temp[i].getBytes(DEFAULT_ENCODING),
							encoding);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 对HTTP接收的参数数组进行编码转换
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String[] queryParamss(Request request, String name) {
		return getEncodedParameters(request, name, TARGET_ENCODING);
	}

	/**
	 * 值为"trur"或'y"时返回true，否则返回false
	 * 
	 * @param request
	 * @param name
	 * @param defaultVal
	 * @return
	 */
	public static boolean getBooleanParameter(Request request,
			String name, boolean defaultVal) {
		String temp = request.queryParams(name);
		if ("true".equalsIgnoreCase(temp) || "y".equalsIgnoreCase(temp)) {
			return true;
		} else if ("false".equalsIgnoreCase(temp) || "n".equalsIgnoreCase(temp)) {
			return false;
		} else {
			return defaultVal;
		}
	}

	/**
	 * 把取得的参数传化为int类型
	 * 
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static int getIntParameter(Request request, String name,
			int defaultNum) {
		String temp = request.queryParams(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Integer.parseInt(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

	/**
	 * 把取得的参数传化为double类型
	 * 
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static double getDoubleParameter(Request request, String name, double defaultNum) {
		String temp = request.queryParams(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Double.parseDouble(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

	/**
	 * 把取得的参数传化为long类型
	 * 
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static long getLongParameter(Request request, String name, long defaultNum) {
		String temp = request.queryParams(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Long.parseLong(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

}