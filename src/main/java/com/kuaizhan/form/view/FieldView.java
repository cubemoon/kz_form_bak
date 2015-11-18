package com.kuaizhan.form.view;

import java.util.List;
import java.util.Map;

import lombok.Data;

import com.google.common.collect.Lists;
import com.kuaizhan.form.model.Answer;

 
@Data
public class FieldView {
	
	private String id;
	
	private String answerId;
	// answer no
	private int no;
	private String value;
	
	@SuppressWarnings("unchecked")
	public static FieldView fromModel(Answer answer){
		FieldView answerView = new FieldView();
		answerView.setAnswerId(String.valueOf(answer.getId()));
		answerView.setNo(answer.getNo());
		
		if(answer.getFields() == null || answer.getFields().isEmpty()){
			return null;
		}
		Map<String, Object> fieldMap = (Map<String, Object>)answer.getFields().get(0);
		String fieldValue = (String) fieldMap.get("value");
		answerView.setValue(fieldValue);
		return answerView;
	}
	
	public static ListPage<FieldView> fromModelList(ListPage<Answer> listPage){
		List<FieldView> viewList = Lists.newArrayList();
		if(listPage.getDataList() != null){
			for(Answer answer : listPage.getDataList()){
				FieldView item = fromModel(answer);
				if(item != null){
					viewList.add(item);
				}
			}
		}
		
		ListPage<FieldView> viewListPage = new ListPage<FieldView>();
		viewListPage.setCurrentPageNo(listPage.getCurrentPageNo());
		viewListPage.setCurrentPageSize(listPage.getCurrentPageSize());
		viewListPage.setTotalCount(listPage.getTotalCount());
		viewListPage.setDataList(viewList);
		return viewListPage;
	}
}
