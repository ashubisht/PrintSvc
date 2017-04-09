package com.app.home.printsvc.utility;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PrintUtility {
	
	public static String detectExtension(String fileName){
		String[] fileNameAndExt = fileName.split("\\.");
		return fileNameAndExt[fileNameAndExt.length-1];
	}
	
	public static String decideApplication(String extension){
		String application = null;
		switch (extension) {
		case "odt":
			application = "LibreOffice";
			break;
		case "pdf":
			application = "xpdf";
			break;
		case "doc":
		case "docx":
		case "ppt":
		case "pptx":
		case "xls":
		case "xlsx":
			application = "msoffice";
			break;
		case "bmp":
		case "jpg":
		case "jpeg":
		case "png":
			application = "paint";
			break;
		default:
			application = "unknown";
			break;
		}
		
		return application;
		
	}
	
	public static void invokePrintCommand(String extension){
		
		List<String> commandList = new ArrayList<String>();
		Properties properties = new Properties();
		
		try{
			properties.load(new FileReader(new File(PrintUtility.class.getResource("commandProperties.properties").getFile())));
		}catch(Exception e){
			
		}
		
		commandList = buildCommandList(properties, extension);
		
		try{
			ProcessBuilder processBuilder = new ProcessBuilder(commandList);
			Process process = processBuilder.start();
			int value = process.exitValue();
			System.out.println(value); //Log the value
		}catch(Exception e){
			//Log error
			//Send mail
		}
		
	}

	private static List<String> buildCommandList(Properties properties, String extension) {
		return Arrays.asList(properties.getProperty(extension).split("\\;"));
	}
	
}
