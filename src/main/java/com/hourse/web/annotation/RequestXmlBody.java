package com.hourse.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能说明: request xml注解<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author guanhui<br>
 * 开发时间: 2016年09月08日<br>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestXmlBody {

}

