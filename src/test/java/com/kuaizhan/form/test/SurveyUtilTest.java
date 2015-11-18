package com.kuaizhan.form.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kuaizhan.form.controller.SurveysHandler;
import com.kuaizhan.form.controller.SurveysHandlerTest;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.model.SurveyUtil;

public class SurveyUtilTest {
	
	private static String surveyPostJson = null;
	
	SurveysHandler surveysHandler = new SurveysHandler();

	@BeforeClass
	public static void loadJsonFiles(){
		try {
			surveyPostJson = FileUtils.readFileToString(new File(SurveysHandlerTest.class.getClassLoader().getResource("survey.json").getPath()), "UTF-8");
			System.out.println(String.format("survey json = \n%s\n", surveyPostJson));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_parseSurvey(){
		Survey survey = SurveyUtil.parseSurvey(surveyPostJson);
		System.out.println(survey);
	}
	
}
