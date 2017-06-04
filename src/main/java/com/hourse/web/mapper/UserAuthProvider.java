package com.hourse.web.mapper;

import com.hourse.web.model.UserAuth;
import com.hourse.web.util.SqlProviderUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class UserAuthProvider {

	public String getAuthByParentId(Map<String, Object> map){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM user_auth WHERE 1=1 ");
		if (StringUtils.isNotBlank(map.get("parentId").toString())) {
			sql.append(" AND parentId = #{parentId} ");
		}
		if (StringUtils.isNotBlank(map.get("authIds").toString())) {
			sql.append(" AND authId in ( " + map.get("authIds") + ")");
		}
		return sql.toString();
	}

	public String queryList(UserAuth UserAuth){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM User_Auth WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(UserAuth,"authId"));
		if(-1 != UserAuth.getAuthId()){
			sql.append(" and authId = " + UserAuth.getAuthId());
		}
		return sql.toString();
	}

	public String count(UserAuth UserAuth){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM User_Auth WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(UserAuth,"authId"));
		if(-1 != UserAuth.getAuthId()){
			sql.append(" and authId = " + UserAuth.getAuthId());
		}
		return sql.toString();
	}

	public String save(UserAuth UserAuth){
		return SqlProviderUtil.provideInsertNotBlankWithout(UserAuth, "User_Auth","authId");
	}

	public String update(UserAuth UserAuth){
		StringBuffer sql = new StringBuffer(" UPDATE User_Auth ");
		sql.append(SqlProviderUtil.provideSetterNotBlank(UserAuth));
		sql.append(" WHERE ");
		sql.append("authId=#{authId}");
		return sql.toString();
	}

	public String delete(String authIds){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from User_Auth ");
		sql.append(" where authId in ( "+ authIds + ")");
		return sql.toString();
	}
}
