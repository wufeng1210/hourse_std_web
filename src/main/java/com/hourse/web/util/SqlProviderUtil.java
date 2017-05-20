package com.hourse.web.util;

import com.hourse.web.datatype.SysDate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author kongdy<br>
 * 开发时间: 2015年8月5日<br>
 */
public class SqlProviderUtil {

	// private static Logger logger = LoggerFactory.getLogger(SqlProviderUtil.class);

	public static final String SEQUENCE_NEXTVAL = ".nextval";

	/**
	 * 按非空值组装查询条件
	 * @param obj
	 * @param columns 有效字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideConditionNotBlank(Object obj, String ... columns) {
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		StringBuffer buff = new StringBuffer("1=1");
		for (Entry<String, Object> entry : toMap(obj).entrySet()) {
			String k = entry.getKey();
			if (!ArrayUtils.isEmpty(columns) && !containsIgnoreCase(columns, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null && StringUtils.isNotBlank(String.valueOf(v))) {
				String columnName = k;
				if (aliasMap.containsKey(k)) {
					columnName = aliasMap.get(k);
				}
				buff.append(" and ").append(columnName).append("=").append("#{").append(k).append("}");
			}
		}
		return buff.toString();
	}
	public static String provideConditionByBranch(String field_alias,String branch_no, String isMainBranch, String en_branch_no) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isBlank(en_branch_no)) {
			sb.append("  AND 1=2 ");
		} else {
			if (StringUtils.isNotBlank(branch_no)) {
				sb.append("  AND ").append(field_alias).append(" in ('" + StringUtils.join(branch_no.split(","), "','") + "') ");
			} else {
				sb.append("  AND ( ").append(field_alias).append(" in ('" + StringUtils.join(en_branch_no.split(","), "','") + "') ");
				if (String.valueOf(Boolean.TRUE).equals(isMainBranch)) {
					sb.append("  or ").append(field_alias).append(" = ' ' or ").append(field_alias).append(" not in(select branch_no from crh_user.allbranch) ");
				}
				sb.append(" ) ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 营业部权限
	 * @param branch_no 营业部条件
	 * @param isMainBranch 主营业部
	 * @param en_branch_no 允许营业部
	 * @return
	 */
	public static String provideConditionByBranch(String branch_no, String isMainBranch, String en_branch_no) {
		return provideConditionByBranch("branch_no",branch_no,isMainBranch,en_branch_no);
	}

	/**
	 * 按非空值组装查询条件
	 * @param obj
	 * @param ignores 忽略字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideConditionNotBlankWithout(Object obj, String ... ignores) {
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		StringBuffer buff = new StringBuffer("1=1");
		for (Entry<String, Object> entry : toMap(obj).entrySet()) {
			String k = entry.getKey();
			if (containsIgnoreCase(ignores, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null && StringUtils.isNotBlank(String.valueOf(v))) {
				String columnName = k;
				if (aliasMap.containsKey(k)) {
					columnName = aliasMap.get(k);
				}
				buff.append(" and ").append(columnName).append("=").append("#{").append(k).append("}");
			}
		}
		return buff.toString();
	}

	/**
	 * 按非空值拼装insert 语句，支持sequence
	 * @param obj
	 * @param tableName
	 * @param columns 有效字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideInsertNotBlank(Object obj, String tableName, String ... columns) {
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		StringBuffer buff = new StringBuffer("insert into ");
		buff.append(tableName);
		List<String> list = new ArrayList<String>();
		Map<String, Object> objMap = toMap(obj);
		for (Entry<String, Object> entry : objMap.entrySet()) {
			String k = entry.getKey();
			if (!ArrayUtils.isEmpty(columns) && !containsIgnoreCase(columns, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null && StringUtils.isNotBlank(String.valueOf(v))) {
				list.add(k);
			}
		}
		buff.append("(");
		for (String col : list) {
			String columnName = col;
			if (aliasMap.containsKey(col)) {
				columnName = aliasMap.get(col);
			}
			buff.append(columnName).append(",");
		}
		buff.append(") values(");
		for (String col : list) {
			Object v = objMap.get(col);
			if (v instanceof SysDate) {
				// buff.append("sysdate,");
				dealSysDateParam4Insert((SysDate)v, buff);
			} else if (StringUtils.endsWithIgnoreCase(String.valueOf(v), SEQUENCE_NEXTVAL)) {
				buff.append(v).append(",");
			} else {
				buff.append("#{").append(col).append("}").append(",");
			}
		}
		buff.append(")");
		return buff.toString().replaceAll(",\\)", "\\)");
	}

	/**
	 * 按非空值拼装insert 语句，支持sequence
	 * @param obj
	 * @param tableName
	 * @param ignores 忽略字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideInsertNotBlankWithout(Object obj, String tableName, String ... ignores) {
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		StringBuffer buff = new StringBuffer("insert into ");
		buff.append(tableName);
		List<String> list = new ArrayList<String>();
		// Map<String, Object> objMap = toMap(obj);
		Map<String, Object> objMap = MapUtil.toMapIgnoreBlank(obj);
		for (Entry<String, Object> entry : objMap.entrySet()) {
			String k = entry.getKey();
			if (containsIgnoreCase(ignores, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null && StringUtils.isNotEmpty(String.valueOf(v))) {
				list.add(k);
			}
		}
		buff.append("(");
		for (String col : list) {
			String columnName = col;
			if (aliasMap.containsKey(col)) {
				columnName = aliasMap.get(col);
			}
			buff.append(columnName).append(",");
		}
		buff.append(") values(");
		for (String col : list) {
			Object v = objMap.get(col);
			if (v instanceof SysDate) {
				// buff.append("sysdate,");
				dealSysDateParam4Insert((SysDate)v, buff);
			} else if (StringUtils.endsWithIgnoreCase(String.valueOf(v), SEQUENCE_NEXTVAL)) {
				buff.append(v).append(",");
			} else {
				buff.append("#{").append(col).append("}").append(",");
			}
		}
		buff.append(")");
		return buff.toString().replaceAll(",\\)", "\\)");
	}

	/**
	 * 按非null值拼装update中的setter
	 * @param obj
	 * @param columns 有效字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideSetterNotNull(Object obj, String ... columns) {
		StringBuffer buff = new StringBuffer("set  ");
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		for (Entry<String, Object> entry : MapUtil.toMapIgnoreBlank(obj).entrySet()) {
			String k = entry.getKey();
			if (!ArrayUtils.isEmpty(columns) && !containsIgnoreCase(columns, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null) {
				if (v instanceof SysDate) {
					// buff.append(k).append("=sysdate, ");
					dealSysDateParam4Setter(k, (SysDate)v, buff);
				} else {
					String columnName = k;
					if (aliasMap.containsKey(k)) {
						columnName = aliasMap.get(k);
					}
					buff.append(columnName).append("=").append("#{").append(k).append("}, ");
				}
			}
		}
		return buff.substring(0, buff.length() - 2);
	}

	/**
	 * 按非空值拼装update中的setter
	 * @param obj
	 * @param columns 有效字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideSetterNotBlank(Object obj, String ... columns) {
		StringBuffer buff = new StringBuffer("set  ");
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		for (Entry<String, Object> entry : toMap(obj).entrySet()) {
			String k = entry.getKey();
			if (!ArrayUtils.isEmpty(columns) && !containsIgnoreCase(columns, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null && StringUtils.isNotBlank(String.valueOf(v))) {
				if (v instanceof SysDate) {
					// buff.append(k).append("=sysdate, ");
					dealSysDateParam4Setter(k, (SysDate)v, buff);
				} else {
					String columnName = k;
					if (aliasMap.containsKey(k)) {
						columnName = aliasMap.get(k);
					}
					buff.append(columnName).append("=").append("#{").append(k).append("}, ");
				}
			}
		}
		return buff.substring(0, buff.length() - 2);
	}

	/**
	 * 按非null值拼装update中的setter
	 * @param obj
	 * @param ignores 忽略字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideSetterNotNullWithout(Object obj, String ... ignores) {
		StringBuffer buff = new StringBuffer("set  ");
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		for (Entry<String, Object> entry : MapUtil.toMapIgnoreBlank(obj).entrySet()) {
			String k = entry.getKey();
			if (containsIgnoreCase(ignores, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null) {
				if (v instanceof SysDate) {
					// buff.append(k).append("=sysdate, ");
					dealSysDateParam4Setter(k, (SysDate)v, buff);
				} else {
					String columnName = k;
					if (aliasMap.containsKey(k)) {
						columnName = aliasMap.get(k);
					}
					buff.append(columnName).append("=").append("#{").append(k).append("}, ");
				}
			}
		}
		return buff.substring(0, buff.length() - 2);
	}

	/**
	 * 按非空值拼装update中的setter
	 * @param obj
	 * @param ignores 忽略字段，传入时筛选，不区分大小写
	 * @return
	 */
	public static String provideSetterNotBlankWithout(Object obj, String ... ignores) {
		StringBuffer buff = new StringBuffer("set  ");
		Map<String, String> aliasMap = MapUtil.getFiledAlias(obj);
		for (Entry<String, Object> entry : toMap(obj).entrySet()) {
			String k = entry.getKey();
			if (containsIgnoreCase(ignores, k)) {
				continue;
			}
			Object v = entry.getValue();
			if (v != null && StringUtils.isNotBlank(String.valueOf(v))) {
				if (v instanceof SysDate) {
					// buff.append(k).append("=sysdate, ");
					dealSysDateParam4Setter(k, (SysDate)v, buff);
				} else {
					String columnName = k;
					if (aliasMap.containsKey(k)) {
						columnName = aliasMap.get(k);
					}
					buff.append(columnName).append("=").append("#{").append(k).append("}, ");
				}
			}
		}
		return buff.substring(0, buff.length() - 2);
	}

	public static String getPaginationSql(String querySql) {
		return "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + querySql + ") A WHERE ROWNUM <= #{end})WHERE RN > #{start}";
	}

	public static String getCountSql(String querySql) {
		return "SELECT count(1) FROM (" + querySql + ")";
	}

	/**
	 * 判断包含关系，不区分大小写
	 * @param array
	 * @param value
	 * @return
	 */
	private static boolean containsIgnoreCase(String[] array, String value) {
		if (array == null || array.length == 0) {
			return false;
		}
		for (String v : array) {
			if (StringUtils.equalsIgnoreCase(v, value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 把对象转成Map<String,Object> <br/>
	 * 这里不能直接用MapUtil.toMap(obj)来转，因为toMap支持别名，得到的map中有别名的会有2个字段和值，这就比数据库表中的字段多
	 * @author yejg
	 * @param obj
	 * @return
	 */
	private static Map<String, Object> toMap(Object obj) {
		return MapUtil.toMapIgnoreAlias(obj);
	}

	private static void dealSysDateParam4Setter(String k, SysDate v, StringBuffer buff) {
		// 日期类型特殊处理
		if (v.isNull()) {
			buff.append(k).append("=null, ");
		} else {
			buff.append(k).append("=sysdate, ");
		}
	}

	private static void dealSysDateParam4Insert(SysDate v, StringBuffer buff) {
		// 日期类型特殊处理
		if (v.isNull()) {
			buff.append("null,");
		} else {
			buff.append("sysdate,");
		}
	}
}
