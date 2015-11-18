package com.kuaizhan.form.biz;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import com.kuaizhan.form.common.IdUtil;
import com.kuaizhan.form.dao.SurveyDao;
import com.kuaizhan.form.dao.SurveyStatDao;
import com.kuaizhan.form.dao.VisitDao;
import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.model.SurveyStat;
import com.kuaizhan.form.model.SurveyUtil;
import com.kuaizhan.form.model.Visit;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;


public class SurveyService {

	private SurveyDao surveyDao = new SurveyDao();
	
	SurveyStatDao statDao = new SurveyStatDao();
	
	VisitDao visitDao = new VisitDao();

	public long save(Survey survey) {
		if(survey.getId() != 0){
			Survey exist = surveyDao.get(survey.getSiteId(), survey.getId());
			if(exist != null){
				survey.setCreatedAt(exist.getCreatedAt());
			}
		}else{
			survey.setId(IdUtil.nextId());
			survey.setCreatedAt(new Date().getTime());
			survey.setPublished(false);
		}
		
		surveyDao.save(survey);
		return survey.getId();
	}
	
	public boolean publish(long siteId, long id) {
		
		Survey survey = surveyDao.get(siteId, id);
		if(survey == null){
			throw new RuntimeException("表单不存在");
		}
		
		survey = SurveyUtil.validateSurvey(survey);
		
		survey.setPublished(true);
		survey.setPublishedAt(new Date().getTime());
		surveyDao.save(survey);
		
		// will be reset when republish??? //FIXME
//		SurveyStat existStat = statDao.get(id);
//		if(existStat == null){
			SurveyStat stat = new SurveyStat();
			stat.setId(survey.getId());
			stat.setSiteId(survey.getSiteId());
			stat.setTitle(survey.getTitle());
			stat.setFields(survey.getFields());
			statDao.save(stat);
//		}
		
		Visit existVisit = visitDao.get(siteId, id);
		if(existVisit == null){
			Visit visit = new Visit();
			visit.setId(survey.getId());
			visit.setSiteId(survey.getSiteId());
			visit.setDateVisits(new HashMap<String, Integer>());
			visitDao.save(visit);
		}
		return true;
	}
	
	public Survey get(long siteId, long id) {
		return surveyDao.get(siteId, id);
	}
	
	public Survey getSimple(long siteId, long id) {
		return surveyDao.getSimple(id);
	}
	
	public Survey get(long id) {
		return surveyDao.get(id);
	}
	
	public boolean delete(long siteId, long id) {
		return surveyDao.deleteById(siteId, id);
	}

	public ListPage<Survey> getBySiteId(long siteId, QueryParameters queryParams) {
		return surveyDao.getBySiteId(siteId, queryParams);
	}
	
	public boolean incrVisitCount(long id){
		String curDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
		return visitDao.incrVisitCount(id, curDate, 1);
	}
}
