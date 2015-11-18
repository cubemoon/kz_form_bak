package com.kuaizhan.form.dao;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import com.kuaizhan.form.common.IdUtil;
import com.kuaizhan.form.common.JacksonUtils;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.test.util.BasicTest;
import com.kuaizhan.form.view.QueryParameters;

@Slf4j
public class AnswerDaoTest extends BasicTest {
	
	AnswerDao answerDao = new AnswerDao();
	
	long siteId = 10000;
	
	long surveyId = 1000;


	@Test
	public void testGetBySurveyId() {
		fail("Not yet implemented");
	}

	@Test
	public void testCountByDate() throws Throwable {
		Answer answer = JacksonUtils.toBean(answerJson, Answer.class);
		answer.setId(IdUtil.nextId());
		answer.setSiteId(siteId);
		answer.setSurveyId(IdUtil.nextId());
		answer.setNo(1);
		answer.setCreatedAt(new Date().getTime());
		answer.setCreatedDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
		System.out.println("*************** befroe save **************************");
		System.out.println(answer);

		answerDao.save(answer);
		System.out.println(answer);
		
		Object result = answerDao.countByDate(siteId, answer.getSurveyId());
		System.out.println(result);
	}
	
	@Test
	public void testCountByDate2() throws Throwable {
		Answer answer = JacksonUtils.toBean(answerJson, Answer.class);
		answer.setId(IdUtil.nextId());
		answer.setSiteId(siteId);
		answer.setSurveyId(IdUtil.nextId());
		answer.setNo(1);
		answer.setCreatedAt(new Date().getTime());
		answer.setCreatedDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
		System.out.println("*************** befroe save **************************");
		System.out.println(answer);

		answerDao.save(answer);
		System.out.println(answer);
		
		Object result = answerDao.countByDate2(siteId, answer.getSurveyId());
		System.out.println(result);
	}
	
	@Test
	public void test_fetchFieldValues(){
		String fieldId = "com20";
		QueryParameters params = new QueryParameters();
		Object result = answerDao.fetchFieldValues(siteId, surveyId, fieldId, params);
		System.out.println(result);
	}

	@Test
	public void test_fetchFieldValues2(){
		String fieldId = "com20";
		Object result = answerDao.fetchFieldValues2(siteId, surveyId, fieldId);
		System.out.println(result);
	}
	
	@Test
	public void test_statSurvey(){
		long surveyId = 1000L;
 		Object result = answerDao.statSurvey(surveyId);
 		System.out.println(result);
	}
	
	
	
	@Test
	public void testSaveT() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented");
	}

}
