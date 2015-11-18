package com.kuaizhan.form.test.util;

import lombok.extern.slf4j.Slf4j;

import org.junit.BeforeClass;

import com.kuaizhan.form.common.ResourceUtils;

@Slf4j
public class BasicTest {

	protected  static String surveyJson = null;
	protected  static String answerJson = null;

	static{
		// Enable MongoDB logging in general
		System.setProperty("DEBUG.MONGO", "true");
		// Enable DB operation tracing
		System.setProperty("DB.TRACE", "true");
	}
	
	@BeforeClass
	public static void loadJsonFiles(){
		surveyJson = ResourceUtils.loadJsonFile("survey.json");
		answerJson = ResourceUtils.loadJsonFile("answer.json");		
//		log.debug(surveyPostJson);
//		log.debug(answerJson);
	}
}
