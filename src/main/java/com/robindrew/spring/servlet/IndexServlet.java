package com.robindrew.spring.servlet;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.template.AbstractTemplateServlet;
import com.robindrew.common.http.servlet.template.TemplateResource;
import com.robindrew.common.web.Bootstrap;
import com.robindrew.spring.component.indexlink.IndexLinkMap;

/**
 * The index servlet handles the default url as well (designated by the url pattern empty string: "")
 */
@WebServlet(urlPatterns = { "/Index", "" })
@TemplateResource("site/Index.html")
public class IndexServlet extends AbstractTemplateServlet {

	@Autowired
	private IndexLinkMap linkMap;

	@PostConstruct
	private void setLinks() {
		linkMap.add("MBeans", "/BeanConsole", Bootstrap.COLOR_PRIMARY);
		linkMap.add("System", "/System", Bootstrap.COLOR_PRIMARY);
	}
	
	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		dataMap.put("linkMap", linkMap);
	}

}
