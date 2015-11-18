package com.kuaizhan.form.dao;

import lombok.extern.slf4j.Slf4j;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.kuaizhan.form.model.SurveyStat;

@Slf4j
public class SurveyStatDao  extends BasicDao<SurveyStat, Long>{
	
	public SurveyStatDao() {
		super(SurveyStat.class);
	}
	
	public boolean fixedSiteId(long id, long siteId){
		Query<SurveyStat> q = this.createQuery().filter("_id", id);
		UpdateOperations<SurveyStat> ops = this.createUpdateOperations().set("site_id", siteId);
		UpdateResults ur = this.update(q, ops);
		return ur.getUpdatedCount() == 1;
	}
	
	public boolean incrVisitCount(long id, int delta){
		Query<SurveyStat> query = this.createQuery().filter("_id", id);
		UpdateOperations<SurveyStat> ops = this.createUpdateOperations().inc("visit_count", delta);
		UpdateResults ur = this.update(query, ops);
		return ur.getUpdatedCount() == 1;
	}
	
	public boolean incrAnswerCount(long id, int delta){
		Query<SurveyStat> query = this.createQuery().filter("_id", id);
		UpdateOperations<SurveyStat> ops = this.createUpdateOperations().inc("answer_count", delta);
		UpdateResults ur = this.update(query, ops);
		return ur.getUpdatedCount() == 1;
	}
	
	public int getAnswerOrder(long id){
		Query<SurveyStat> q = this.createQuery().filter("_id", id);
		UpdateOperations<SurveyStat> ops = this.createUpdateOperations().inc("answer_order", 1);
		SurveyStat result = this.getDatastore().findAndModify(q, ops, false);
		return result.getAnswerOrder();
	}
	
	public boolean incrFieldSelectedCount(long id, String fieldId, String valueName, int valueIndex, int delta){
		Query<SurveyStat> q = this.createQuery()
				.disableValidation()
				.filter("_id", id)
				.filter("fields.id", fieldId)
				.filter("fields.value.name", valueName);
		
		String incrField = String.format("fields.$.value.%d.selected_count", valueIndex); 
		UpdateOperations<SurveyStat> ops = this.createUpdateOperations()
				.disableValidation().inc(incrField, delta);
		UpdateResults ur = this.update(q, ops);
		return ur.getUpdatedCount() == 1;
	}
	
	public boolean incrFieldAnswerCount(long id, String fieldId, int delta){
		Query<SurveyStat> q = this.createQuery()
				.disableValidation()
				.filter("_id", id)
				.filter("fields.id", fieldId);
		UpdateOperations<SurveyStat> ops = this.createUpdateOperations()
				.disableValidation()
				.inc("fields.$.answer_count", delta);
		UpdateResults ur = this.update(q, ops);
		return ur.getUpdatedCount() == 1;
	}
	
 }
