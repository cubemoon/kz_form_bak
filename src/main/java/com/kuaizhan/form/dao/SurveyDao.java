package com.kuaizhan.form.dao;

import lombok.extern.slf4j.Slf4j;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.kuaizhan.form.model.Survey;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;
import com.mongodb.WriteResult;

@Slf4j
public class SurveyDao  extends BasicDao<Survey, Long>{
	
	public SurveyDao() {
		super(Survey.class);
	}
	
	public Survey get(long id) {
		Query<Survey> query = this.createQuery().filter("_id", id)
				.filter("deleted", false);
		return query.get();
	}
	
	public Survey get(long siteId, long id) {
		Query<Survey> query = this.createQuery().filter("_id", id)
				.filter("site_id", siteId)
				.filter("deleted", false);
		return query.get();
	}
	
	public Survey getSimple(long id){
		Query<Survey> query = this.createQuery()
				.filter("_id", id)
				.filter("deleted", false)
				.retrievedFields(false, "fields");
		return query.get();
	}
	
	public boolean deleteById(long siteId, long id) {
		log.info(String.format("deleteById(siteId=%d, id=%d)", siteId, id));
		Query<Survey> q = this.createQuery().filter("site_id", siteId)
				.filter("_id", id);
		UpdateOperations<Survey> ops = this.createUpdateOperations().set("deleted", true);
		WriteResult writeResult = this.update(q, ops).getWriteResult();
		return writeResult.getN() == 1;
	}
	

	public ListPage<Survey> getBySiteId(long siteId, QueryParameters queryParams) {
		Query<Survey> query = this.createQuery().filter("site_id", siteId)
				.filter("deleted", false)
				.retrievedFields(false, "fields")
				.order("-_id");
		return queryListPage(query, queryParams);
	}


}
