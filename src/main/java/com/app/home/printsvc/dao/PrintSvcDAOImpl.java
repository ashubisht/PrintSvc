package com.app.home.printsvc.dao;

public class PrintSvcDAOImpl implements PrintSvcDAO{

	@Override
	public boolean validateUserName(String username, String password) {
		if(username.trim().equalsIgnoreCase("username") &&
				password.trim().equalsIgnoreCase("password")){
			return true;
		}else{
			return false;
		}
	}

}
