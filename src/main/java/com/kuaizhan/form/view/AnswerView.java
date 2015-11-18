package com.kuaizhan.form.view;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;
import com.kuaizhan.form.model.Answer;

 
@Data
public class AnswerView {
	
	private String id;
	
	// answer no
	private int no;
	
	@JsonProperty("survey_id")
	private String surveyId;
	
	@JsonProperty("site_id")
	private long siteId;
	
	private String title;
	private String subtitle;
	
	@JsonProperty("created_at")
	private long createdAt;
	
	@JsonProperty("created_date")
	private String createdDate;
	
	private boolean star = false;
	private boolean unread = true;
	private boolean done = false;
	private boolean deleted = false;
	
	// this map stored data of fields
	private List<Object> fields;
	
	public static AnswerView fromModel(Answer answer){
		AnswerView answerView = new AnswerView();
		answerView.setId(String.valueOf(answer.getId()));
		answerView.setNo(answer.getNo());
		answerView.setSurveyId(String.valueOf(answer.getSurveyId()));
		answerView.setCreatedAt(answer.getCreatedAt());
		answerView.setTitle(answer.getTitle());
		answerView.setSubtitle(answer.getSubtitle());
		answerView.setSiteId(answer.getSiteId());
		answerView.setDeleted(answer.isDeleted());
		answerView.setUnread(answer.isUnread());
		answerView.setDone(answer.isDone());
		answerView.setStar(answer.isStar());
		answerView.setFields(answer.getFields());
		return answerView;
	}
	
	public static ListPage<AnswerView> fromModelList(ListPage<Answer> listPage){
		List<AnswerView> viewList = Lists.newArrayList();
		if(listPage.getDataList() != null){
			for(Answer answer : listPage.getDataList()){
				AnswerView item = fromModel(answer);
				viewList.add(item);
			}
		}
		
		ListPage<AnswerView> viewListPage = new ListPage<AnswerView>();
		viewListPage.setCurrentPageNo(listPage.getCurrentPageNo());
		viewListPage.setCurrentPageSize(listPage.getCurrentPageSize());
		viewListPage.setTotalCount(listPage.getTotalCount());
		viewListPage.setDataList(viewList);
		return viewListPage;
	}
}
