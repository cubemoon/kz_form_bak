package com.kuaizhan.form.test;


import org.junit.Test;

import com.kuaizhan.form.biz.AnswerService;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;

public class AnswerServiceTest {
	
	AnswerService answerService = new AnswerService();
	long surveyId = 1000;
	long siteId = 1000;


	@Test
	public void testCreate() {
//		String result = answerService.create(surveyId);
//		System.out.println(result);
		
	}

	@Test
	public void testUpdate() {
		answerService.update();
	}

	@Test
	public void testDelete() {
		boolean result = answerService.delete(siteId, 1000);
		System.out.println(result);
	}

	@Test
	public void testGet() {
		Answer result = answerService.get(siteId, 1000);
//		System.out.println(result.toMap().toString());
	}

	@Test
	public void testGetBySurveyId() {
		QueryParameters queryParameters = new QueryParameters();
		ListPage<Answer> result = answerService.getBySurveyId(siteId, surveyId, queryParameters);
		System.out.println(result.getTotalCount());
	}

}
