package com.kuaizhan.form.controller;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import spark.Route;
import spark.Spark;

import com.google.common.collect.Maps;
import com.kuaizhan.form.biz.SurveyService;
import com.kuaizhan.form.common.JacksonUtils;
import com.kuaizhan.form.common.KZUtil;
import com.kuaizhan.form.common.RequestUtils;
import com.kuaizhan.form.dao.AnswerDao;
import com.kuaizhan.form.dao.SurveyStatDao;
import com.kuaizhan.form.dao.VisitDao;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.model.SurveyUtil;
import com.kuaizhan.form.model.Visit;
import com.kuaizhan.form.view.CodeMsg;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;
import com.kuaizhan.form.view.SurveyView;

@Slf4j
public class SurveysHandler extends BaseHandler {

	private SurveyService surveyService = new SurveyService();
	
	
	/** 访问表单 **/
	public Route get = (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		Survey survey = surveyService.getSimple(siteId, id);
		
		if (survey == null) {
			return CodeMsg.failure(404, "survey not found");
		} else {
			SurveyView surveyView = SurveyView.fromModel(survey);
			return CodeMsg.success().data(surveyView);
		}

	};


	public Route listSurveys = (request, response) -> {

		long siteId = KZUtil.getSiteId(request);
		int pageNo = RequestUtils.getIntParameter(request, "page_no", 1) ;
		int pageSize = RequestUtils.getIntParameter(request, "page_size", 10) ;
		
		QueryParameters queryParams = new QueryParameters();
		queryParams.setPageNo(pageNo);
		queryParams.setPageSize(pageSize);

		ListPage<Survey> surveys = surveyService.getBySiteId(siteId, queryParams);
		ListPage<SurveyView> listPage = SurveyView.fromModelList(surveys);
		return CodeMsg.success().data(listPage);
	};

	/**
	 * create survey
	 */
	public Route post = (request, response) -> {
		String dataJson = request.body();
		log.info("body = \n" + dataJson);
		
		long siteId = KZUtil.getSiteId(request);
		Survey survey = null;
		
		try {
			survey = JacksonUtils.toBean(dataJson, Survey.class);
			survey.setSiteId(siteId);
			log.info("survey" + survey);
			
			survey = SurveyUtil.validateSurvey(survey);
			long surveyId = surveyService.save(survey);
			
			Map<String, Object> data = Maps.newHashMap();
			data.put("id", String.valueOf(surveyId));
			data.put("title", survey.getTitle());
			return CodeMsg.success().data(data);
		} catch (Exception e1) {
			log.error(String.format("parse survey json error %s", e1.getMessage()), e1);
			return CodeMsg.failure(400, "表单数据格式错误," + e1.getMessage());
		}
	};

	/**
	 * update survey
	 */
	public Route publish = (request, response) -> {
		String dataJson = request.body();
		log.info("body = \n" + dataJson);
		
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		try{
			Survey survey = null;
			survey = JacksonUtils.toBean(dataJson, Survey.class);
			survey.setSiteId(siteId);

			if(id != 0){
				survey.setId(id);
			}
			log.info("survey" + survey);
			id = surveyService.save(survey);
			
			boolean result = surveyService.publish(siteId, id);
			if(result){
				Map<String, Object> data = Maps.newHashMap();
				data.put("id", String.valueOf(id));
				data.put("title", survey.getTitle());
				return CodeMsg.success().data(data);
			}else{
				return CodeMsg.failure("表单发布失败。");
			}
		}catch(Exception e){
			return CodeMsg.failure(e.getMessage());
		}
	};

	public Route delete = (request, response) -> {
		long siteId = KZUtil.getSiteId(request);

		String idStr = request.params(":id");		
		if (idStr.isEmpty()) {
			Spark.halt(400, "Bad Request");
		}
		try {
			long id = Long.parseLong(idStr);
			log.info(String.format("surveyService.delete(siteId=%d, id=%d)", siteId, id));
			boolean result = surveyService.delete(siteId, id);
			return CodeMsg.parse(result, "删除表单失败");
		} catch (NumberFormatException e) {
			return CodeMsg.parse(false, "删除表单失败," + e.getMessage());
		}
	};


	
	private AnswerDao answerDao = new AnswerDao();
	
	private VisitDao visitDao = new VisitDao();
	
	private SurveyStatDao statDao = new SurveyStatDao();

	public Route stat = (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long surveyId = Long.parseLong(request.params(":id"));
		
		Survey survey = surveyService.get(siteId, surveyId);
		if(survey == null){
			return CodeMsg.failure("survey not exist");
		}
		// fixed site id field
		statDao.fixedSiteId(survey.getId(), survey.getSiteId());
		
		Visit visit = visitDao.get(siteId, surveyId);
		Map<String,Integer> answerCount = answerDao.countByDate(siteId, surveyId);
		List<Object> fields = answerDao.statSurvey(surveyId);
		
		StatView view = new StatView();
		view.setId(surveyId);
		view.setTitle(survey.getTitle());
		view.setVisits(visit.getDateVisits());
		view.setAnswerCount(answerCount);
		view.setFields(fields);
		return CodeMsg.success().data(view);
	};
	
	@Data
	public static class StatView{
		private long id;

		private String title;
		private Map<String, Integer> visits;
		private Map<String, Integer> answerCount;
		private List<Object> fields;
	}
	
}
