package com.robindrew.spring.component.servlet.template;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.robindrew.common.base.Java;
import com.robindrew.common.text.Strings;
import com.robindrew.spring.component.service.ServiceDefinition;

@Component
public class VelocityDataMapper {

	@Autowired
	private ServiceDefinition service;

	public void populate(Map<String, Object> dataMap) {

		dataMap.put("title", service.getName() + " #" + service.getInstance());

		dataMap.put("serviceName", service.getName());
		dataMap.put("serviceInstance", service.getInstance());
		dataMap.put("serviceEnv", service.getEnv());
		dataMap.put("servicePort", service.getPort());

		dataMap.put("systemHost", Java.getHostName());
		dataMap.put("systemAddress", Java.getHostAddress());
		dataMap.put("systemTime", Strings.date(Java.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

		dataMap.put("processId", Java.getProcessId());

		dataMap.put("javaStartTime", Strings.date(Java.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		dataMap.put("javaUptime", Strings.time(Java.getUptime()));
		dataMap.put("javaMaxMemory", Strings.bytes(Java.maxMemory()));
		dataMap.put("javaUsedMemory", Strings.bytes(Java.usedMemory()));
		dataMap.put("javaPercentMemory", Strings.percent(Java.usedMemory(), Java.maxMemory()));
	}

	public ModelMap newModelMap() {
		ModelMap map = new ModelMap();
		populate(map);
		return map;
	}

}
