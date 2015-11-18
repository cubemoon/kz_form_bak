package com.kuaizhan.form.model;

public enum SiteStatus {

	offline(0), auditing(1), online(2), refused(3), deleted(4);

	private int value;

	private SiteStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
