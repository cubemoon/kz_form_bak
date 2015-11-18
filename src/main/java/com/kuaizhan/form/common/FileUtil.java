package com.kuaizhan.form.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	public static File getResourceFile(String path) throws FileNotFoundException {
		ClassLoader cl = FileUtil.class.getClassLoader();
		URL root = cl.getResource("");
		if (root != null) {
			/* There is a directory in CLASSPATH. */
			File dir = new File(root.getFile());

			/* 1. Find `path` in CLASSPATH */
			File target = new File(dir, path);
			if (target.exists()) {
				return target;
			}

			/* 2. Find `path` in CLASSPATH/.. */
			target = new File(dir.getParentFile(), path);
			if (target.exists()) {
				return target;
			}

			/* Not found. */
			log.error(String.format("No %s in/with CLASSPATH", path));
			throw new FileNotFoundException();

		} else {
			/* Only jars exist in CLASSPATH. */
			log.error("Run with -cp option and ensure a DIR in it.");
			throw new FileNotFoundException();
		}
	}
}
