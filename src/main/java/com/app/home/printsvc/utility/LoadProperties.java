package com.app.home.printsvc.utility;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoadProperties {
	public static Properties loadProperties(String propertiesFile) throws Exception{
		Properties properties = new Properties();
		try{
			properties.load(new FileReader(new File(PrintUtility.class.getResource(propertiesFile).getFile())));
			return properties;
		}catch(Exception e){
			//Log the error
			throw new Exception("Error loading properties file: "+ propertiesFile + System.lineSeparator() + e.getMessage());
		}
	}
	
	public static Map<String, String> getMapFromProperties(String propertiesFile) throws Exception{
		Properties properties = loadProperties(propertiesFile);
		Map<String, String> map = new HashMap<String, String>();
		for(String propertyKey : properties.stringPropertyNames()){
			map.put(propertyKey, properties.getProperty(propertyKey));
		}
		return map;
	}
	
}
