package com.hourse.web.http;

public class HttpConstant {
	public static final int ACCEPT = 0x01;

	public static final int ACCEPT_ENCODING = 0x02;

	public static final int ACCEPT_LANGUAGE = 0x04;

	public static final int USER_AGENT = 0x08;

	public static final int ALL = (ACCEPT | ACCEPT_ENCODING | ACCEPT_LANGUAGE
			| USER_AGENT);
}
