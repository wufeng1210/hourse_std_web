package com.hourse.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectKey {

    /**
     * 公钥路径
     * @return
     */
    String publicKey();

    /**
     * 私钥路径
     * @return
     */
    String privateKey();


}
