package com.hourse.web.http;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class UserAgent {
	private static Properties props = new Properties();
	public static final String DEFAULT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";

	static {
		try {
			props.load(UserAgent.class.getClassLoader().getResourceAsStream(
					"UserAgent.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getRandom() {
		Random rnd = new Random();

		int num = 1000 + rnd.nextInt(5300);
		String key = "" + num;

		if (props.containsKey(key) == false) {
			return UserAgent.DEFAULT;
		}

		return props.getProperty(key);
	}
}
