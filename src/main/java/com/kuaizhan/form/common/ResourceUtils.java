package com.kuaizhan.form.common;


import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import spark.utils.IOUtils;


@Slf4j
public class ResourceUtils {

	public static String loadJsonFile(String filename){
		try {
			InputStream is  = ResourceUtils.class.getClassLoader().getResourceAsStream(filename);
			String json = IOUtils.toString(is);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return null;
		}
	}
}
