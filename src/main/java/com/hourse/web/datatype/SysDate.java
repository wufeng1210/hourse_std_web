package com.hourse.web.datatype;

import java.util.Date;

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
