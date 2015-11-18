package com.kuaizhan.form.controller;

import spark.Request;
import spark.Response;
import spark.Route;

public class HealthCheckHandler implements Route {

	@Override
	public Object handle(Request request, Response response) {
		return "WORKING";
	}

}
