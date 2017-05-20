package com.hourse.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {

    /**
     * 错误号
     * @return
     */
    String errorNo() default "";

    /**
     * 错误信息
     * @return
     */
    String errorInfo() default "";

    /**
     * 验证类型
     * @return
     */
    String type() default "NotBlank";
}
