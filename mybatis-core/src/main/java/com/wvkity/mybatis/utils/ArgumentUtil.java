package com.wvkity.mybatis.utils;

import com.wvkity.mybatis.core.metadata.ColumnWrapper;

import java.util.Optional;

/**
 * 参数工具类
 * @author wvkity
 */
public abstract class ArgumentUtil {

    /**
     * 参数填充
     * @param column      字段映射对象
     * @param placeholder 占位字符串
     * @return 字符串
     */
    public static String fill(ColumnWrapper column, String placeholder) {
        StringBuffer buffer = new StringBuffer(60);
        buffer.append("#{").append(placeholder);
        if (column != null) {
            // 指定JDBC类型
            Optional.ofNullable(column.getJdbcType()).ifPresent(it -> buffer.append(", jdbcType=")
                    .append(it.toString()));
            // 指定Java类型
            if (column.isUseJavaType() && !column.getJavaType().isArray()) {
                buffer.append(", javaType=").append(column.getJavaType().getCanonicalName());
            }
            // 指定处理类型
            Optional.ofNullable(column.getTypeHandler()).ifPresent(it -> buffer.append(", typeHandler=")
                    .append(it.getCanonicalName()));
        }
        buffer.append("}");
        return buffer.toString();
    }
}
