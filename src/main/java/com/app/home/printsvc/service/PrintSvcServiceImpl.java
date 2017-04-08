package com.app.home.printsvc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.home.printsvc.dao.PrintSvcDAO;

import ch.qos.logback.core.status.Status;

@Service("fileService")
public class PrintSvcServiceImpl implements PrintSvcService {

	//Note, this returns success even if exception comes
	
	@Autowired
	private PrintSvcDAO printSvcDao;
	
	public static final String UPLOAD_FILE_SERVER = "/home/ashu/Documents/remoteUploaded/";
	
	@Override
	public Response uploadFile(List<Attachment> attachments, HttpServletRequest request) {
		
		String fileName = null;
		String username = null;
		String password= null;
		
		Map<String, Object> formProperties = new HashMap<>();
		
		
		try{
			for(Attachment attachment : attachments){
				DataHandler dataHandler = attachment.getDataHandler();
				MultivaluedMap<String, String> multivaluedMap = attachment.getHeaders();
				parseProperties(multivaluedMap, formProperties, dataHandler);
			}
			username = (String)formProperties.get("username");
			password = (String)formProperties.get("password");
			fileName = (String)formProperties.get("fileName");
			boolean userValidated = false;
			try{
				userValidated = printSvcDao.validateUserName(username, password);
			}catch(Exception e){
				return Response.ok("Internal Error").build();
			}
			if(!userValidated){
				return Response.ok("Invalid user").build();
			}
			InputStream inputStream = (InputStream)formProperties.get("uploadedFile");
            writeToFileServer(inputStream, fileName);
			return Response.ok("Upload successful").build();
		}catch(Exception e){
			//Log the error
			System.out.println(e.getMessage());
			e.printStackTrace();
			return Response.ok("Unable to upload the file. Please try again later or contact admin").build();
		}finally{
			System.out.println("process finished");
		}
		
	}

	private void parseProperties(MultivaluedMap<String, String> multivaluedMap, Map<String, Object> formProperties, DataHandler dataHandler) throws Exception{
		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");
		for (String contentMetaData : contentDisposition) {			 
            if (contentMetaData.trim().startsWith("filename")) {
                String[] name = contentMetaData.split("=");
                String exactFileName = name[1].trim().replaceAll("\"", "");
                formProperties.put("fileName", exactFileName);
            }else if(contentMetaData.trim().startsWith("name")){
            	String[] name = contentMetaData.split("=");
            	String exactName = name[1].trim().replaceAll("\"", "");
            	if(exactName.equals("uploadedFile")){
            		formProperties.put(exactName, dataHandler.getInputStream());
            	}else{
            		formProperties.put(exactName, IOUtils.readStringFromStream(dataHandler.getInputStream()));
            	}
            }
        }
	}
	
	private void writeToFileServer(InputStream inputStream, String fileName) throws Exception{	 
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(UPLOAD_FILE_SERVER + fileName));
            int read = 0;
            byte[] bytes = new byte[1024]; //Check, original was 1024
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }
        finally{
            //release resource, if any
        }
    }
}
