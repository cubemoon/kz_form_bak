package com.kuaizhan.form.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.google.common.collect.Lists;
import com.kuaizhan.form.common.ResourceUtils;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.QueryBuilder;

@Slf4j
public class AnswerDao extends BasicDao<Answer, Long>{
	
	public AnswerDao() {
		super(Answer.class);
	}
	
	public Answer get(long siteId, long id) {
		Query<Answer> q = this.createQuery().filter("id", id).filter("site_id", siteId);
		return q.get();
	}
	
	public boolean updateProperty(long siteId, long id, String property, Object value){
		Query<Answer> q = this.createQuery().filter("id", id).filter("site_id", siteId);
		UpdateOperations<Answer> ops = this.createUpdateOperations().set(property, value);
		return this.update(q, ops).getWriteResult().getN() == 1;
	}
	
	public boolean deleteById(long siteId, long id) {
		Query<Answer> q = this.createQuery().filter("id", id).filter("site_id", siteId);
		UpdateOperations<Answer> ops = this.createUpdateOperations().set("deleted", true);
		return this.update(q, ops).getWriteResult().getN() == 1;
	}
	
	public ListPage<Answer> getBySurveyId(long siteId, long surveyId, QueryParameters queryParams){
		Query<Answer> q = this.createQuery().filter("site_id", siteId)
				.filter("survey_id", surveyId)
				.filter("deleted", false)
				.retrievedFields(false, "fields");
		
		if(queryParams.getParameter("star") != null){
			q.filter("star", queryParams.getParameter("star"));
		}
		if(queryParams.getParameter("done") != null){
			q.filter("done", queryParams.getParameter("done"));
		}
		if(queryParams.getParameter("unread") != null){
			q.filter("unread", queryParams.getParameter("unread"));
		}
		
		return queryListPage(q, queryParams);
	}
	
	// for export excel
	public List<Answer> getFullBySurveyId(long siteId, long surveyId){
		Query<Answer> q = this.createQuery().filter("site_id", siteId)
				.filter("survey_id", surveyId)
				.filter("deleted", false);
		return q.asList();
	}
	
	public HashMap<String, Integer> countByDate(long siteId, long surveyId) {
		
		DBObject cond = new BasicDBObject("site_id", siteId);
		cond.put("survey_id", surveyId);
		
		DBObject key = new BasicDBObject("created_date","created_date");
		DBObject initial = new BasicDBObject("cnt", 0);
		String reduce = "function(item,prev){prev.cnt+=1;}";

		DBObject group = getCollection().group(key, cond, initial, reduce);

		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (String i : group.keySet()) {
			BasicDBObject item = (BasicDBObject) group.get(i);
			String createdDate = item.getString("created_date");
			Double cnt = item.getDouble("cnt");
			ret.put(createdDate, cnt.intValue());
		}
		return ret;
	}
	
	public HashMap<String, Integer> countByDate2(long siteId, long surveyId) {
		
		DBObject match = new BasicDBObject("site_id", siteId);
		match.put("survey_id", surveyId);
		
		DBObject groupRule = new BasicDBObject("_id",new BasicDBObject("created_date","$created_date"));
		groupRule.put("count", new BasicDBObject("$sum",1));
		DBObject group = new BasicDBObject("$group", groupRule);
//		DBObject sort = new BasicDBObject("created_date", "1");
		
		List<DBObject> pipeline = Arrays.asList(group);
		AggregationOutput output = getCollection().aggregate(pipeline);
		
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (DBObject item : output.results()) {
			DBObject id = (DBObject)item.get("_id");
			String createdDate = (String)id.get("created_date");
			Integer count = (Integer)item.get("count");
		    ret.put(createdDate, count);
		}
		return ret;
	}
	
	public ListPage<Answer> fetchFieldValues(long siteId, long surveyId, String fieldId, QueryParameters queryParams){
		
		Query<Answer> q = (Query<Answer>) this.createQuery().disableValidation()
				.filter("site_id", siteId)
				.filter("survey_id", surveyId)
				.filter("deleted", false)
				.filter("fields.id", fieldId)
//				.filter("fields.name", "id_text")
//				.filter("fields.name in", Lists.newArrayList("id_text", "id_textarea"))
				.filter("fileds.value != ", "")
				.retrievedFields(true, "no", "title", "fields.$");
		
		return queryListPage(q, queryParams);
	}
	
	@Deprecated
	public List<DBObject> _fetchFieldValues(long siteId, long surveyId, String fieldId){
		
		int numToSkip = 0;
		int batchSize = 1000;
		DBObject fields =  new BasicDBObject("fields.$",1);
		fields.put("title", 1);
		
		QueryBuilder queryBuiler = QueryBuilder
				.start("site_id").is(siteId)
				.put("survey_id").is(surveyId)
				.put("deleted").is(false)
				.put("fields.id").is(fieldId)
				.put("fields.name").is("id_text");
		DBCursor cursor  = getCollection().find(queryBuiler.get(), fields, numToSkip, batchSize);
		
		List<DBObject> results = Lists.newArrayList();
		while(cursor != null && cursor.hasNext()){
			DBObject item = cursor.next();
			results.add(item);
		}
		return results;
	}
	
	public List<Answer> fetchFieldValues2(long siteId, long surveyId, String fieldId){
		
		DBObject unwind = new BasicDBObject("$unwind", "$fields");
		
		QueryBuilder queryBuiler = QueryBuilder
				.start("site_id").is(siteId)
				.put("survey_id").is(surveyId)
				.put("deleted").is(false)
				.put("fields.id").is(fieldId);
//				.put("fields.name").is("id_text");
		DBObject match = new BasicDBObject("$match", queryBuiler.get());

		DBObject fields =  new BasicDBObject("_id",1);
		fields.put("no", 1);
		fields.put("title", 1);
		fields.put("value", "$fields.value");
		DBObject project = new BasicDBObject("$project", fields );

		// Finally the $sort operation
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", 1));
		List<DBObject> pipeline = Arrays.asList(unwind, match, project,sort);
		
		AggregationOutput output = getCollection().aggregate(pipeline);
		for (DBObject result : output.results()) {
		    System.out.println(result);
		}

		//TODO upgrade mongodb to 2.6
//		AggregationOptions aggregationOptions = AggregationOptions.builder()
//		        .batchSize(100)
//		        .outputMode(AggregationOptions.OutputMode.CURSOR) // support on mongodb 2.6
//		        .allowDiskUse(true)
//		        .build();
//		Cursor cursor = getCollection().aggregate(pipeline, aggregationOptions);
//		while (cursor.hasNext()) {
//		    System.out.println(cursor.next());
//		}
		return null;
	}
	
	// use map reduce to generate real-time statistics
	// get all answer for a survey 
	// emit by field_id
	// reduce by answer count and  selected count
	static String  map = ResourceUtils.loadJsonFile("answer_map.json");
	static String  reduce = ResourceUtils.loadJsonFile("answer_reduce.json");
	static String  finalize = ResourceUtils.loadJsonFile("answer_finalize.json");

	@SuppressWarnings("rawtypes")
	public List<Object> statSurvey(long surveyId){
		
		DBObject query = QueryBuilder
				.start("survey_id").is(surveyId)
				.and("deleted").is(false).get();

		String outputTarget = null;
		MapReduceCommand.OutputType outputType = MapReduceCommand.OutputType.INLINE;
		MapReduceCommand  cmd = new MapReduceCommand( getCollection() , map , reduce , outputTarget , outputType , query );
		cmd.setFinalize(finalize);
		MapReduceOutput mapReduceOutput = getCollection().mapReduce(cmd);
		
		List<Object> results = Lists.newArrayList();
		for(DBObject item : mapReduceOutput.results()){
			results.add(item.toMap().get("value"));
		}

		// sort fields by order no
		Collections.sort(results, (Object o1, Object o2) -> {
			Double no1 = (Double) ((Map) o1).get("no");
			Double no2 = (Double) ((Map) o2).get("no");
			return no1.compareTo(no2);
		} );
		return results;
	}
		
}
