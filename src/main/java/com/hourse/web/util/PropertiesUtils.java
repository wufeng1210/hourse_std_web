package com.hourse.web.util;

import freemarker.template.utility.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtils {

	private static Properties prop;

	private static List<String> fileList;
	
	public static String[] pwdNameList = {"ifs_operator_pwd","livebos_pwd","zl_trust_store_pwd","hsipccweb_privatekey","sisap_identifier_pwd","sms_user_pwd","ca_beijingca_keyPassword"};

	private static Logger logger = Logger.getLogger(PropertiesUtils.class);

	public static void clear() {
		prop = null;
	}

	public static Properties getProp() {
		if (prop == null) {
			prop = new Properties();

			if (fileList != null) {
				for (String filename : fileList) {
					InputStream configIs = null;
					try {
						configIs = new FileInputStream(EnvironmentUtils.getFileAbsolutePath(filename));
						prop.load(configIs);

					} catch (IOException e) {
						logger.error(String.format("未找到配置文件[%s]", filename),e);
					} finally {
						if (configIs != null) {
							try {
								configIs.close();
							} catch (IOException e) {
								logger.error("关闭文件流错误", e);
							}
						}
					}
				}
			}

		}
		return prop;
	}

	public static String get(String key) {
		String value = "";
		if (getProp().containsKey(key)) value = getProp().getProperty(key);
		if(value == null || value.toLowerCase().equals("null") || (value.startsWith("${") && value.endsWith("}"))){
			value = "";
		}
		if(isPassword(key,value)&&value.matches("^[A-F0-9]+$") && value.length()%16==0){
			return TriDES.decode(value);
		}
		return value;
	}

	public static String get(String key, String defaultValue) {
		String value = getProp().getProperty(key, defaultValue);

		/*if(null==value || value.toLowerCase().equals("null") || StringUtil.isBlank(value)
				|| (value.startsWith("${") && value.endsWith("}"))){*/
		if(value.equals("NULL") || value.equals("null")
				|| (value.startsWith("${") && value.endsWith("}"))){
			value = defaultValue;
		}
		if(isPassword(key,value)&&value.matches("^[A-F0-9]+$") && value.length()%16==0){
			return TriDES.decode(value);
		}
		return value;
	}

	public static int getInt(String key, int defaultValue) {
		String value = get(key, String.valueOf(defaultValue));
		if(isPassword(key,value)&&value.matches("^[A-F0-9]+$") && value.length()%16==0){
			return Integer.parseInt(TriDES.decode(value));
		}
		return Integer.parseInt(value);
	}

	public static String getWithFormat(String key, String ... vals) {
		String msg = "";
		String format = getProp().getProperty(key);
		if (!StringUtils.isBlank(format)) {
			MessageFormat mf = new MessageFormat(format);
			msg = mf.format(vals);
		}
		return msg;
	}

	public static String getWithFormat(String key, Map<String, String> params) {
		String str = get(key, "");
		String regex = "\\$\\{([^)]*?)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			key = m.group(1);
			String value = params.get(key);
			str = str.replace("${" + key + "}", StringUtils.defaultIfEmpty(value, ""));
		}
		return str;
	}

	public static Integer get(String key, Integer def) {
		try {
			Integer res = def;
			String value = getProp().getProperty(key);
			if(null==value|| StringUtils.isBlank(value)){
				value = String.valueOf(def);
			}
			
			if(isPassword(key,value)&&value.matches("^[A-F0-9]+$") && value.length()%16==0){
				return Integer.parseInt(TriDES.decode(value));
			}
			if (getProp().containsKey(key)){
				if(value == null || StringUtils.isEmpty(value) || value.toLowerCase().equals("null")
						|| (value.startsWith("${") && value.endsWith("}"))){
					value = res.toString();
				}
				res = Integer.parseInt(value);
			}
			return res;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}

	}

	public static Long get(String key, Long def) {
		try {
			Long res = def;
			String value = getProp().getProperty(key);
			if(null==value || StringUtils.isBlank(value)){
				value = String.valueOf(def);
			}
			if(isPassword(key,value)&&value.matches("^[A-F0-9]+$") && value.length()%16==0){
				return Long.parseLong(TriDES.decode(value));
			}
			if (getProp().containsKey(key)) res = Long.parseLong(value);
			return res;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}

	}

	public static void set(String key, String value) {
		getProp().put(key, value);
	}

	public void setFileList(List<String> fileList) {
		PropertiesUtils.fileList = fileList;
	}
	
	public static boolean isPassword(String key , String value){
		boolean flag = false;
		boolean flag2 = false;
		if(key!=null&&value!=null&&!"null".equals(value)){
			key = key.replace(".","_");
			for(String str:pwdNameList){
				if(str.equals(key.trim())){
					flag = true;
				}
			}
			if(key.trim().contains("password")&&!"db_password".equals(key.trim())){
				flag2 = true;
			}
		}
		return flag || flag2;
	}

}
