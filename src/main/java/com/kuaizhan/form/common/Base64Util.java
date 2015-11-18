package com.kuaizhan.form.common;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	public static byte[] decode(byte[] buf) {
		return Base64.decodeBase64(buf);
	}
	
	public static byte[] encode(byte[] buf) {
		return Base64.encodeBase64(buf);
	}
}
