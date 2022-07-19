package com.robindrew.spring.servlet;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.web.Bootstrap;
import com.robindrew.spring.component.indexlink.IndexLinkMap;
import com.robindrew.spring.component.servlet.template.AbstractTemplateServlet;
import com.robindrew.spring.component.servlet.template.TemplateResource;

/**
 * The index servlet handles the default url as well (designated by the url pattern empty string: "")
 */
@WebServlet(urlPatterns = { "/Index", "" })
@TemplateResource("templates/service/Index.html")
public class IndexServlet extends AbstractTemplateServlet {

	private final IndexLinkMap linkMap;

	public IndexServlet(IndexLinkMap linkMap) {
		this.linkMap = linkMap;
	}

	@PostConstruct
	private void setLinks() {
		linkMap.add("MBeans", "/BeanConsole", Bootstrap.COLOR_PRIMARY);
		linkMap.add("System", "/System", Bootstrap.COLOR_PRIMARY);
		linkMap.add("REST", "/swagger-ui/index.html", Bootstrap.COLOR_PRIMARY);
	}

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		dataMap.put("linkMap", linkMap);
	}

}
