package com.kuaizhan.form.biz;

import java.io.InputStream;

import com.kuaizhan.form.common.Base62Util;
import com.kuaizhan.form.common.MsgDigestUtil;
import com.kuaizhan.form.dao.SohuCloudClient;

public class SohuCloudService {

	private final static String BUCKET_IMG = "appimg";
	private final static String HOST_IMG = "appimg.bjcnc.img.sohucs.com";
	private final static String CDN_HOST_IMG = "1580846b7e638.cdn.sohucs.com";

	private SohuCloudClient dao = new SohuCloudClient();

	public String uploadPic(InputStream in, long length, String type) {
		String unique = String.format("%s/pic/%s", SohuCloudService.class.getName(),
				Base62Util.encode(System.currentTimeMillis()));
		String key = MsgDigestUtil.md5(unique);
		dao.putObject(BUCKET_IMG, key, in, length, type);
		return key;
	}

	public String getPicURL(String key) {
		return String.format("http://%s/%s", HOST_IMG, key);
	}

	public String getPicCDNURL(String key) {
		return String.format("http://%s/%s", CDN_HOST_IMG, key);
	}

}
