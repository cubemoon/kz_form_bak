package com.kuaizhan.form.model;

public enum Platform {
	Android(1), IOS(2);

	private int value;

	private Platform(int v) {
		value = v;
	}

	public int getValue() {
		return value;
	}
}
