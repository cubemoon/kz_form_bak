package com.kuaizhan.form.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	final static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	private static final String UTF8_ENC = "UTF-8";

	public static HTTPResponse get(String url, Map<String, String> params) {
		HttpClient client = HttpClients.createDefault();
		StringBuilder uri = new StringBuilder(url);
		String paramStr = buildUrlQueryString(params);
		if (!StringUtil.isEmpty(paramStr)) {
			uri.append('?').append(paramStr);
		}
		HttpGet get = new HttpGet(uri.toString());
		HTTPResponse resp = null;
		try {
			resp = new HTTPResponse(client.execute(get));
		} catch (IOException e) {
			log.error(String.format("Get %s error", uri.toString()), e);
		}

		return resp;
	}

	public static HTTPResponse post(String url, Map<String, String> params) {
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> args = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			args.add(new BasicNameValuePair(key, params.get(key)));
		}
		HTTPResponse resp = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(args, UTF8_ENC));
			resp = new HTTPResponse(client.execute(post));
		} catch (IOException e) {
			log.error(String.format("Post %s error", url), e);
		}
		return resp;
	}

	public static HTTPResponse post(String url, String content) {
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		HTTPResponse resp = null;
		try {
			post.setEntity(new StringEntity(content, UTF8_ENC));
			resp = new HTTPResponse(client.execute(post));
		} catch (IOException e) {
			log.error(String.format("Post %s error", url), e);
		}
		return resp;
	}

	private static String buildUrlQueryString(Map<String, String> params) {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				paramList.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		return URLEncodedUtils.format(paramList, UTF8_ENC);
	}

	public static class HTTPResponse {
		private int code;
		private String body;

		HTTPResponse(HttpResponse resp) throws IOException {
			code = resp.getStatusLine().getStatusCode();
			body = StringUtil.fromInputStream(resp.getEntity().getContent());
		}

		public int getCode() {
			return code;
		}

		public String getBody() {
			return body;
		}
	}

}
