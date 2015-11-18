package com.kuaizhan.form.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import spark.utils.StringUtils;

import com.google.common.collect.Maps;
import com.kuaizhan.form.biz.KZService;
import com.kuaizhan.form.common.JacksonUtils;
import com.kuaizhan.form.common.StringUtil;

@Slf4j
public class SurveyUtil {
	
	private static KZService kzService = new KZService();

	public static Survey parseSurvey(String dataJson) {

		try { 
			Survey survey = JacksonUtils.toBean(dataJson, Survey.class);
			return survey;
		} catch (IOException e) {
			log.error(String.format("parse survey json error %s", e.getMessage()),e);
			return null;
		}catch (Exception e) {
			log.error(String.format("parse survey json error %s", e.getMessage()),e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Survey validateSurvey(Survey survey){
		
		if (survey == null) {
			throw new RuntimeException("survey  can't be null");		
		}
		
		if(StringUtils.isEmpty(survey.getTitle())){
			throw new RuntimeException("表单的标题不能为空");
		}
		if(survey.getTitle().length() > 15){//FIXME magic number
			String title = survey.getTitle().substring(0, 15);
			survey.setTitle(title);
		}
		
		String subtitle = survey.getSubtitle();
		if(subtitle != null && subtitle.length() > 100){
			subtitle = subtitle.substring(0, 100);
			survey.setSubtitle(subtitle);
		}
		
		if(survey.getFields() == null || survey.getFields().isEmpty()){
			if(survey.isPublished()){
				throw new RuntimeException("不能发布表单项为空的表单");
			}else{
				return survey;
			}
		}
		
		if(survey.getFields() != null ){
			for(Object field : survey.getFields()){
				LinkedHashMap<String, Object> fieldMap = (LinkedHashMap<String, Object>)field;
				if(fieldMap == null || fieldMap.isEmpty()){
					throw new RuntimeException("表单项格式出错，内容(" + field + ")");
				}
				
				String fieldName = (String)fieldMap.get("name");
				if(StringUtil.isEmpty(fieldName)){
					throw new RuntimeException(String.format("表单项的名称不能为空, 表单项： %s ", fieldMap.get("title")));
				}
						
				if("id_checkbox".equals(fieldName) 
					|| "id_radio".equals(fieldName) //FIXME etc.
					){
					List<LinkedHashMap<String, String>> values = (List<LinkedHashMap<String, String>>)fieldMap.get("value");
					if (values == null || values.isEmpty()){
						throw new RuntimeException(String.format("表单项的选项不能为空, field = %s ", fieldMap.get("title")));
					}
				}
			}
		}
		
		if(survey.getAfterSubmit() == null){
			if(survey.isPublished()){
				throw new RuntimeException("请设置表单填写完成后跳转的页面");
			}
		}
		
			

		Map<String, String> after_submit = survey.getAfterSubmit();
		String forwardType = after_submit.get("type");
		if ("page_id".equals(forwardType)) {
			try {
				long pageId = Long.parseLong(after_submit.get("value"));
				String url = kzService.getPageURL(survey.getSiteId(), pageId);
				
				Map<String, String> data = Maps.newHashMap();
				data.put("type", "url");
				data.put("value",  url);
				survey.setAfterSubmit(data);
			} catch (NumberFormatException e) {
				throw new RuntimeException("请选择正确的页面");
			}

		} else if ("post_id".equals(forwardType)) {
			try {
				long postId = Long.parseLong(after_submit.get("value"));
				String url = kzService.getPostURL(survey.getSiteId(), postId);
				
				Map<String, String> data = Maps.newHashMap();
				data.put("type", "url");
				data.put("value",  url);
				survey.setAfterSubmit(data);
			} catch (NumberFormatException e) {
				throw new RuntimeException("请选择正确的文章");
			}
		}
		
		return survey;
	}
}
