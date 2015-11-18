package com.kuaizhan.form.migrate;

import java.util.List;

import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.kuaizhan.form.dao.DaoHelper;

public class DataExportDao extends BasicDAO<AnswerOld,String>{

	public DataExportDao(){
		super(AnswerOld.class, DaoHelper.getOldDatastore());
	}
	
	public List<AnswerOld> getAllAnswer(){
		Query<AnswerOld>query = this.createQuery();
		List<AnswerOld> answerOlds = query.asList();
		return answerOlds;
	}
}
