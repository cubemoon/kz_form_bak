package com.kuaizhan.form.server;

import java.util.List;

public class KZJsonpTransformerObject {

	private String object;
	private String method;
	private List<String> arguments;
	private boolean nested;

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
	
	public boolean isNested() {
		return nested;
	}
	
	public void setNested(boolean nested) {
		this.nested = nested;
	}

}
