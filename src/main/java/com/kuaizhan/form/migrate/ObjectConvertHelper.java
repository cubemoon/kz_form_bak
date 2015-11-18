package com.kuaizhan.form.migrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kuaizhan.form.model.Answer;

public final class ObjectConvertHelper {

	public static List<Answer> insertAnswers(List<AnswerOld>list){
		if(list==null||list.isEmpty()){
			return null;
		}
		List<Answer> answers = new ArrayList<Answer>();
		for(AnswerOld ansOld:list){
			Answer answer = new Answer();
			
			answer.setSiteId(ansOld.getSite_id());
			answer.setSurveyId(ansOld.getForm_id());
			answer.setCreatedDate(ansOld.getCtime());
			answer.setTitle(ansOld.getTitle());
			answer.setStar(ansOld.getStatus().equals("1"));
			
			List<AnswerOld.Item> items = ansOld.getItems();
			List<Object> fieldMap = new ArrayList<Object>();
			if(items!=null && !items.isEmpty()){
				for(AnswerOld.Item item:items){
					Map<String, String>map = new HashMap<String, String>();
					map.put("value", item.getAnwser());
					map.put("required", item.getIs_required());
					map.put("title", item.getTitle());
					fieldMap.add(map);
				}
			}
			answer.setFields(fieldMap);
			answers.add(answer);
		}
		return answers;
	}
}
