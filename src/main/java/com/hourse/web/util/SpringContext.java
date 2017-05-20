package com.hourse.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext appContext = null;
	
	public static Logger logger = LoggerFactory.getLogger(SpringContext.class);

	static {

		// 设置XServer模式
		System.setProperty("java.awt.headless", "true");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	public static boolean containsBean(String beanName) {
		if (appContext != null) {
			return appContext.containsBean(beanName);
		}
		return false;
	}

    public static Object getBean(String beanName) {
		if (appContext != null) {
			return appContext.getBean(beanName);
        }
        return null;
    }

	public static <T> T getBean(Class<T> clazz) {
		if (appContext != null) {
			try {
				return appContext.getBean(clazz);
			} catch (BeansException e) {
				Map<String, T> map = getBeansOfType(clazz);
				for (Map.Entry<String, T> entry : map.entrySet()) {
					if (entry.getValue().getClass().equals(clazz)) {
						return entry.getValue();
					}
				}
				throw e;
			}
		}
		return null;
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> clazz) throws BeansException {
		Map<String, T> map = null;
		if (appContext != null) {
			map = appContext.getBeansOfType(clazz);
			if ((map == null || map.isEmpty()) && appContext.getParent() != null) {
				map = appContext.getParent().getBeansOfType(clazz);
			}
		}
		if (map == null) {
			map = new HashMap<String, T>();
		}
		return map;
	}

	public static <T> T getBean(String beanId, Class<T> clazz) {
		if (appContext != null) {
			return appContext.getBean(beanId, clazz);
		}
		return null;
	}

	public <T> List<Map.Entry<String, T>> getOrderedBeans(Class<T> clazz) {
		Set<Map.Entry<String, T>> caches = getBeansOfType(clazz).entrySet();
		List<Map.Entry<String, T>> list = new ArrayList<Map.Entry<String, T>>(caches);
		/*
		 * 根据order顺序排列
		 */
		Collections.sort(list, new Comparator<Map.Entry<String, T>>() {

			public int compare(Map.Entry<String, T> e1, Map.Entry<String, T> e2) {
				return new OrderComparator().compare(e1.getValue(),
						e2.getValue());
			}
		});
		return list;
	}

	public static Method getHandleMethod(HttpServletRequest request, Object handler) throws Exception {
		MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
		String methodName = methodNameResolver.getHandlerMethodName(request);
		Map<String, String> pathVariables = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		for (Method method : handler.getClass().getMethods()) {
			RequestMapping rm = method.getAnnotation(RequestMapping.class);
			if (rm != null) {
				for (String val : rm.value()) {
					while (pathVariables != null && val.indexOf("{") < val.indexOf("}")) {
						String temp = val.substring(val.indexOf("{") + 1, val.indexOf("}"));
						val = val.replace("{" + temp + "}", pathVariables.get(temp));
					}
					if ("".equals(val) || methodName.equals(val)) {
						return method;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @deprecated 现在统一返回true,不取配置 <br />
	 * <s>读取配置的useConfigCache字段值.(默认返回true：使用redis缓存)</s>
	 * @author yejg
	 * @param context
	 * @return
	 */
	public static boolean useConfigCache(ApplicationContext context) {
		boolean useConfigCache = true;
		logger.debug("XPE平台不再支持此配置，不管配置useConfigCache为多少，均当做[true:使用redis缓存]来处理");
		// if (context instanceof WebApplicationContext) {
		// WebApplicationContext wac = (WebApplicationContext)context;
		// String paraValue = wac.getServletContext().getInitParameter("useConfigCache");
		// if ("false".equalsIgnoreCase(paraValue)) {
		// useConfigCache = false;
		// }
		// } else {
		// useConfigCache = false;
		// }
		return useConfigCache;
	}
}