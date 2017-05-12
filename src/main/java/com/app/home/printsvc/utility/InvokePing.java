package com.app.home.printsvc.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.app.home.printsvc.constants.PrintSvcConstants;

public class InvokePing {
	
	public static String runPingCommand() throws IOException, InterruptedException{
		List<String> commandList = Arrays.asList("cmd.exe", "/C", "ping "+ PrintSvcConstants.PRINTER_IP_ADDRESS);
		ProcessBuilder processBuilder = new ProcessBuilder(commandList);
		Process process = processBuilder.start();
		BufferedReader iReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String tempStr= "";
		StringBuffer buffer = new StringBuffer();
		while((tempStr = iReader.readLine())!=null){
			buffer.append(tempStr+System.lineSeparator());
		}
		process.waitFor();
		int exitValue = process.exitValue();
		System.out.println(exitValue); //Log exitValue
		String returnString = buffer.toString();
		char packetLoss = returnString.charAt(returnString.indexOf("Lost = ")+ 7);
		int packetLossNumber = Integer.parseInt(packetLoss+"");
		if(packetLossNumber>=4){
			return PrintSvcConstants.UNREACHABLE;
		}else{
			return PrintSvcConstants.REACHABLE;
		}
	}
	
}
