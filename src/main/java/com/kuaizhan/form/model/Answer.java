package com.kuaizhan.form.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

 
@Indexes({@Index("site_id, survey_id,fields.id"),@Index("-no"), @Index("survey_id, created_date")})
@Entity(value="answers", noClassnameStored=true)
@Data
public class Answer {
	
	@Id
	private long id;
	
	// answer no
	private int no;
	
	@JsonProperty("survey_id")
	@Property("survey_id")
	private long surveyId;
	
	@JsonProperty("site_id")
	@Property("site_id")
	private long siteId;
	
	private String title;
	private String subtitle;
	
	@JsonProperty("created_at")
	@Property("created_at")
	private long createdAt;
	
	@JsonProperty("created_date")
	@Property("created_date")// yyyy-MM-dd
	private String createdDate;
	
	//TODO need mongodb indexes??
	private boolean star = false;
	private boolean unread = true;
	private boolean done = false;
	private boolean deleted = false;
	
	// this map stored data of fields
	private List<Object> fields;
}
