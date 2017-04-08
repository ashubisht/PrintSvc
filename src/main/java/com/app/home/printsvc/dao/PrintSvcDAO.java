package com.app.home.printsvc.dao;

import com.app.vopackage.UserInfo;

public interface PrintSvcDAO {

	public UserInfo getUserProfile(String username) throws Exception;

}
