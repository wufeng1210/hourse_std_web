package com.hourse.web.datatype;

import java.util.Date;

/**
 * 功能说明: 当前日期，数据库保留类型<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author kongdy<br>
 * 开发时间: 2015年8月12日<br>
 */
public final class SysDate extends Date {

	/**
	 * <pre>
	 * 拼装sql的时候，
	 *   如果isNull为false，那么sql里面拼的是sysdate;
	 *   如果isNull为true，那么sql里面拼的是null
	 * 以支持向数据库Date类型字段设null的需求
	 * </pre>
	 */
	private boolean isNull = false;

	public SysDate() {
		this.isNull = false;
	}

	public SysDate(boolean isNull) {
		this.isNull = isNull;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

}
