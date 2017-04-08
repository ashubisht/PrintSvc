package com.app.home.printsvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.app.vopackage.UserInfo;

public class PrintSvcDAOImpl implements PrintSvcDAO{

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public UserInfo getUserProfile(String username) throws Exception {
		
		String sqlQuery = "select password, activation_status from users_data where username = :username";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", username);
		
		UserInfo userInfo = namedParameterJdbcTemplate.query(sqlQuery, paramMap, new ResultSetExtractor<UserInfo>() {

			@Override
			public UserInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				UserInfo userInfo = new UserInfo();
				while(rs.next()){
					userInfo.setUsername(username);
					userInfo.setPassword(rs.getString("password"));
					userInfo.setActivation_status(rs.getString("activation_status"));
				}
				
				return userInfo;
			}
		});
		
		return userInfo;
		
	}

}
