package com.kuaizhan.form.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity(value = "survey_stat", noClassnameStored = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyStat {

	/** survey id */
	@Id 
	private long id;
	
	@JsonProperty("site_id")
	@Property("site_id")
	private long siteId;
	
	// redundant title of survey for query
	private String title;
	@Property("visit_count")
	@JsonProperty("visit_count")
	private int visitCount = 0;
	@Property("answer_count")
	@JsonProperty("answer_count")
	private int answerCount = 0;
	@Property("answer_order")
	@JsonProperty("answer_order")
	private int answerOrder = 0; // increase only
	
	private List<Object> fields;
	//TODO 每天的浏览数统计
	// visits[{"2014-08-13":0},{"2014-08-15":2}]
	// findAndModify
}
