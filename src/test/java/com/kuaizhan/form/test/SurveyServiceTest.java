package com.kuaizhan.form.test;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.kuaizhan.form.biz.SurveyService;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.model.SurveyUtil;
import com.kuaizhan.form.test.util.BasicTest;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;

public class SurveyServiceTest extends BasicTest {
	
	SurveyService surveyService = new SurveyService();
	long siteId = 10000;
	long id=1221132L;

	@Test
	public void testCreate() throws Throwable {
		long result = surveyService.save(new Survey());
		System.out.println(result);
	}
	
	@Test
	public void test_publish(){
		surveyService.publish(siteId, id);
	}

	@Test
	public void testUpdate() {
		Survey result = surveyService.get(siteId, id);
		result.setTitle("sohu");
//		surveyService.update(result);
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		boolean result = surveyService.delete(siteId, id);
		System.out.println(result);
	}

	@Test
	public void testGet() {
		Survey result = surveyService.get(siteId, id);
		System.out.println(result.getTitle());
	}

	@Test
	public void testGetBySiteId() {
		QueryParameters queryParameters = new QueryParameters();
		ListPage<Survey> result = surveyService.getBySiteId(siteId, queryParameters);
		System.out.println(result.getTotalCount());
	}
}
