package com.kuaizhan.form.server;

import spark.ModelAndView;

public class KZFreeMarkerSegmentEngine extends KZFreeMarkerEngine {

	private final static String PAGE = "<!DOCTYPE html><html><head></head><body>%s</body></html>";

	public KZFreeMarkerSegmentEngine(String templateDir) {
		super(templateDir);
	}

	@Override
	public String render(ModelAndView modelAndView) {
		String content = super.render(modelAndView);
		return String.format(PAGE, content);
	}

}
