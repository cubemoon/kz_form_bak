package com.kuaizhan.form.controller;

import lombok.extern.slf4j.Slf4j;
import spark.Route;
import spark.utils.StringUtils;

import com.kuaizhan.form.biz.AnswerService;
import com.kuaizhan.form.common.KZUtil;
import com.kuaizhan.form.common.RequestUtils;
import com.kuaizhan.form.dao.AnswerDao;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.view.AnswerView;
import com.kuaizhan.form.view.CodeMsg;
import com.kuaizhan.form.view.FieldView;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;

@Slf4j
public class AnswersHandler extends BaseHandler {
	
	AnswerService answerService = new AnswerService();
	
	AnswerDao answerDao = new AnswerDao();

	public Route listAnswers =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long surveyId = Long.parseLong(request.params(":survey_id"));
		String star = request.queryParams("star");
		String unread = request.queryParams("unread");
		String done = request.queryParams("done");
		String order = request.queryParams("order");
		boolean asc = (order == null) || "".equals(order) || ("asc".equals(order));
		
		int pageNo = RequestUtils.getIntParameter(request, "page_no", 1) ;
		int pageSize = RequestUtils.getIntParameter(request, "page_size", 10) ;
		
		QueryParameters queryParams = new QueryParameters();
		queryParams.setPageNo(pageNo);
		queryParams.setPageSize(pageSize);
		queryParams.addOrder("no", asc);
		
		if(StringUtils.isNotEmpty(star)){
			queryParams.addParameter("star", Boolean.parseBoolean(star));
		}
		if(StringUtils.isNotEmpty(unread)){
			queryParams.addParameter("unread", Boolean.parseBoolean(unread));
		}
		if(StringUtils.isNotEmpty(done)){
			queryParams.addParameter("done", Boolean.parseBoolean(done));
		}

		ListPage<Answer> answers = answerService.getBySurveyId(siteId, surveyId, queryParams);
		ListPage<AnswerView> listPage = AnswerView.fromModelList(answers);
		return CodeMsg.success().data(listPage);
	};
	
	public Route listFields =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long surveyId = Long.parseLong(request.params(":survey_id"));
		String fieldId = request.params(":id");
		
		int pageNo = RequestUtils.getIntParameter(request, "page_no", 1) ;
		int pageSize = RequestUtils.getIntParameter(request, "page_size", 10) ;
		
		QueryParameters queryParams = new QueryParameters();
		queryParams.setPageNo(pageNo);
		queryParams.setPageSize(pageSize);
		
		answerDao.fetchFieldValues(siteId, surveyId, fieldId, queryParams);

		ListPage<Answer> answers = answerDao.fetchFieldValues(siteId, surveyId, fieldId, queryParams);
		ListPage<FieldView> listPage = FieldView.fromModelList(answers);
		return CodeMsg.success().data(listPage);
	};
	

	
	
	
	public Route export = (request, response) -> {
		log.info("//////////////////////////////////////////////////");
		log.info("export survey_id " +  request.params(":survey_id"));
		
		try{	
			long siteId = KZUtil.getSiteId(request);
			long surveyId = Long.parseLong(request.params(":survey_id").trim());
			
			ExportUtils.exportSurvey(siteId, surveyId, request, response);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return CodeMsg.failure(e.getMessage());
		}
		return CodeMsg.success();
	};



	public Route get =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		Answer answer = answerService.get(siteId, id);
		AnswerView view = AnswerView.fromModel(answer);
		return CodeMsg.success().data(view);
	};

	public Route delete =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		boolean result = answerService.delete(siteId, id);
		return CodeMsg.parse(result, "删除反馈失败");
	};
	
	public Route star =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		boolean star = Boolean.parseBoolean(request.queryParams("star"));
		boolean result = answerService.star(siteId, id, star);
		return CodeMsg.parse(result, "标记失败");
	};
	
	public Route done =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		boolean done = Boolean.parseBoolean(request.queryParams("done"));
		boolean result = answerService.done(siteId, id, done);
		return CodeMsg.parse(result, "标记失败");
	};
	
	public Route markRead =  (request, response) -> {
		long siteId = KZUtil.getSiteId(request);
		long id = Long.parseLong(request.params(":id"));
		boolean unread = Boolean.parseBoolean(request.queryParams("unread"));
		boolean result = answerService.markRead(siteId, id, unread);
		return CodeMsg.parse(result, "标记失败");
	};

}
