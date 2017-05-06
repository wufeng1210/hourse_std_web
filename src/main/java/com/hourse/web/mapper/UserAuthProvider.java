package com.hourse.web.mapper;

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
}
