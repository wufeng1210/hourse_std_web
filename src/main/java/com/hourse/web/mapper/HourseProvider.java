package com.hourse.web.mapper;

import com.hourse.web.model.Hourse;
import com.hourse.web.util.SqlProviderUtil;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class HourseProvider {

	public String queryList(Hourse Hourse){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM hourse_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(Hourse,"hourseId","userId"));
		if(-1 != Hourse.getHourseId()){
			sql.append(" and hourseId = " + Hourse.getHourseId());
		}
		if(0 != Hourse.getUserId()){
			sql.append(" and userId = " + Hourse.getUserId());
		}
		return sql.toString();
	}

	public String count(Hourse Hourse){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM hourse_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(Hourse,"hourseId"));
		if(-1 != Hourse.getHourseId()){
			sql.append(" and hourseId = " + Hourse.getHourseId());
		}
		return sql.toString();
	}

	public String save(Hourse Hourse){
		return SqlProviderUtil.provideInsertNotBlankWithout(Hourse, "hourse_info","hourseId");
	}

	public String update(Hourse Hourse){
		StringBuffer sql = new StringBuffer(" UPDATE hourse_info ");
		sql.append(SqlProviderUtil.provideSetterNotBlank(Hourse));
		sql.append(" WHERE ");
		sql.append("hourseId=#{hourseId}");
		return sql.toString();
	}

	public String delete(String hourseIds){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from hourse_info ");
		sql.append(" where hourseId in ( "+ hourseIds + ")");
		return sql.toString();
	}

}
