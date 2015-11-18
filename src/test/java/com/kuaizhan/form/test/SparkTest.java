package com.kuaizhan.form.test;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;

public class SparkTest {

	public static void main(String[] args) {
		Spark.port(8080);
		Spark.get("/", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				Spark.halt("OK");
				return null;
			}
		}, new ResponseTransformer() {
			@Override
			public String render(Object model) throws Exception {
				System.out.println("here");
				return String.format("***--%s--***\n", model);
			}
		});
	}
}
