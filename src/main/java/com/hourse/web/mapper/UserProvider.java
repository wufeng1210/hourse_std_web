package com.hourse.web.mapper;

import com.hourse.web.model.User;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class UserProvider {

	public String save(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into user_info values(null,");
		sql.append("22,33,44,55,66,77,88,99");
		sql.append(" ) ");

		return sql.toString();
	}

	public String update(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" update user_info set");
		sql.append(" userName = "+ user.getUserName());
		sql.append(" where userId = "+ user.getUserId());

		return sql.toString();
	}

	public String delete(String userIds){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from user_info ");
		sql.append(" where userId in ( "+ userIds + ")");
		return sql.toString();
	}

}
