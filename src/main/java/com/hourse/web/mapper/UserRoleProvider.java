package com.hourse.web.mapper;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.util.SqlProviderUtil;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class UserRoleProvider {

	public String queryList(UserRole userRole){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM user_role WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(userRole,"roleId"));
		if(-1 != userRole.getRoleId()){
			sql.append(" and roleId = " + userRole.getRoleId());
		}
		return sql.toString();
	}

	public String count(UserRole userRole){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM user_role WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(userRole,"roleId"));
		if(-1 != userRole.getRoleId()){
			sql.append(" and roleId = " + userRole.getRoleId());
		}
		return sql.toString();
	}

	public String save(UserRole userRole){
		return SqlProviderUtil.provideInsertNotBlankWithout(userRole, "user_role","roleId");
	}

	public String update(UserRole userRole){
		StringBuffer sql = new StringBuffer(" UPDATE user_role ");
		sql.append(SqlProviderUtil.provideSetterNotBlank(userRole));
		sql.append(" WHERE ");
		sql.append("roleId=#{roleId}");
		return sql.toString();
	}

	public String delete(String roleIds){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from user_role ");
		sql.append(" where roleId in ( "+ roleIds + ")");
		return sql.toString();
	}

}
