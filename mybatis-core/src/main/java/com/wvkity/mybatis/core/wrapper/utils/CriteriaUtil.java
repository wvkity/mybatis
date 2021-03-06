package com.wvkity.mybatis.core.wrapper.utils;

import com.wvkity.mybatis.core.converter.Property;
import com.wvkity.mybatis.core.metadata.ColumnWrapper;
import com.wvkity.mybatis.core.wrapper.criteria.Criteria;
import com.wvkity.mybatis.utils.CollectionUtil;
import com.wvkity.mybatis.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 条件工具类
 * @author wvkity
 */
public final class CriteriaUtil {

    private CriteriaUtil() {
    }

    /**
     * 属性转成对应字段
     * @param criteria   条件对象
     * @param properties 属性集合
     * @param <T>        泛型类型
     * @return 字段集合
     */
    public static <T> List<ColumnWrapper> propertyToColumn(Criteria<T> criteria, Collection<String> properties) {
        return CollectionUtil.hasElement(properties) ? properties.stream().map(criteria::searchColumn)
                .filter(Objects::nonNull).collect(Collectors.toList()) : new ArrayList<>(0);
    }

    /**
     * 属性转成对应字段
     * @param criteria   条件对象
     * @param properties 属性集合
     * @param <T>        泛型类型
     * @param <V>        返回值类型
     * @return 字段集合
     */
    public static <T, V> List<ColumnWrapper> lambdaToColumn(Criteria<T> criteria, Collection<Property<T, V>> properties) {
        return CollectionUtil.hasElement(properties) ? properties.stream().map(criteria::searchColumn)
                .filter(Objects::nonNull).collect(Collectors.toList()) : new ArrayList<>(0);
    }
}
