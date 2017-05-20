package com.hourse.web.util;

import com.alibaba.fastjson.JSONObject;
import com.hourse.web.annotation.Alias;
import com.hourse.web.annotation.DateFormat;
import com.hourse.web.annotation.NoSerialize;
import com.hourse.web.annotation.SensitiveInfo;
import com.hourse.web.sensitive.ISensitiveInfo;
import com.hourse.web.sensitive.SensitiveManager;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * 功能说明: Map工具类<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author tanjk@hundsun.com<br>
 * 开发时间: 2012-5-22<br>
 */
public class MapUtil extends MapUtils {

	private final static Logger logger = LoggerFactory.getLogger(MapUtil.class);

	public static <T> T map2ObjectIgnoreCase(Class<T> clazz, Map<String, ?> map) {
		T object = null;
		try {
			object = clazz.newInstance();
			if (map == null) return object;
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.putAll(map);
			toObject(clazz, object, tMap, true);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return object;
	}

	public static <T> T map2Object(Class<T> clazz, Map<String, ?> map) {
		T object = null;
		try {
			object = clazz.newInstance();
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.putAll(map);
			toObject(clazz, object, tMap, false);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return object;
	}

	private static void toObject(Class<?> clazz, Object object, Map<String, ?> map, boolean ignoreCase) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			if (ignoreCase) {
				// 不区分大小写
				map = new CaseInsensitiveMap(map);
			}
			for (int i = 0; i < fields.length; i++) {
				try {
					Alias alias = fields[i].getAnnotation(Alias.class);
					String fieldName = fields[i].getName();
					if ("serialVersionUID".equals(fieldName)) {
						continue;
					} else if ("class".equals(fieldName)) {
						continue;
					}
					Object fieldValue = map.get(fieldName);
					if (fieldValue == null && alias != null) {
						// 适配别名
						fieldValue = map.get(alias.value());
					}
					if (fieldValue != null) {
						if (Date.class.isAssignableFrom(fields[i].getType()) && fieldValue instanceof String) {
							fieldValue = DateUtil.parse((String)fieldValue);
						}
						if (fieldValue != null) {
							BeanUtils.setProperty(object, fieldName, fieldValue);
						}
					}

				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (SecurityException e) {
					logger.error(e.getMessage(), e);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		if (clazz.getSuperclass() != null) {
			toObject(clazz.getSuperclass(), object, map, ignoreCase);
		}

	}

	@SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> toMapList(Collection collection) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (collection != null && !collection.isEmpty()) {
			for (Object object : collection) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 增加对返回List<Map<String,Object>>类型的结果集支持Excel导出
				if (object instanceof HashMap<?, ?>) {
					retList.add((Map<String, Object>)object);
				} else {
					toMap(object.getClass(), object, map);
					retList.add(map);
				}
			}
		}
		return retList;
	}

	@SuppressWarnings("rawtypes")
	private static List<Map<String, Object>> desensitizationMapList(Collection collection, Map<String, ISensitiveInfo> sensitiveInfos) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (collection != null && !collection.isEmpty()) {
			for (Object object : collection) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 增加对返回List<Map<String,Object>>类型的结果集支持Excel导出
				if (object instanceof HashMap<?, ?>) {
					retList.add((Map<String, Object>)object);
				} else {
					toDesensitizationMap(object.getClass(), object, map, sensitiveInfos);
					retList.add(map);
				}
			}
		}
		return retList;
	}

	@SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> toDesensitizationMapList(Collection collection) {
		return desensitizationMapList(collection, null);
	}

	@SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> toDesensitizationMapList(Collection collection, Map<String, ISensitiveInfo> sensitiveInfos) {
		return desensitizationMapList(collection, sensitiveInfos);
	}

	/**
	 * 将对象转成&lt;String, Object&gt;，支持别名，支持日期格式化(DateFormat注解)
	 * @param object
	 * @return
	 */
	public static Map<String, Object> toMap(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (object == null) {
			return map;
		}

		if (object instanceof Map) {
			return (Map<String, Object>)object;
		}
		toMap(object.getClass(), object, map);
		return map;
	}

	private static Map<String, Object> desensitizationMap(Object object, Map<String, ISensitiveInfo> sensitiveInfos) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (object == null) {
			return map;
		}

		if (object instanceof Map) {
			return (Map<String, Object>)object;
		}
		toDesensitizationMap(object.getClass(), object, map, sensitiveInfos);
		return map;
	}

	/**
	 * 将对象转成&lt;String, Object&gt;，支持别名，支持日期格式化(DateFormat注解),支持脱敏(@SensitiveInfo注解)
	 * @param object
	 * @return
	 */
	public static Map<String, Object> toDesensitizationMap(Object object) {
		return desensitizationMap(object, null);
	}

	/**
	 * 将对象转成&lt;String, Object&gt;，支持别名，支持日期格式化(DateFormat注解),支持脱敏(@SensitiveInfo注解)
	 * @param object
	 * @return
	 */
	public static Map<String, Object> toDesensitizationMap(Object object, Map<String, ISensitiveInfo> sensitiveInfos) {
		return desensitizationMap(object, sensitiveInfos);
	}

	/**
	 * 将对象转成Map&lt;String, String&gt;，支持别名，支持日期格式化(DateFormat注解)
	 * @param object
	 * @return
	 */
	public static Map<String, String> toMapString(Object object) {
		Map<String, Object> map = toMap(object);
		Map<String, String> mapString = new HashMap<String, String>();
		for (Entry<String, Object> entry : map.entrySet()) {
			mapString.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return mapString;
	}

	/**
	 * 将对象转成Map&lt;String, String&gt;，支持别名，支持日期格式化(DateFormat注解)
	 * 如果对象属性包含数组，则把数组value改为 value,value,value的格式
	 * @param object
	 * @return
	 */
	public static Map<String, String> beanIncludeArray2Map(Object object) {
		Map<String, Object> map = toMap(object);
		Map<String, String> mapString = new HashMap<String, String>();
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (null == value || StringUtils.isBlank(value.toString())) {
				continue;
			}
			if (value instanceof String[]) {
				String[] v = (String[])value;
				for (int i = 0; i < v.length; i++) {
					if (i == 0) {
						value = v[i] + ",";
					} else if (i == v.length - 1) {
						value = value + v[i];
					} else {
						value = value + v[i] + ",";
					}
				}
			}
			if (key.contains("date") && value instanceof String) {
				value = value.toString().replaceAll("-", "");
			}
			mapString.put(entry.getKey(), String.valueOf(value));
		}
		return mapString;
	}

	private static void toMap(Class<?> clazz, Object object, Map<String, Object> map) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				NoSerialize noSerialize = fields[i].getAnnotation(NoSerialize.class);
				if (noSerialize != null) {
					continue;
				}
				Alias alias = fields[i].getAnnotation(Alias.class);
				String fieldName = fields[i].getName();
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				} else if ("class".equals(fieldName)) {
					continue;
				}
				try {
					Object value = PropertyUtils.getProperty(object, fieldName);
					// 日期格式化
					if (value != null && value instanceof Date) {
						DateFormat dateFormat = fields[i].getAnnotation(DateFormat.class);
						value = DateUtil.format((Date)value, dateFormat == null ? DateUtil.DATE_FORMAT : dateFormat.value());
					}
					// 如果value为null，则设置成空字符串。否则返回到前台的是null字符串
					map.put(fieldName, value == null ? "" : value);
					if (alias != null) {
						map.put(alias.value(), value == null ? "" : value);
					}
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					// 忽略get方法不存在错误
					// logger.error(e.getMessage(), e);
				}
			}
		}
		if (clazz.getSuperclass() != null) {
			toMap(clazz.getSuperclass(), object, map);
		}
	}

	private static void toDesensitizationMap(Class<?> clazz, Object object, Map<String, Object> map, Map<String, ISensitiveInfo> sensitiveInfos) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				NoSerialize noSerialize = fields[i].getAnnotation(NoSerialize.class);
				if (noSerialize != null) {
					continue;
				}
				Alias alias = fields[i].getAnnotation(Alias.class);
				String fieldName = fields[i].getName();
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				} else if ("class".equals(fieldName)) {
					continue;
				}
				try {
					Object value = PropertyUtils.getProperty(object, fieldName);
					// 日期格式化
					if (value != null && value instanceof Date) {
						DateFormat dateFormat = fields[i].getAnnotation(DateFormat.class);
						value = DateUtil.format((Date)value, dateFormat == null ? DateUtil.DATE_FORMAT : dateFormat.value());
					}
					// 脱敏操作
					value = desensitization(clazz, fields[i], value, sensitiveInfos);
					// 如果value为null，则设置成空字符串。否则返回到前台的是null字符串
					map.put(fieldName, value == null ? "" : value);
					if (alias != null) {
						map.put(alias.value(), value == null ? "" : value);
					}
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					// 忽略get方法不存在错误
					// logger.error(e.getMessage(), e);
				}
			}
		}
		if (clazz.getSuperclass() != null) {
			toDesensitizationMap(clazz.getSuperclass(), object, map, sensitiveInfos);
		}
	}

	/**
	 * 字段脱敏操作
	 * @author zhangxin
	 * @param clazz
	 * @param field
	 * @param value
	 * @param sensitiveInfos
	 * @return
	 */
	private static Object desensitization(Class<?> clazz, Field field, Object value, Map<String, ISensitiveInfo> sensitiveInfos) {
		SensitiveInfo sensitiveInfo = field.getAnnotation(SensitiveInfo.class);
		if (sensitiveInfo != null && StringUtils.isNotBlank((String)value)) {

				SensitiveManager sensitiveManager = SpringContext.getBean(SensitiveManager.class);
				String valueStr = (String)value;
				int start = sensitiveInfo.start();
				int end = sensitiveInfo.end();
				if (start >= 0 && start <= end && end < valueStr.length()) {
					String frontStr = valueStr.substring(0, start);
					String endStr = valueStr.substring(end + 1);
					StringBuffer sb = new StringBuffer();
					for (int k = 0; k < end - start + 1; k++) {
						sb.append("*");
					}
					String middleStr = sb.toString();
					value = frontStr + middleStr + endStr;
				} else if (StringUtils.isNotBlank(sensitiveInfo.custom()) && sensitiveInfos != null && sensitiveInfos.containsKey(sensitiveInfo.custom())) {
					return sensitiveInfos.get(sensitiveInfo.custom()).sensitive(value);
				} else if (StringUtils.isNotBlank(sensitiveInfo.sensitiveType()) && sensitiveManager.contains(sensitiveInfo.sensitiveType())) {
					return sensitiveManager.sensitive(sensitiveInfo.sensitiveType(), value);
				} else {
					logger.error("类名={},字段名为{},值为={},使用SensitiveInfo注解配置错误[start={},end={}]", clazz.getCanonicalName(), field.getName(), valueStr, start, end);
				}

		}
		return value;
	}

	/**
	 * 获取字段别名
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getFiledAlias(Object obj) {
		Map<String, String> aliasMap = new HashMap<String, String>();
		if (obj == null || obj instanceof Map) {
			return aliasMap;
		}
		Class<?> clz = obj.getClass();
		while (clz != Object.class) {
			for (Field field : clz.getDeclaredFields()) {
				field.setAccessible(true);
				Alias alias = field.getAnnotation(Alias.class);
				if (alias != null) {
					aliasMap.put(field.getName(), alias.value());
				}
			}
			clz = clz.getSuperclass();
		}
		return aliasMap;
	}

	/**
	 * 将后面一个Map合并到前面一个Map中，合并过程中如果有重复的将忽略
	 * @return
	 */
	public static Map<String, Object> mergeMap(Map<String, Object> mainMap, Map<String, Object> subMap) {
		for (Entry<String, Object> entry : subMap.entrySet()) {
			if (!mainMap.containsKey(entry.getKey())) {
				mainMap.put(entry.getKey(), entry.getValue());
			}
		}
		return mainMap;
	}

	/**
	 * clone一个map，并按key进行过滤
	 * @param from
	 * @param filter
	 * @return
	 */
	public static Map<String, Object> cloneByFilter(Map<String, ?> from, String[] filter) {
		Map<String, Object> target = new HashMap<String, Object>();
		for (String item : filter) {
			target.put(item, ObjectUtils.defaultIfNull(from.get(item), ""));
		}
		return target;
	}

	/**
	 * clone一个List，并按key进行过滤
	 * @param from
	 * @param filter
	 * @return
	 */
	public static List<Map<String, Object>> cloneByFilter(List<Map<String, ?>> from, String[] filter) {
		Map<String, Object> target = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Map<String, ?> map : from) {
			for (String item : filter) {
				target.put(item, ObjectUtils.defaultIfNull(map.get(item), ""));
				result.add(target);
			}
		}
		return result;
	}

	/**
	 * 把对象转成Map&lt;String, Object&gt;，不支持别名
	 * @author yejg
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> toMapIgnoreAlias(Object obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		if (obj == null) {
			return objMap;
		}

		if (obj instanceof Map) {
			return (Map<String, Object>)obj;
		}
		Class<?> clz = obj.getClass();
		while (clz != Object.class) {
			Field[] fields = clz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				NoSerialize noSerialize = fields[i].getAnnotation(NoSerialize.class);
				if (noSerialize != null) {
					continue;
				}
				String key = fields[i].getName();
				Object value = null;
				if ("serialVersionUID".equals(key)) {
					continue;
				}
				try {
					value = PropertyUtils.getProperty(obj, key);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
					objMap.put(key, value);
				}
			}
			clz = clz.getSuperclass();
		}
		return objMap;
	}

	/**
	 * 把对象转成Map&lt;String, Object&gt;，不支持别名,忽略null值
	 * @author zhangxin
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> toMapIgnoreBlank(Object obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		if (obj == null) {
			return objMap;
		}

		if (obj instanceof Map) {
			return (Map<String, Object>)obj;
		}
		Class<?> clz = obj.getClass();
		while (clz != Object.class) {
			Field[] fields = clz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				NoSerialize noSerialize = fields[i].getAnnotation(NoSerialize.class);
				if (noSerialize != null) {
					continue;
				}
				String key = fields[i].getName();
				Object value = null;
				if ("serialVersionUID".equals(key)) {
					continue;
				} 
				try {
					value = PropertyUtils.getProperty(obj, key);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				if (value != null) {
					objMap.put(key, value);
				}
			}
			clz = clz.getSuperclass();
		}
		return objMap;
	}

	/**
	 * 将String转为Map
	 * @param value
	 * @return
	 * 		开发人员: @author huadi<br>
	 *         开发时间: 2015年10月14日<br>
	 */
	public static Map<String, Map<String, String>> parseStr(String value) {
		Map<String, Map<String, String>> taskMap = new HashMap<String, Map<String, String>>();
		if (StringUtils.isNotBlank(value) && !StringUtils.equals(value, "{}")) {
			JSONObject obj = JSONObject.parseObject(value);
			Map<String, String> map = MapUtil.toMapString(obj);
			if (MapUtils.isNotEmpty(map)) {
				for (String key : map.keySet()) {
					if (StringUtils.isNotBlank(key)) {
						String orgin = MapUtils.getString(map, key, "");
						if (StringUtils.isNotBlank(orgin) && !StringUtils.equals(orgin, "{}")) {
							JSONObject innerObj = JSONObject.parseObject(orgin);
							Map<String, String> personMap = MapUtil.toMapString(innerObj);
							taskMap.put(key, personMap);
						}
					}
				}
			}
		}
		return taskMap;
	}

	/**
	 * 方法名称:transMapToString
	 * 传入参数:map
	 * 返回值:String 形如 username'chenziwen^password'1234
	 */
	public static String transMapToString(Map map) {
		Entry entry;
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			entry = (Entry)iterator.next();
			sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? "" : entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
		}
		return sb.toString();
	}

	/**
	 * 方法名称:transStringToMap
	 * 传入参数:mapString 形如 username'chenziwen^password'1234
	 * 返回值:Map
	 */
	public static Map transStringToMap(String mapString) {
		Map map = new HashMap();
		StringTokenizer items;
		for (StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens(); map.put(items.nextToken(), items.hasMoreTokens() ? ((Object)(items.nextToken())) : null))
			items = new StringTokenizer(entrys.nextToken(), "'");
		return map;
	}

	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		sortMap.putAll(map);
		return sortMap;
	}

	/**
	 * 使用 Map按value进行排序
	 * @param map
	 * @return
	 */
	public static Map<String, Object> sortMapByValue(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();
		List<Entry<String, Object>> entryList = new ArrayList<Entry<String, Object>>(map.entrySet());
		Collections.sort(entryList, new Comparator<Entry<String, Object>>() {

			@Override
			public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
				return String.valueOf(o1.getValue()).compareTo(String.valueOf(o2.getValue()));
			}
		});

		Iterator<Entry<String, Object>> iter = entryList.iterator();
		Entry<String, Object> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}

	/**
	 * Map<String,Object> 转化为 Map<String,String>
	 * @param objMap
	 * @return
	 */
	public static Map<String, String> convertToStringMap(Map<String, Object> objMap) {
		Map<String, String> tempMap = new HashMap<String, String>();
		for (Entry<String, Object> entry : objMap.entrySet()) {
			tempMap.put(entry.getKey(), null != entry.getValue() ? entry.getValue().toString() : "");
		}
		return tempMap;
	}
}
