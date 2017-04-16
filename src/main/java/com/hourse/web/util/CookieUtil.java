package com.hourse.web.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Zhangjun
 *
 */
public class CookieUtil {
	protected static Logger logger = Logger
			.getLogger(CookieUtil.class);

	/**
	 * cookie 读取
	 * 
	 * @param req
	 * @return
	 */
	public static String getCookie(HttpServletRequest req, String name) {
		// try{
		Cookie[] reqCookies = req.getCookies();
		if (reqCookies != null) {
			for (Cookie cookie : reqCookies) {
				if (name.equals(cookie.getName())) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						logger.error("解析cookie出现异常:" + e.getMessage());
						return null;
					}
				}
			}
		}
		return null;
	}

	
	public static Map<String,String> getCookieN_V(HttpServletRequest req, String name) {
		// try{
		Cookie[] reqCookies = req.getCookies();
		if (reqCookies != null) {
			for (Cookie cookie : reqCookies) {
				if (name.equals(cookie.getName())) {
					try {
						Map<String,String> resMap = new HashMap<String, String>();
						resMap.put("cookieName", cookie.getName());
						resMap.put("cookieValue", URLDecoder.decode(cookie.getValue(), "UTF-8"));
						resMap.put("cookieDomian", cookie.getDomain());
						resMap.put("cookiePath", cookie.getPath());
						
						return resMap;
					} catch (UnsupportedEncodingException e) {
						logger.error("解析cookie出现异常:" + e.getMessage());
						return null;
					}
				}
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	/**
	 * 清楚cookie
	 * 
	 * @param resp
	 * @param name
	 */
	public static void clearCookie(HttpServletResponse resp, String name,
                                   String domain) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		resp.addCookie(cookie);
	}

	/**
	 * cookie 加密
	 * 
	 * @param userProfile
	 */
	// public static String encryptionCookie(String str){
	// String key =
	// AuthCodeUtil.MD5(PropertiesUtils.get("oos.cookie.encryptkey"));
	// String cookieSteing = AuthCodeUtil.authcodeEncode(str, key);
	// return cookieSteing;
	// }

	/**
	 * cookie 解析
	 * 
	 * @param orgcookieString
	 * @return UserProfile null:解析失败或者密码错误
	 */
	// public static String decryptCookie(String orgcookieString){
	// try{
	// String key =
	// AuthCodeUtil.MD5(PropertiesUtils.get("oos.cookie.encryptkey"));
	// String cookieStringDecode = AuthCodeUtil.authcodeDecode(orgcookieString,
	// key);
	// return cookieStringDecode;
	// }catch(Exception e){
	// logger.error("解析失败" + e);
	// return null;
	// }
	//
	// }

	/**
	 * 读cookie
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(HttpServletRequest request, String key,
                                   Class<T> clazz) {
		String cookie = CookieUtil.getCookie(request, key);
		if (StringUtils.isNotEmpty(cookie)) {
			Gson gson = new Gson();

			Object bean = gson.fromJson(TriDES.decode(cookie), clazz);
			return (T) bean;
		}
		return null;

	}

	/**
	 * 写 cookie
	 * 
	 * @param response
	 * @param key
	 * @param object
	 * @throws UnsupportedEncodingException
	 */
	// public static void writeObject(HttpServletRequest request,
	// HttpServletResponse response, String key, Object object) {
	// JSONObject jsonObject = JSONObject.fromObject(object);
	// response.addCookie(new Cookie(key,
	// TriDES.encode(jsonObject.toString())));
	// }
	//
	/**
	 * 从cookie获取指定类型对象数据
	 * 
	 * @param req
	 * @param name
	 * @param clazz
	 * @return
	 */
	// @SuppressWarnings("unchecked")
	// public static <T> T getObjectCookie(HttpServletRequest req, String name,
	// Class<T> clazz) {
	// String cookie = CookieUtil.getCookie(req, name);
	// if (StringUtil.isNotEmpty(cookie)) {
	// Object bean =
	// JSONObject.toBean(JSONObject.fromObject(TriDES.decode(cookie)), clazz);
	// return (T)bean;
	// }
	// return null;
	// }

	/**
	 * 将对象转成json加密保存到cookie
	 * 
	 * @param response
	 * @param object
	 * @param cookieKey
	 * @param time
	 */
	public static void setObjectCookie(HttpServletResponse response,
			Object object, String cookieKey, int time) {
		setObjectCookie(response, object, cookieKey, time, null);
	}

	/**
	 * 将对象转成json加密保存到cookie
	 * 
	 * @param response
	 * @param object
	 * @param cookieKey
	 * @param time
	 * @param domain
	 */
	public static void setObjectCookie(HttpServletResponse response,
			Object object, String cookieKey, int time, String domain) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(object);
		setCookie(response, cookieKey, TriDES.encode(jsonStr), time, domain);
	}

	/****
	 * 添加cookie
	 * 
	 * @param response
	 * @param key
	 * @param value
	 * @param time
	 *            second
	 */
	public static void setCookie(HttpServletResponse response, String key,
                                 String value, int time) {
		setCookie(response, key, value, time, null);
	}

	/**
	 * 添加cookie
	 * 
	 * @param response
	 * @param key
	 * @param value
	 * @param time
	 * @param domain
	 */
	public static void setCookie(HttpServletResponse response, String key,
                                 String value, int time, String domain) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		if (domain != null) {
			cookie.setDomain(domain);
		}
		cookie.setHttpOnly(true);
		if (time > 0) {
			cookie.setMaxAge(time);
		}
		response.addCookie(cookie);
	}
}
