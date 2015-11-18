package com.kuaizhan.form.biz;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuaizhan.form.common.HttpUtil;
import com.kuaizhan.form.common.HttpUtil.HTTPResponse;
import com.kuaizhan.form.common.JsonUtil;
import com.kuaizhan.form.model.KZSite;

public class KZService {

	final static Logger log = LoggerFactory.getLogger(KZService.class);

	private final static String SITE_INFO = "http://www.kuaizhan-service.com/site/service-site-info";
	//private final static String P_URL = "http://www.kuaizhan-service.com/pageui/service-get-url";
	//private final static String SITE_INFO = "http://www.t1.com/site/service-site-info";
	private final static String P_URL = "http://www.t1.com/pageui/service-get-url";

	/**
	 * Get domain name of a site.
	 * 
	 * @param siteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public KZSite getDomainBySiteId(long siteId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("site_id", String.valueOf(siteId));
		HTTPResponse resp = HttpUtil.get(SITE_INFO, params);
		if (resp == null || resp.getCode() >= 400) {
			return null;
		}

		Map<String, Object> result = JsonUtil.fromJson(resp.getBody(), Map.class);

		// FIXME
		int ret = (int) (double) result.get("ret");

		if (ret != 0) {
			log.warn((String) result.get("msg"));
			return null;
		}

		Map<String, String> data = (Map<String, String>) result.get("data");
		KZSite site = new KZSite();
		site.setSiteId(siteId);
		site.setStatus(Integer.parseInt(data.get("status")));
		site.setDomain((String) data.get("domain"));

		return site;
	}

	public String getPageURL(long siteId, long pageId) {
		return getIdURL(siteId, pageId);
	}

	public String getPostURL(long siteId, long postData) {
		return getIdURL(siteId, postData);
	}

	@SuppressWarnings("unchecked")
	private String getIdURL(long siteId, long id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("site_id", String.valueOf(siteId));
		params.put("id", String.valueOf(id));
		HTTPResponse resp = HttpUtil.get(P_URL, params);
		if (resp == null || resp.getCode() >= 400) {
			return null;
		}
		Map<String, Object> result = JsonUtil.fromJson(resp.getBody(), Map.class);
		return ((Map<String, String>) result.get("data")).get("url");
	}

}
