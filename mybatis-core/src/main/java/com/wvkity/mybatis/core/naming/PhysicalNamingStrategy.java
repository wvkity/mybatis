package com.wvkity.mybatis.core.naming;

import com.wvkity.mybatis.annotation.naming.NamingStrategy;

/**
 * 命名策略接口
 * @author wvkity
 */
public interface PhysicalNamingStrategy {

    /**
     * 表命名
     * @param tableName 表名
     * @param strategy  命名策略
     * @return 新的表名
     */
    String tableName(final String tableName, NamingStrategy strategy);

    /**
     * 字段命名
     * @param columnName 字段名
     * @param strategy   命名策略
     * @return 新的字段名
     */
    String columnName(final String columnName, NamingStrategy strategy);
}
