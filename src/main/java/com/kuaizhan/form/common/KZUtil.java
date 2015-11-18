package com.kuaizhan.form.common;

import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Spark;

import com.kuaizhan.form.conf.Config;

@Slf4j
public class KZUtil {

	public static long getSiteId(Request request) {
		long siteId = -1;
		String idStr = null;
		String debug = Config.get("kuaizhan.debug");

		if ("true".equalsIgnoreCase(debug)) {
			idStr = request.queryParams("site_id");
		} else {
			idStr = request.cookie("kz_site");
		}

		if (idStr == null) {
			Spark.halt(401, "Not authorized\n");
		}

		try {
			siteId = Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			log.warn("Failed to get site id", e);
			Spark.halt(401, "Not authorized\n");
		}

		return siteId;
	}
}
