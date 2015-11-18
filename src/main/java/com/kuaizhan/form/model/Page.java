package com.kuaizhan.form.model;

public class Page {

	private long id;
	private String title;

	public Page() {
		setId(-1);
		setTitle(null);
	}

	public Page(long id, String title) {
		setId(id);
		setTitle(title);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
