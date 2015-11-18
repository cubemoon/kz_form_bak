package com.kuaizhan.form.server;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.template.freemarker.FreeMarkerEngine;

import com.kuaizhan.form.common.FileUtil;

import freemarker.template.Configuration;

public class KZFreeMarkerEngine extends FreeMarkerEngine {

	private final static Logger log = LoggerFactory.getLogger(KZFreeMarkerEngine.class);

	public KZFreeMarkerEngine(String templateDir) {
		Configuration conf = new Configuration();
		try {
			File tempDir = FileUtil.getResourceFile(templateDir);
			conf.setDirectoryForTemplateLoading(tempDir);
			log.info(String.format("Template File: %s", tempDir));
		} catch (IOException e) {
			log.error("set directory for template loading failed!", e);
		}

		this.setConfiguration(conf);
	}

}
