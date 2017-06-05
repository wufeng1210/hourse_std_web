package com.hourse.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveInfo {

	/**
	 * 固定长度，脱敏开始位置
	 * @return
     */
	public int start() default -1;

	/**
	 * 固定长度，脱敏截止位置
	 * @return
     */
	public int end() default -1;

	/**
	 * 动态长度，脱敏数据自定义
	 * @return
     */
	public String custom() default "";

	/**
	 * 脱敏类型<br />
	 * 目前支持
	 * 1.id_no:身份证
	 * @return
     */
	public String sensitiveType() default "";

}
