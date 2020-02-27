package com.wkit.lost.mybatis.annotation.auditing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 删除操作时间审计
 * @author wvkity
 */
@Documented
@Inherited
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface DeletedDate {
}