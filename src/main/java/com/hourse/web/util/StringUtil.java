package com.hourse.web.util;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class StringUtil extends MapUtils {

	private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);

	public static String translateStatus(String status){
		String resStr = "";
		if(StringUtils.equals(status,"0")){
			resStr="未审核";
		}else if(StringUtils.equals(status,"1")){
			resStr="审核通过";
		}else if(StringUtils.equals(status,"3")){
			resStr="审核打回";
		}
		return resStr;
	}

	public final static Map<String, String> status_map = new HashMap<String, String>() {
		{
			put("0", "未审核");
			put("1", "审核通过");
			put("3", "审核打回");
		}
	};


	public final static Map<String, String> yes_no_map = new HashMap<String, String>() {
		{
			put("1", "是");
			put("0", "不是");
		}
	};

}
