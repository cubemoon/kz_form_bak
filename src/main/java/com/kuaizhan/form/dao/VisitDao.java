package com.kuaizhan.form.dao;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.kuaizhan.form.model.Visit;

public class VisitDao extends BasicDao<Visit, String>{

	public VisitDao() {
		super(Visit.class);
	}
	
	public Visit get(long siteId, long id){
		Query<Visit> query = this.createQuery().filter("site_id", siteId)
				.filter("_id", id);
		return query.get();
	}
	
	public boolean incrVisitCount(long id, String date, int delta){
		String key = String.format("date_visits.%s", date);
		Query<Visit> query = this.createQuery().filter("_id", id);
		UpdateOperations<Visit> ops = this.createUpdateOperations().inc(key, delta);
		UpdateResults ur = this.update(query, ops);
		return ur.getUpdatedCount() == 1;
	}

}
