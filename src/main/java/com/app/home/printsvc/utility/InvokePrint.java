package com.app.home.printsvc.utility;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class InvokePrint extends Thread{

	private List<String> commandList;

	public InvokePrint(List<String> commandList) {
		this.commandList = commandList;
	}

	public void run(){
		try{
			if(CollectionUtils.isNotEmpty(commandList)){
				ProcessBuilder processBuilder = new ProcessBuilder(commandList);
				Process process = processBuilder.start();
				process.waitFor();
				int value = process.exitValue();
				System.out.println(value); //Log the value
			}else{
				//throw empty commandlist exception
				System.out.println("Empty command list");
			}
		}catch(Exception e){
			//Log error
			//Send mail
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
