package com.hourse.web.mapper;

import com.hourse.web.model.Hourse;
import com.hourse.web.util.SqlProviderUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 功能说明: 查询sql拼装类<br>
 */
public class HourseProvider {

	public String queryList(Hourse Hourse){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM hourse_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(Hourse,"hourseId","hourseAddr","userId","hallNum","toiletNum","roomNum","monthly","kitchenNum","packingingLot"));
		if(-1 != Hourse.getHourseId() && 0 != Hourse.getHourseId()){
			sql.append(" and hourseId = " + Hourse.getHourseId());
		}
		if(-1 != Hourse.getUserId() && 0 != Hourse.getUserId()){
			sql.append(" and userId = " + Hourse.getUserId());
		}
		if(null !=Hourse.getHourseAddr()&&!StringUtils.equals("-1",Hourse.getHourseAddr())){
			sql.append(" and hourseAddr like '%" + Hourse.getHourseAddr() + "%'");
		}
		return sql.toString();
	}

	public String count(Hourse Hourse){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(*) ");
		sql.append(" FROM hourse_info WHERE ");
		sql.append(SqlProviderUtil.provideConditionNotBlankWithout(Hourse,"hourseId","hourseAddr","userId","hallNum","toiletNum","roomNum","monthly","kitchenNum","packingingLot"));
		if(-1 != Hourse.getHourseId() && 0 != Hourse.getHourseId()){
			sql.append(" and hourseId = " + Hourse.getHourseId());
		}
		if(-1 != Hourse.getUserId() && 0 != Hourse.getUserId()){
			sql.append(" and userId = " + Hourse.getUserId());
		}
		if(null !=Hourse.getHourseAddr()&&!StringUtils.equals("-1",Hourse.getHourseAddr())){
			sql.append(" and hourseAddr like '%" + Hourse.getHourseAddr() + "%'");
		}
		return sql.toString();
	}

	public String save(Hourse Hourse){
		return SqlProviderUtil.provideInsertNotBlankWithout(Hourse, "hourse_info","hourseId");
	}

	public String update(Hourse Hourse){
		StringBuffer sql = new StringBuffer(" UPDATE hourse_info ");
		sql.append(SqlProviderUtil.provideSetterNotBlankWithout(Hourse, "hourse_info","hourseId","userId"));
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
