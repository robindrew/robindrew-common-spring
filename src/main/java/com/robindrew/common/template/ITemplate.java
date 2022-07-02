package com.robindrew.common.template;

import java.io.Writer;
import java.util.Map;

public interface ITemplate {

	String getName();

	ITemplateLocator getLocator();

	String execute(Map<String, ?> map);

	String execute(ITemplateData data);

	void execute(ITemplateData data, Writer writer);

}
