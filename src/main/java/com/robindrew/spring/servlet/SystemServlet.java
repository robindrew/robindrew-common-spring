package com.robindrew.spring.servlet;

import static com.robindrew.spring.stats.StatsComponent.JAVA_HEAP_FREE;
import static com.robindrew.spring.stats.StatsComponent.JAVA_HEAP_USED;
import static com.robindrew.spring.stats.StatsComponent.SYSTEM_MEMORY_FREE;
import static com.robindrew.spring.stats.StatsComponent.SYSTEM_MEMORY_USED;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.robindrew.common.base.Java;
import com.robindrew.common.base.Strings;
import com.robindrew.common.base.SystemProperties;
import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.template.AbstractTemplateServlet;
import com.robindrew.spring.service.ServiceDefinition;
import com.robindrew.spring.servlet.system.FileRootView;
import com.robindrew.spring.servlet.system.MemoryStats;
import com.robindrew.spring.servlet.system.NetworkInterfaceView;
import com.robindrew.spring.servlet.system.ThreadView;
import com.robindrew.spring.stats.IStatsCache;
import com.robindrew.spring.stats.StatsInstantSet;

@WebServlet(urlPatterns = "/system")
public class SystemServlet extends AbstractTemplateServlet {

	private static final Logger log = LoggerFactory.getLogger(SystemServlet.class);

	@Autowired
	private ServiceDefinition service;
	@Autowired
	private IStatsCache statsCache;

	@Override
	public String getTemplateName() {
		return "site/System.html";
	}

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

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
		dataMap.put("javaMaxHeapMemory", Strings.bytes(Java.maxMemory()));
		dataMap.put("javaUsedHeapMemory", Strings.bytes(Java.usedMemory()));
		dataMap.put("javaPercentHeapMemory", Strings.percent(Java.usedMemory(), Java.maxMemory()));

		dataMap.put("javaMaxDirectMemory", getJavaMaxDirectMemory());
		dataMap.put("javaUsedDirectMemory", getJavaUsedDirectMemory());
		dataMap.put("javaPercentDirectMemory", getJavaPercentDirectMemory());

		dataMap.put("javaMainClass", SystemProperties.getJavaCommand());
		dataMap.put("javaTimezone", SystemProperties.getTimeZone());
		dataMap.put("javaVersion", SystemProperties.getJavaVersion());
		dataMap.put("javaFileEncoding", SystemProperties.getFileEncoding());
		dataMap.put("javaWorkingDirectory", SystemProperties.getWorkingDirectory());

		dataMap.put("operatingSystem", SystemProperties.getOperatingSystem());
		dataMap.put("systemMaxMemory", Strings.bytes(Java.getSystemMaxMemory(1)));
		dataMap.put("systemUsedMemory", Strings.bytes(Java.getSystemUsedMemory(1)));
		dataMap.put("systemPercentMemory", getSystemPercentMemory());

		dataMap.put("systemProperties", toMap(System.getProperties()));
		dataMap.put("envProperties", toMap(System.getenv()));

		// Charts
		dataMap.put("pieChartJavaMemoryUsed", Java.usedMemory() / (1024 * 1024));
		dataMap.put("pieChartJavaMemoryFree", Java.freeMemory() / (1024 * 1024));

		dataMap.put("pieChartSystemMemoryUsed", Java.getSystemUsedMemory(1) / (1024 * 1024));
		dataMap.put("pieChartSystemMemoryFree", Java.getSystemFreeMemory(1) / (1024 * 1024));

		setJavaHeapStats(statsCache, dataMap);
		setSystemMemoryStats(statsCache, dataMap);

		setNetworkInterfaces(dataMap);

		setThreads(dataMap);

		setFileRoots(dataMap);
	}

	private void setFileRoots(Map<String, Object> dataMap) {
		Set<FileRootView> views = new TreeSet<>();
		FileSystem fileSystem = FileSystems.getDefault();
		for (Path root : fileSystem.getRootDirectories()) {
			views.add(new FileRootView(root));
		}
		dataMap.put("fileRoots", views);
	}

	private void setThreads(Map<String, Object> dataMap) {
		Set<ThreadView> views = new TreeSet<>();
		Map<Thread, StackTraceElement[]> threadToTraceMap = Thread.getAllStackTraces();
		for (Entry<Thread, StackTraceElement[]> entry : threadToTraceMap.entrySet()) {
			Thread thread = entry.getKey();
			StackTraceElement[] elements = entry.getValue();
			views.add(new ThreadView(thread, elements));
		}
		dataMap.put("threads", views);
	}

	private void setNetworkInterfaces(Map<String, Object> dataMap) {
		List<NetworkInterfaceView> views = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface network : Collections.list(nets)) {
				NetworkInterfaceView view = new NetworkInterfaceView(network);
				if (view.getAddressList().isEmpty()) {
					continue;
				}
				views.add(view);
			}
		} catch (Exception e) {
			log.info("Unable to list network interfaces", e);
		}
		dataMap.put("networks", views);
	}

	private void setJavaHeapStats(IStatsCache stats, Map<String, Object> dataMap) {
		StatsInstantSet used = new StatsInstantSet(stats.getStats(JAVA_HEAP_USED));
		StatsInstantSet free = new StatsInstantSet(stats.getStats(JAVA_HEAP_FREE));
		dataMap.put("javaHeapStats", MemoryStats.toSet(used, free));
	}

	private void setSystemMemoryStats(IStatsCache stats, Map<String, Object> dataMap) {
		StatsInstantSet used = new StatsInstantSet(stats.getStats(SYSTEM_MEMORY_USED));
		StatsInstantSet free = new StatsInstantSet(stats.getStats(SYSTEM_MEMORY_FREE));
		dataMap.put("systemMemoryStats", MemoryStats.toSet(used, free));
	}

	private String getJavaMaxDirectMemory() {
		try {
			for (BufferPoolMXBean bean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
				if (bean.getName().equals("direct")) {
					return Strings.bytes(bean.getTotalCapacity());
				}
			}
		} catch (Exception e) {
		}
		return "not available";
	}

	private String getJavaUsedDirectMemory() {
		try {
			for (BufferPoolMXBean bean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
				if (bean.getName().equals("direct")) {
					return Strings.bytes(bean.getMemoryUsed());
				}
			}
		} catch (Exception e) {
		}
		return "not available";
	}

	private String getJavaPercentDirectMemory() {
		try {
			for (BufferPoolMXBean bean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
				if (bean.getName().equals("direct")) {
					long used = bean.getMemoryUsed();
					long total = bean.getTotalCapacity();
					return Strings.percent(used, total);
				}
			}
		} catch (Exception e) {
		}
		return "not available";
	}

	private String getSystemPercentMemory() {
		long free = Java.getSystemFreeMemory(1);
		long total = Java.getSystemMaxMemory(1);
		return Strings.percent(total - free, total);
	}

	private Object toMap(Map<?, ?> map) {
		String separator = System.getProperty("path.separator");
		TreeMap<String, String> newMap = new TreeMap<>();
		for (Entry<?, ?> entry : map.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			if (key.toLowerCase().endsWith("path")) {
				value = value.replace(separator, "<br>");
			}
			newMap.put(key, value);
		}
		return newMap;
	}

}
