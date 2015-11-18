package com.kuaizhan.form.server;

import spark.ResponseTransformer;

import com.kuaizhan.form.common.JacksonUtils;

public class JacksonTransformer implements ResponseTransformer {

	@Override
	public String render(Object model) throws Exception {
		return JacksonUtils.toJson(model);
	}

}
