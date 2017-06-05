package com.hourse.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlValidate {

    /**
     * 验签Xpath路径
     * @return
     */
    String validateXpath();

    /**
     * 加签Xpath路径
     * @return
     */
    String signXpath();

    /**
     * 异常响应模板
     * @return
     */
    String errorTemplet();

    /**
     * 签名值Xpath路径
     * @return
     */
    String signValueXpath();
}
