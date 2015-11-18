package com.kuaizhan.form.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.kuaizhan.form.dao.SurveyStatDao;
import com.kuaizhan.form.model.SurveyStat;
import com.kuaizhan.form.test.util.BasicTest;

public class SurveyStatDaoTest extends BasicTest{
	
	SurveyStatDao surveyStatDao = new SurveyStatDao();
	
	@Test
	public void testIncreaseVisitCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncreaseAnswerCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAnswerOrder() {		
		long id = 10000;
		
		SurveyStat surveyStat = new SurveyStat();
		surveyStat.setId(id);
		surveyStat.setTitle("客户满意度调查");
		surveyStatDao.save(surveyStat);
		
		int delta = 2;
		surveyStatDao.incrVisitCount(id, delta);
		surveyStatDao.incrAnswerCount(id, delta);
		
		SurveyStat surveystat2 = surveyStatDao.get(id);
		System.out.println(surveystat2);
		
		int answerOrder = surveyStatDao.getAnswerOrder(id);
		System.out.println("answerOrder"+ answerOrder);
		answerOrder = surveyStatDao.getAnswerOrder(id);
		System.out.println("answerOrder"+ answerOrder);
	}
	
	@Test
	public void test_incrFieldSelectedCount(){
		long id = 10000;
		String fieldId = "com26";
		String valueName = "商务";
		int valueIndex = 1;
		int delta = 1;
		surveyStatDao.incrFieldSelectedCount(id, fieldId, valueName, valueIndex, delta);
	}
	
	@Test
	public void test_incrFieldAnswerCount(){
		long id = 10000;
		String fieldId = "com25";
		int delta = 1;
		surveyStatDao.incrFieldAnswerCount(id, fieldId, delta);
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		fail("Not yet implemented");
	}

}
