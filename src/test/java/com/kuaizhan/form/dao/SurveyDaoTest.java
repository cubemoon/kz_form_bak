package com.kuaizhan.form.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import com.github.jmkgreen.morphia.annotations.Id;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.model.SurveyUtil;
import com.kuaizhan.form.test.util.BasicTest;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;

public class SurveyDaoTest extends BasicTest{

	SurveyDao surveyDao = new SurveyDao();
	
	long surveyId = 1000;
	long siteId = 7539545611L;
	long id = 10000;
	
	public Survey getSurvey(long id){
		Survey survey = new Survey();
		survey.setId(id);
		survey.setSiteId(siteId);
		survey.setTitle("客户满意度调查");
		survey.setSubtitle("为了提升我们的服务质量，请您提出宝贵的建议与意见！");
		survey.setCreatedAt(new Date().getTime());
		return survey;
	}

	
	@Test
	public void testCreate() {
		Survey survey = getSurvey(surveyId);
		long surveyId2 = (long)surveyDao.save(survey).getId();
		System.out.println(surveyId2);
	}
	
	@Test
	public void testCreate2() {
		Survey survey = SurveyUtil.parseSurvey(surveyJson);
		System.out.println("survey id from json " + survey.getId());
		survey.setId(id);
		survey.setSiteId(siteId);
		
		long surveyId2 = (long)surveyDao.save(survey).getId();
		System.out.println(surveyId2);
	}



	@Test
	public void testDelete() {
		Survey survey = getSurvey(123456);
		surveyDao.save(survey);
		boolean result = surveyDao.deleteById(siteId, surveyId);
		assertTrue(result);
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		Survey survey2 = surveyDao.get(surveyId);
		System.out.println(survey2);
		assertTrue(survey2 != null);
		assertTrue(surveyId == survey2.getId());
	}
	
	@Test
	public void testGet2(){
		Survey survey = surveyDao.get(10000, id);
		assertTrue(survey!=null);
		System.out.println(survey);
	}

	@Test
	public void testGetBySiteId() {
		QueryParameters queryParameters = new QueryParameters();
		ListPage<Survey> surveys = surveyDao.getBySiteId(siteId, queryParameters);
		System.out.println(surveys);
		assertTrue(surveys != null && surveys.getTotalCount()>0);
	}

}
