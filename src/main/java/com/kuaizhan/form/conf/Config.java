package com.kuaizhan.form.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuaizhan.form.common.FTLUtil;
import com.kuaizhan.form.common.StringUtil;

public class Config {

	static final Logger log = LoggerFactory.getLogger(Config.class);

	private static final String PROP_NAME = "conf.properties";
	private static final Properties PROP = new Properties();

	static {
		ClassLoader cl = Config.class.getClassLoader();
		if (cl == null) {
			log.error(String.format("ClassLoader of %s should not be null!", Config.class.getName()));
			System.exit(-1);
		}

		String path = cl.getResource(PROP_NAME).getPath();
		try {
			System.out.println(StringUtil.fromInputStream(cl.getResourceAsStream(PROP_NAME)));
			PROP.load(cl.getResourceAsStream(PROP_NAME));
		} catch (IOException e) {
			log.error(String.format("Load config file %s failed!", path), e);
			System.exit(-1);
		}
	}

	/**
	 * Get property first from System.properties and then conf.properties.
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String value = System.getProperty(key);
		if (value == null) {
			value = PROP.getProperty(key);
		}
		return value;
	}

	/**
	 * Like get(String key), but a default value is used if no property is found.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String get(String key, String defaultValue) {
		String value = System.getProperty(key);
		if (value == null) {
			value = PROP.getProperty(key, defaultValue);
		}
		return value;
	}

	/**
	 * Generate URL for domains like kuaizhan.com, t1.com, t2.com,
	 * which is read from the "domain" property.
	 * 
	 * @author shengzhong
	 */
	public static class V {

		public static String _(String path) {
			if (!path.startsWith("/")) {
				throw new IllegalArgumentException("path must be absolute");
			}
			return String.format("http://%s%s", get("domain"), path);
		}

		public static String www(String path) {
			if (!path.startsWith("/")) {
				throw new IllegalArgumentException("path must be absolute");
			}
			return String.format("http://www.%s%s", get("domain"), path);
		}

		public static String s1(String path) {
			if (!path.startsWith("/")) {
				throw new IllegalArgumentException("path must be absolute");
			}
			return String.format("http://s1.%s%s", get("domain"), path);
		}

		public static String s0(String path) {
			if (!path.startsWith("/")) {
				throw new IllegalArgumentException("path must be absolute");
			}
			return String.format("http://s0.%s%s", get("domain"), path);
		}

		public static String _() {
			return String.format("http://%s%s", get("domain"));
		}

		public static String www() {
			return String.format("http://www.%s%s", get("domain"));
		}

		public static String s1() {
			return String.format("http://s1.%s%s", get("domain"));
		}

		public static String s0() {
			return String.format("http://s0.%s", get("domain"));
		}

	}

	/**
	 * Get a model which has class V inside.
	 * 
	 * @return a model of type Map
	 */
	public static Map<String, Object> getVersionedModel() {
		Map<String, Object> model = new HashMap<String, Object>();
		FTLUtil.addClass("V", V.class.getName(), model);
		return model;
	}

}
