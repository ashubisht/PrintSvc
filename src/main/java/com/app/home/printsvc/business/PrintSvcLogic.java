package com.app.home.printsvc.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.home.printsvc.constants.PrintSvcConstants;
import com.app.home.printsvc.dao.PrintSvcDAO;
import com.app.home.printsvc.utility.InvokePing;
import com.app.home.printsvc.utility.PrintUtility;
import com.app.vopackage.UserInfo;

public class PrintSvcLogic {
	
	@Autowired
	private PrintSvcDAO printSvcDAO;
	
	public static final String UPLOAD_FILE_SERVER = "G:/";
	
	public boolean validateUserName(String username, String password) throws Exception{
		try{
			UserInfo userInfo = printSvcDAO.getUserProfile(username);
			
			if(StringUtils.isNotBlank(userInfo.getPassword()) && StringUtils.isNotBlank(userInfo.getActivation_status())
					&& userInfo.getPassword().equals(password) && userInfo.getActivation_status().equalsIgnoreCase("Y")){
				return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			throw e;
		}
	}
	
	public void writeToFileServer(InputStream inputStream, String fileName) throws Exception{	 
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

	public Map<String, Object> generateFormProperties(List<Attachment> attachments) throws Exception {
		Map<String, Object> map = new HashMap<>();
		for(Attachment attachment : attachments){
			DataHandler dataHandler = attachment.getDataHandler();
			MultivaluedMap<String, String> multivaluedMap = attachment.getHeaders();
			parseFormProperties(multivaluedMap, map, dataHandler);
		}
		return map;
	}
	
	
	private void parseFormProperties(MultivaluedMap<String, String> multivaluedMap, Map<String, Object> formProperties, 
			DataHandler dataHandler) throws Exception{
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

	public void startPrint(String fileName) {
		try{
			String extension = PrintUtility.detectExtension(fileName);
			String application = PrintUtility.decideApplication(extension);
			String availability = InvokePing.runPingCommand();
			if(availability.equals(PrintSvcConstants.REACHABLE)){
				//Add to logs
				//Add to DB
				PrintUtility.invokePrintCommand(fileName, application);
			}else{
				//Add to logs
				//Add to DB
			}
		}catch(Exception e){
			//Log error
			//Send mail
		}
		
	}
	
	
	
}
