package com.app.home.printsvc.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.home.printsvc.business.PrintSvcLogic;

@Service("fileService")
public class PrintSvcServiceImpl implements PrintSvcService {

	@Autowired
	private PrintSvcLogic printSvcLogic;
	
	
	@Override
	public Response uploadFile(List<Attachment> attachments, HttpServletRequest request) {
		
		String fileName = null;
		String username = null;
		String password= null;
		
		try{
			Map<String, Object> formProperties = printSvcLogic.generateFormProperties(attachments);
			username = (String)formProperties.get("username");
			password = (String)formProperties.get("password");
			fileName = (String)formProperties.get("fileName");
			boolean userValidated = false;
			
			userValidated = printSvcLogic.validateUserName(username, password);
			
			if(!userValidated){
				return Response.ok("Invalid user").build();
			}
			InputStream inputStream = (InputStream)formProperties.get("uploadedFile");
			
            printSvcLogic.writeToFileServer(inputStream, fileName);
            
			return Response.ok("Upload successful").build();
		}catch(Exception e){
			//Log the error
			System.out.println(e.getMessage());
			e.printStackTrace();
			return Response.ok("Internal error. Unable to upload the file. Please try again later or contact admin").build();
		}finally{
			System.out.println("process finished");
		}
		
	}
	
}
