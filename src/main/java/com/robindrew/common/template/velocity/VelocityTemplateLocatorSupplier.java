package com.robindrew.common.template.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.google.common.base.Supplier;
import com.robindrew.common.template.ITemplateLocator;

public class VelocityTemplateLocatorSupplier implements Supplier<ITemplateLocator> {

	private String path = null;
	private boolean caching = true;
	private String extension = "";
	private boolean strict = false;

	public String getPath() {
		return path;
	}

	public boolean isCaching() {
		return caching;
	}

	public String getExtension() {
		return extension;
	}

	public String getFileResourceLoaderPath() {
		return path;
	}

	public boolean isFileResourceLoaderCaching() {
		return caching;
	}

	public VelocityTemplateLocatorSupplier setExtension(String extension) {
		if (extension.isEmpty()) {
			throw new IllegalArgumentException("extension is empty");
		}
		this.extension = extension;
		return this;
	}

	public VelocityTemplateLocatorSupplier setPath(String path) {
		if (path.isEmpty()) {
			throw new IllegalArgumentException("path is empty");
		}
		this.path = path;
		return this;
	}

	public VelocityTemplateLocatorSupplier setCaching(boolean caching) {
		this.caching = caching;
		return this;
	}

	@Override
	public ITemplateLocator get() {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		engine.setProperty("class.resource.loader.class", VelocityResourceLoader.class.getName());
		if (path != null) {
			engine.setProperty("class.resource.loader.path", path);
		}
		engine.setProperty("class.resource.loader.caching", caching);
		if (strict) {
			engine.setProperty("runtime.references.strict", strict);
		}
		if (!extension.isEmpty()) {
			engine.setProperty("class.resource.loader.extension", extension);
		}
		engine.setProperty("velocimacro.library", "");
		engine.init();
		return new VelocityTemplateLocator(engine);
	}

}
