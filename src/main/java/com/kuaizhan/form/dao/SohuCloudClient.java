package com.kuaizhan.form.dao;

import java.io.InputStream;

import com.sohucs.auth.BasicSohuCSCredentials;
import com.sohucs.services.scs.SohuSCS;
import com.sohucs.services.scs.SohuSCSClient;
import com.sohucs.services.scs.model.ObjectMetadata;

public class SohuCloudClient {

	public void putObject(String bucket, String key, InputStream input, long length, String type) {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(length);
		meta.setContentType(type);

		initSCS();
		scs.putObject(bucket, key, input, meta);
	}

	private void initSCS() {
		if (scs == null) {
			synchronized (SohuCloudClient.class) {
				if (scs == null) {
					BasicSohuCSCredentials cred = new BasicSohuCSCredentials(ACCESS_KEY, SECRET_KEY);
					scs = new SohuSCSClient(cred);
				}
			}
		}
	}

	private static SohuSCS scs;

	private final static String ACCESS_KEY = "skfOAUHThTqy+1rAysDQ0A==";
	private final static String SECRET_KEY = "/5an46aCoKT03Q+gatsxVg==";

}
