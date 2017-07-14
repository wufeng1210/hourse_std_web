package com.hourse.web.mapper;

import com.hourse.web.model.User;
import com.hourse.web.util.SqlProviderUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class UserProvider {

	public String queryList(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM user_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(user,"userId","roleId"));
		if(-1 != user.getUserId()  && 0 != user.getUserId()){
			sql.append(" and userId = " + user.getUserId());
		}
		if(0 != user.getRoleId()){
			sql.append(" and roleId = " + user.getRoleId());
		}
		return sql.toString();
	}

	public String count(User user){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM user_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(user,"userId","roleId"));
		if(-1 != user.getUserId()){
			sql.append(" and userId = " + user.getUserId());
		}
		if(0 != user.getRoleId()){
			sql.append(" and roleId = " + user.getRoleId());
		}
		return sql.toString();
	}

	public String save(User user){
		return SqlProviderUtil.provideInsertNotBlankWithout(user, "user_info","userId");
	}

	public String update(User user){
		StringBuffer sql = new StringBuffer(" UPDATE user_info ");
		sql.append(SqlProviderUtil.provideSetterNotBlank(user));
		sql.append(" WHERE ");
		sql.append("userId=#{userId}");
		return sql.toString();
	}

	public String delete(String userIds){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from user_info ");
		sql.append(" where userId in ( "+ userIds + ")");
		return sql.toString();
	}

}
