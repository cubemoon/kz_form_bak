package com.kuaizhan.form.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgDigestUtil {

	final static Logger log = LoggerFactory.getLogger(MsgDigestUtil.class);

	public static String md5(String msg) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			log.error("MD5 not supported", e);
			System.exit(-1);
		}

		return bytes2String(md5.digest(msg.getBytes()));
	}

	private static String bytes2String(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
}
