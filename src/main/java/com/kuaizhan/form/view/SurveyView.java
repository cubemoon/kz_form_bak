package com.kuaizhan.form.view;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;
import com.kuaizhan.form.model.Survey;

@Data
public class SurveyView {

	private String id;

	@JsonProperty("site_id")
	private long siteId;
	
	private String title;
	
	private String subtitle;
	
	@JsonProperty("created_at")
	private long createdAt;
	
	@JsonProperty("title_visible")
	private boolean titleVisible;
	
	private boolean published = false;
	
	private boolean deleted = false;
	
	@JsonProperty("published_at")
	private long publishedAt;
	
	// this map stored data of fields
	private List<Object> fields;
	
	public static SurveyView fromModel(Survey survey){
		SurveyView surveyView = new SurveyView();
		surveyView.setId(String.valueOf(survey.getId()));
		surveyView.setCreatedAt(survey.getCreatedAt());
		surveyView.setTitle(survey.getTitle());
		surveyView.setSubtitle(survey.getSubtitle());
		surveyView.setSiteId(survey.getSiteId());
		surveyView.setDeleted(survey.isDeleted());
		surveyView.setPublished(survey.isPublished());
		surveyView.setPublishedAt(survey.getPublishedAt());
		surveyView.setTitleVisible(survey.isTitleVisible());
		surveyView.setFields(survey.getFields());
		return surveyView;
	}
	
	public static ListPage<SurveyView> fromModelList(ListPage<Survey> listPage){
		List<SurveyView> viewList = Lists.newArrayList();
		if(listPage.getDataList() != null){
			for(Survey survey : listPage.getDataList()){
				SurveyView surveyView = fromModel(survey);
				viewList.add(surveyView);
			}
		}
		
		ListPage<SurveyView> viewListPage = new ListPage<SurveyView>();
		viewListPage.setCurrentPageNo(listPage.getCurrentPageNo());
		viewListPage.setCurrentPageSize(listPage.getCurrentPageSize());
		viewListPage.setTotalCount(listPage.getTotalCount());
		viewListPage.setDataList(viewList);
		return viewListPage;
	}
	
}
