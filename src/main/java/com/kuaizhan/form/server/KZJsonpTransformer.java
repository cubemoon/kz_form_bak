package com.kuaizhan.form.server;

import spark.ResponseTransformer;

import com.kuaizhan.form.common.StringUtil;

public class KZJsonpTransformer implements ResponseTransformer {

	private final static String TEMPLATE = "<script type=\"text/javascript\">%s</script>";

	@Override
	public String render(Object model) throws Exception {
		KZJsonpTransformerObject t = (KZJsonpTransformerObject) model;
		String call = String.format("%s.%s(%s);", t.getObject(), t.getMethod(),
				StringUtil.join(", ", t.getArguments()));
		return t.isNested() ? String.format(TEMPLATE, call) : call;
	}

}
