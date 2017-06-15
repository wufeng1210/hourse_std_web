package com.hourse.web.mapper;

import com.hourse.web.model.ActivityInfo;
import com.hourse.web.util.SqlProviderUtil;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class ActivityProvider {

	public String queryList(ActivityInfo ActivityInfo){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM activity_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(ActivityInfo,"activityId"));
		if(-1 != ActivityInfo.getActivityId()){
			sql.append(" and activityId = " + ActivityInfo.getActivityId());
		}
		return sql.toString();
	}

	public String count(ActivityInfo ActivityInfo){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM activity_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(ActivityInfo,"activityId"));
		if(-1 != ActivityInfo.getActivityId()){
			sql.append(" and activityId = " + ActivityInfo.getActivityId());
		}
		return sql.toString();
	}

	public String save(ActivityInfo ActivityInfo){
		return SqlProviderUtil.provideInsertNotBlankWithout(ActivityInfo, "activity_info","activityId");
	}

	public String update(ActivityInfo ActivityInfo){
		StringBuffer sql = new StringBuffer(" UPDATE activity_info ");
		sql.append(SqlProviderUtil.provideSetterNotBlank(ActivityInfo));
		sql.append(" WHERE ");
		sql.append("activityId=#{activityId}");
		return sql.toString();
	}

	public String delete(String ActivityIds){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from activity_info ");
		sql.append(" where activityId in ( "+ ActivityIds + ")");
		return sql.toString();
	}

}
