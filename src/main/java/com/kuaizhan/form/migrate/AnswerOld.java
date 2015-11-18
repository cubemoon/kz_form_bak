package com.kuaizhan.form.migrate;

import java.util.List;

import lombok.Data;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value="answers", noClassnameStored=true)
@Data
public class AnswerOld {

	@Id
	private long id;
	private String title;
	private long form_id;
	private String ctime;
	private long site_id;
	private String status;
	
	@Embedded("items")
	private List<Item> items;
	
	@Data
	@Embedded
	public static class Item{
		private String title;
		private String anwser;
		private String is_multi;
		private String is_required;
	}
}
