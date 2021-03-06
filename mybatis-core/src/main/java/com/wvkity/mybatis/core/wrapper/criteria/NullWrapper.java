package com.wvkity.mybatis.core.wrapper.criteria;

import com.wvkity.mybatis.core.converter.Property;
import com.wvkity.mybatis.core.converter.PropertyConverter;

/**
 * NULL条件
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 */
public interface NullWrapper<T, Chain extends NullWrapper<T, Chain>> extends PropertyConverter<T> {

    /**
     * IS NULL
     * @param property 属性
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain isNull(Property<T, V> property) {
        return isNull(convert(property));
    }

    /**
     * IS NULL
     * @param property 属性
     * @return {@code this}
     */
    Chain isNull(String property);

    /**
     * 或IS NULL
     * @param property 属性
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orIsNull(Property<T, V> property) {
        return orIsNull(convert(property));
    }

    /**
     * 或IS NULL
     * @param property 属性
     * @return {@code this}
     */
    Chain orIsNull(String property);

    /**
     * IS NULL
     * @param column 字段
     * @return {@code this}
     */
    Chain isNullWith(String column);

    /**
     * IS NULL
     * @param tableAlias 表别名
     * @param column     字段
     * @return {@code this}
     */
    Chain isNullWith(String tableAlias, String column);

    /**
     * 或IS NULL
     * @param column 字段
     * @return {@code this}
     */
    Chain orIsNullWith(String column);

    /**
     * 或IS NULL
     * @param tableAlias 表别名
     * @param column     字段
     * @return {@code this}
     */
    Chain orIsNullWith(String tableAlias, String column);

    /**
     * IS NULL
     * @param property 属性
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain notNull(Property<T, V> property) {
        return notNull(convert(property));
    }

    /**
     * IS NULL
     * @param property 属性
     * @return {@code this}
     */
    Chain notNull(String property);

    /**
     * 或IS NULL
     * @param property 属性
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orNotNull(Property<T, V> property) {
        return orNotNull(convert(property));
    }

    /**
     * 或IS NULL
     * @param property 属性
     * @return {@code this}
     */
    Chain orNotNull(String property);

    /**
     * IS NULL
     * @param column 字段
     * @return {@code this}
     */
    Chain notNullWith(String column);

    /**
     * IS NULL
     * @param tableAlias 表别名
     * @param column     字段
     * @return {@code this}
     */
    Chain notNullWith(String tableAlias, String column);

    /**
     * 或IS NULL
     * @param column 字段
     * @return {@code this}
     */
    Chain orNotNullWith(String column);

    /**
     * 或IS NULL
     * @param tableAlias 表别名
     * @param column     字段
     * @return {@code this}
     */
    Chain orNotNullWith(String tableAlias, String column);

}
