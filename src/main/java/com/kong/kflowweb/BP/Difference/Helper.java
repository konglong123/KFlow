package com.kong.kflowweb.BP.Difference;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Helper {

	/**
	 * @return
	 * @throws IOException
	 */
	public static Properties loadResource() throws IOException {
		Properties properties = new Properties();
		ResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("jflow.properties");
		properties.load(resource.getInputStream());
		return properties;
	}
}
