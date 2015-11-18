package com.kuaizhan.form.biz;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kuaizhan.form.common.IdUtil;
import com.kuaizhan.form.common.StringUtil;
import com.kuaizhan.form.dao.AnswerDao;
import com.kuaizhan.form.dao.SurveyDao;
import com.kuaizhan.form.dao.SurveyStatDao;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;


public class AnswerService {
	
	private SurveyDao surveyDao = new SurveyDao();

	private AnswerDao answerDao = new AnswerDao();
	
	private SurveyStatDao statDao = new SurveyStatDao(); 

	public long create(Answer answer) {
		
		Survey survey = surveyDao.getSimple(answer.getSurveyId());
		if (survey == null){
			throw new IllegalArgumentException("can't get survey");
		}
		
		answer.setId(IdUtil.nextId());
		answer.setSurveyId(survey.getId());
		answer.setSiteId(survey.getSiteId());
		answer.setTitle(survey.getTitle());
		answer.setSubtitle(survey.getSubtitle());
		answer.setCreatedAt(new Date().getTime());
		answer.setCreatedDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
		int answerNo = statDao.getAnswerOrder(answer.getSurveyId());
		answer.setNo(answerNo);
		
		long answerId = (long)answerDao.save(answer).getId();
		statDao.incrAnswerCount(answer.getSurveyId(), 1);
		statDao.fixedSiteId(answer.getSurveyId(), answer.getSiteId());// fixed site id

		incrSurveyStat(answer);
		return answerId;	
	}

	@SuppressWarnings("unchecked")
	private void incrSurveyStat(Answer answer) {
		
		if(answer == null){return;}
		if(answer.getFields() == null || answer.getFields().isEmpty()){return;}
		
		for (Object field : answer.getFields()) {
			Map<String, Object> fieldMap = (Map<String, Object>)field;
			String fieldName = (String)fieldMap.get("name");
			String fieldId = String.valueOf(fieldMap.get("id"));
			
			if("id_text".equals(fieldName)
				|| "id_textarea".equals(fieldName)){
				// increase answer count;
				String value = (String)fieldMap.get("value");
				if(StringUtil.isNotEmpty(value)){
					statDao.incrFieldAnswerCount(answer.getSurveyId(), fieldId, 1);
				}
				
			}else if("id_radio".equals(fieldName) 
					|| "id_checkbox".equals(fieldName)
					|| "id_dropdown".equals(fieldName)
					|| "id_image_radio".equals(fieldName)
					|| "id_image_checkbox".equals(fieldName)
					){
				
				List<Map<String, Object>> values = (List<Map<String, Object>>)fieldMap.get("value");
				boolean skipDropdownDefault = false;
				
				for(Map<String,Object> value : values){
					boolean selected = (Boolean)value.get("selected");
					String valueName = (String)value.get("name");
					int lid = Integer.parseInt(String.valueOf(value.get("lid")));
					
					if("id_dropdown".equals(fieldName)  && lid == -1){
						skipDropdownDefault = true;
					}
					
					if(selected){
						// increase selected count
						if("id_dropdown".equals(fieldName)  && skipDropdownDefault){
							lid = lid + 1;// skip dropdown default value
						}
						statDao.incrFieldSelectedCount(answer.getSurveyId(), fieldId, valueName, lid, 1);
					}
				}
				 
				
			}else if("id_section".equals(fieldName)){
				// do nothing
			}
			
		}
	}
	
	public Answer update() {
		return null;
	}
	
	public boolean delete(long siteId, long id) {
		return answerDao.deleteById(siteId, id);
	}
	
	public Answer get(long siteId, long id) {
		return answerDao.get(siteId, id);
	}
	
	public ListPage<Answer> getBySurveyId(long siteId, long surveyId, QueryParameters queryParams) {
		return answerDao.getBySurveyId(siteId, surveyId, queryParams);
	}
	
	public boolean star(long siteId, long id, boolean star){
		return answerDao.updateProperty(siteId, id, "star", star);
	}
	
	public boolean done(long siteId, long id, boolean done){
		return answerDao.updateProperty(siteId, id, "done", done);
	}
	
	public boolean markRead(long siteId, long id, boolean unread){
		return answerDao.updateProperty(siteId, id, "unread", unread);
	}
}
