package com.kuaizhan.form.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

@Indexes(@Index("site_id, deleted"))
@Entity(value = "surveys", noClassnameStored = true)
@Data
public class Survey {

	@Id
	private long id;

	@JsonProperty("site_id")
	@Property("site_id")
	private long siteId;

	private String title;

	private String subtitle;

	@JsonProperty("created_at")
	@Property("created_at")
	private long createdAt;

	@JsonProperty("title_visible")
	@Property("title_visible")
	private boolean titleVisible;

	private boolean published = false;
	
	private boolean deleted = false;
	
	@JsonProperty("published_at")
	@Property("published_at")
	private long publishedAt;

	@JsonProperty("after_submit")
	@Property("after_submit")
	private Map<String, String> afterSubmit; 
	
	// this map stored data of fields
	private List<Object> fields;

}
