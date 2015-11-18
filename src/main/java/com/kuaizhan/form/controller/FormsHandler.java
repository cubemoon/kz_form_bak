package com.kuaizhan.form.controller;

import lombok.extern.slf4j.Slf4j;
import spark.Route;

import com.kuaizhan.form.biz.AnswerService;
import com.kuaizhan.form.biz.SurveyService;
import com.kuaizhan.form.common.JacksonUtils;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.view.CodeMsg;
import com.kuaizhan.form.view.SurveyView;

@Slf4j
public class FormsHandler extends BaseHandler {

	private SurveyService surveyService = new SurveyService();
	
	AnswerService answerService = new AnswerService();


	/** 访问表单 **/
	public Route get = (request, response) -> {
		long id = Long.parseLong(request.params(":id"));
		Survey survey = surveyService.get(id);
		
		if (survey == null) {
			return CodeMsg.failure(404, "survey not found");
		} else {
			SurveyView surveyView = SurveyView.fromModel(survey);
			return CodeMsg.success().data(surveyView);
		}

	};
	
	public Route visit = (request, response) -> {
		long id = Long.parseLong(request.params(":id"));
		boolean result = surveyService.incrVisitCount(id);
		return CodeMsg.parse(result);
	};
	
	public Route postAnswer =  (request, response) -> {
		String dataJson = request.body();
		log.info("body = \n" + dataJson);
		
		long surveyId = Long.parseLong(request.params(":survey_id"));

		try {
			Survey survey = surveyService.get(surveyId);
			if(survey == null || !survey.isPublished()){
				throw new RuntimeException("找不到对应的表单");
			}
			
			Answer answer = JacksonUtils.toBean(dataJson, Answer.class);
			answer.setSurveyId(surveyId);
			answerService.create(answer);
			return CodeMsg.success().data(survey.getAfterSubmit());
		} catch (Exception e) {
			log.error("parse answer error " + e.getMessage(), e);
			return CodeMsg.failure("表单提交失败" + e.getMessage());
		}
	};

}
