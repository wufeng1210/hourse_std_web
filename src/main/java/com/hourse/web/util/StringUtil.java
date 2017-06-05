package com.hourse.web.util;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
}
