package com.kuaizhan.form.model;

import java.util.Map;

import lombok.Data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Data
@Entity(value = "visits", noClassnameStored = true)
public class Visit {
	
	@Id
	private long id;
	
	@Property("site_id")
	private long siteId;
	
	@Property("date_visits")
	private Map<String, Integer> dateVisits;
}
