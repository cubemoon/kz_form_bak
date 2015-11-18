package com.kuaizhan.form;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import spark.Spark;

import com.google.common.collect.Maps;
import com.kuaizhan.form.common.KZUtil;
import com.kuaizhan.form.controller.AnswersHandler;
import com.kuaizhan.form.controller.FormsHandler;
import com.kuaizhan.form.controller.HealthCheckHandler;
import com.kuaizhan.form.controller.ImageUploadHandler;
import com.kuaizhan.form.controller.QRCodeHandler;
import com.kuaizhan.form.controller.SurveysHandler;

@Slf4j
public class FormServer extends AbstractServer{
	
	static{
		// Enable MongoDB logging in general
		System.setProperty("DEBUG.MONGO", "true");
		// Enable DB operation tracing
		System.setProperty("DB.TRACE", "true");
	}

	private static void route() {
		/* api route */
		Spark.get("/healthcheck", new HealthCheckHandler());
		
		// form url match for template view
		FormsHandler formsHandler = new FormsHandler();
		Spark.get("/forms/:id", "text/html", (request,respons)->{
//			long siteId = KZUtil.getSiteId(request);
			Map<String, Object> data = Maps.newHashMap();
//			data.put("siteId", siteId);
			return Spark.modelAndView(data, "form.ftl");
		} , tmplEngine);
		
		Spark.get("/forms/:id", "application/json", formsHandler.get, jsonTransformer);  // preview survey
		Spark.post("/forms/:id/visits", formsHandler.visit, jsonTransformer);
		Spark.get("/forms/:id/qrcode", new QRCodeHandler());
		Spark.post("/forms/:survey_id/answers", formsHandler.postAnswer, jsonTransformer);

		SurveysHandler surveyHandler = new SurveysHandler();
		//survey restful
		Spark.get("/surveys", surveyHandler.listSurveys, jsonTransformer);
		Spark.post("/surveys", surveyHandler.post, jsonTransformer); // save draft survey/ publish survey
		Spark.put("/surveys", surveyHandler.post, jsonTransformer); // save draft survey/ publish survey
		Spark.post("/surveys/images", new ImageUploadHandler(), jsonTransformer);
		Spark.get("/surveys/:id", "application/json", surveyHandler.get, jsonTransformer);  // preview survey
		Spark.put("/surveys/:id/publish", surveyHandler.publish, jsonTransformer);
		Spark.delete("/surveys/:id", surveyHandler.delete, jsonTransformer);
		Spark.get("/surveys/:id/stat",  surveyHandler.stat, jsonTransformer);// view survey statistics
		
		AnswersHandler answerHandler = new AnswersHandler();
		Spark.get("/surveys/:survey_id/answers", answerHandler.listAnswers, jsonTransformer);
		Spark.get("/surveys/:survey_id/fields/:id", answerHandler.listFields, jsonTransformer);
		Spark.get("/surveys/:survey_id/answers/:id", answerHandler.get, jsonTransformer);
		Spark.delete("/surveys/:survey_id/answers/:id", answerHandler.delete, jsonTransformer);
		Spark.put("/surveys/:survey_id/answers/:id/star", answerHandler.star, jsonTransformer);
		Spark.put("/surveys/:survey_id/answers/:id/done", answerHandler.done, jsonTransformer);
		Spark.put("/surveys/:survey_id/answers/:id/read", answerHandler.markRead, jsonTransformer);
		Spark.get("/surveys/:survey_id/answers/:id/export", answerHandler.export);

		
		
		// 表单管理入口页
		Spark.get("/survey", (request,respons)->{
			long siteId = KZUtil.getSiteId(request);
			Map<String, Object> data = Maps.newHashMap();
			data.put("siteId", siteId);
			return Spark.modelAndView(data, "survey.ftl");
		} , tmplEngine);	
		
		
	}

	public static void main(String[] args) {
		try {
			init();
			configure();
			route();

		} catch (Throwable t) {
			log.error(String.format(STRONG_FMT, "Server start error!"), t);
		}

		log.info(String.format(STRONG_FMT, "Server started!"));
	}

	
}
