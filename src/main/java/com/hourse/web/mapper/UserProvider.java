package com.hourse.web.mapper;

import com.hourse.web.model.User;
import org.apache.commons.lang3.StringUtils;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class UserProvider {

	public String queryList(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from user_info where 1=1 ");
		if(-1 != user.getUserId()){
			sql.append(" and userId = " + user.getUserId());
		}
		if(StringUtils.isNotBlank(user.getUserName())){
			sql.append(" and userName = " + user.getUserName());
		}
		if(StringUtils.isNotBlank(user.getUserType())){
			sql.append(" and userType = " + user.getUserType());
		}
		return sql.toString();
	}

	public String count(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from user_info where 1=1 ");
		if(-1 != user.getUserId()){
			sql.append(" and userId = " + user.getUserId());
		}
		if(StringUtils.isNotBlank(user.getUserName())){
			sql.append(" and userName = " + user.getUserName());
		}
		if(StringUtils.isNotBlank(user.getUserType())){
			sql.append(" and userType = " + user.getUserType());
		}
		return sql.toString();
	}

	public String save(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into user_info values(null");
		sql.append(",'" + user.getUserName()+"'");
		sql.append(",'" + user.getUserPassWord()+"'");
		sql.append(",'','','1',null,null,null ) ");

		return sql.toString();
	}

	public String update(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" update user_info set");
		if(StringUtils.isNotBlank(String.valueOf(user.getUserId()))){
			sql.append(" userId = " + user.getUserId());
		}
		if(StringUtils.isNotBlank(user.getUserName())){
			sql.append(" , ");
			sql.append(" userName = " + user.getUserName());
		}

		if(StringUtils.isNotBlank(user.getUserPassWord())){
			sql.append(" , ");
			sql.append(" userPassWord = " + user.getUserPassWord());
		}
		if(StringUtils.isNotBlank(user.getUserType())){
			sql.append(" , ");
			sql.append(" userType = " + user.getUserType());
		}

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
