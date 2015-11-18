package com.kuaizhan.form.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {

	private static final int DefaultCharBufferSize = 8192;

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[DefaultCharBufferSize];
		int bytesRead;
		while ((bytesRead = in.read(buf)) != -1) {
			out.write(buf, 0, bytesRead);
		}
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}

	public static InputStream fromBytes(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}
}
