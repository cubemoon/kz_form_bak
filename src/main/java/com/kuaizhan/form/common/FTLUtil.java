package com.kuaizhan.form.common;

import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class FTLUtil {

	public static void addClass(String key, String className, Map<String, Object> args) {
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel cls;
		try {
			cls = (TemplateHashModel) staticModels.get(className);
		} catch (TemplateModelException e) {
			throw new RuntimeException(e);
		}
		args.put(key, cls);
	}

}
