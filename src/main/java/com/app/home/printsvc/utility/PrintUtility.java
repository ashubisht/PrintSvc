package com.app.home.printsvc.utility;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PrintUtility {
	
	public static String detectExtension(String fileName){
		String[] fileNameAndExt = fileName.split("\\.");
		//Linux compatibility
		if(fileNameAndExt.length==1){
			//No extension. Assume text file //Need to check later
			return "txt";
		}
		return fileNameAndExt[fileNameAndExt.length-1];
	}
	
	public static String decideApplication(String extension) throws Exception{
		Map<String, String> map = LoadProperties.getMapFromProperties("extToApp.properties");
		String application = map.getOrDefault(extension, "unknown");		
		return application;
	}
	
	public static void invokePrintCommand(String fileName, String application) throws Exception{
		Properties properties = LoadProperties.loadProperties("commandProperties.properties");
		List<String> commandList = buildCommandList(properties, application, fileName);
		InvokePrint printInvocation = new InvokePrint(commandList);
		printInvocation.start();	
	}

	private static List<String> buildCommandList(Properties properties, String application, String fileName) {
		return Arrays.asList(properties.getProperty(application).replace("<filename>", fileName).split("\\;"));
	}
	
}
