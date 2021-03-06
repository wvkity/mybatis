package com.wvkity.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wvkity
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SequenceGenerator {

    /**
     * 序列名
     * @return 序列名
     */
    String name() default "";

    /**
     * 序列名
     * @return 序列名
     */
    String sequenceName() default "";
}
