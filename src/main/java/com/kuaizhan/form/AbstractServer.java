package com.kuaizhan.form;

import java.io.File;
import java.io.FileNotFoundException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import spark.ResponseTransformer;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import com.kuaizhan.form.common.FileUtil;
import com.kuaizhan.form.conf.Config;
import com.kuaizhan.form.server.JacksonTransformer;
import com.kuaizhan.form.server.KZFreeMarkerEngine;

@Slf4j
public abstract class AbstractServer {

	@Getter
	protected static String ipAddress;
	
	@Getter
	protected static int port;

	protected final static String STATIC_DIR = "webapp/static";
	protected final static String TEMPLATE_DIR = "webapp/templates";

	protected static FreeMarkerEngine tmplEngine;
	protected static ResponseTransformer jsonTransformer;

	protected static void configStaticFileDir() throws FileNotFoundException {
		File staticDir = FileUtil.getResourceFile(STATIC_DIR);
		Spark.externalStaticFileLocation(staticDir.getPath());
		log.info(String.format("Static File: %s", staticDir));
	}

	protected static void configTemplateEngine() {
//		if ("true".equalsIgnoreCase(Config.get("kuaizhan.debug"))) {
//			tmplEngine = new KZFreeMarkerSegmentEngine(TEMPLATE_DIR);
//		} else {
			tmplEngine = new KZFreeMarkerEngine(TEMPLATE_DIR);
//		}
	}

	protected static void configJsonTransformer() {
		jsonTransformer = new JacksonTransformer();
	}

	protected static void configHTTPServer() {
		AbstractServer.ipAddress = "0.0.0.0";
		AbstractServer.port = Integer.parseInt(Config.get("bindport", "8086"));

		Spark.ipAddress(AbstractServer.ipAddress);
		Spark.port(AbstractServer.port);

	}

	protected static void configure() throws FileNotFoundException {
		configStaticFileDir();

		configTemplateEngine();
		configJsonTransformer();

		configHTTPServer();
	}
	

	public static void init() {
		Config.class.getName();
	}
	

	protected static final String STRONG_FMT = "*************** %s ***************";
}
