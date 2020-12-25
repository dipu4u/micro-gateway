package io.axiom.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
	
	private final static Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

	private static ApplicationProperties APPLICATION_PROPERTIES = new ApplicationProperties();
	
	private Properties properties = new Properties();
	
	private ApplicationProperties() {
		readConfig();
	}
	
	public static ApplicationProperties getInstance() {
		return APPLICATION_PROPERTIES;
	}
	
	private void readConfig() {
		try (InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("bootstrap.properties")) {
			properties = new Properties();
			properties.load(inStream);
			log.info("Application Config Read Complete");
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public String getProperty(final String key) {
		return properties.getProperty(key);
	}
	
}
