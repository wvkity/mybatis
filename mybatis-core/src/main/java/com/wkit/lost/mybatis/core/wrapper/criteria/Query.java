package com.wkit.lost.mybatis.core.wrapper.criteria;

import com.wkit.lost.mybatis.core.lambda.LambdaConverter;
import com.wkit.lost.mybatis.core.lambda.Property;
import com.wkit.lost.mybatis.utils.ArrayUtil;

import java.util.Collection;
import java.util.Map;

/**
 * 查询列接口
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 */
public interface Query<T, Chain extends Query<T, Chain>> extends CriteriaSearch, LambdaConverter<Property<T, ?>> {


    /**
     * 添加查询列
     * @param property 属性
     * @return {@code this}
     */
    default Chain select(Property<T, ?> property) {
        return select(lambdaToProperty(property));
    }

    /**
     * 添加查询列
     * @param property 属性
     * @return {@code this}
     */
    Chain select(String property);

    /**
     * 添加查询列
     * @param property 属性
     * @param alias    列别名
     * @return {@code this}
     */
    default Chain select(Property<T, ?> property, String alias) {
        return select(lambdaToProperty(property), alias);
    }

    /**
     * 添加查询列
     * @param property 属性
     * @param alias    列别名
     * @return {@code this}
     */
    Chain select(String property, String alias);

    /**
     * 添加查询列
     * @param column 列名
     * @return {@code this}
     */
    Chain directSelect(String column);

    /**
     * 添加查询列
     * @param column 列名
     * @param alias  列别名
     * @return {@code this}
     */
    Chain directSelect(String column, String alias);

    /**
     * 添加查询列
     * @param tableAlias 表别名
     * @param column     列名
     * @param alias      列别名
     * @return {@code this}
     */
    Chain directSelect(String tableAlias, String column, String alias);

    /**
     * 添加查询列
     * @param criteriaAlias 子查询对象别名
     * @param property      属性
     * @return {@code this}
     */
    default <E> Chain subSelect(String criteriaAlias, String property) {
        return subSelect(searchSub(criteriaAlias), property);
    }


    /**
     * 添加查询列
     * @param criteria 子查询对象
     * @param property 属性
     * @return {@code this}
     */
    default <E> Chain subSelect(SubCriteria<E> criteria, Property<E, ?> property) {
        return subSelect(criteria, criteria.lambdaToProperty(property));
    }

    /**
     * 添加查询列
     * @param criteria 子查询对象
     * @param property 属性
     * @return {@code this}
     */
    <E> Chain subSelect(SubCriteria<E> criteria, String property);

    /**
     * 添加查询列
     * @param criteria 子查询对象
     * @param property 属性
     * @param alias    列别名
     * @return {@code this}
     */
    default <E> Chain subSelect(SubCriteria<E> criteria, Property<E, ?> property, String alias) {
        return subSelect(criteria, criteria.lambdaToProperty(property), alias);
    }


    /**
     * 添加查询列
     * @param criteria 子查询对象
     * @param property 属性
     * @param alias    列别名
     * @return {@code this}
     */
    <E> Chain subSelect(SubCriteria<E> criteria, String property, String alias);

    /**
     * 添加查询列
     * @param properties 属性数组
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default Chain selects(Property<T, ?>... properties) {
        return selects(lambdaToProperties(properties));
    }

    /**
     * 添加查询列
     * @param properties 属性数组
     * @return {@code this}
     */
    default Chain selects(String... properties) {
        return selects(ArrayUtil.toList(properties));
    }

    /**
     * 添加查询列
     * @param properties 属性集合
     * @return {@code this}
     */
    Chain selects(Collection<String> properties);

    /**
     * 添加查询列
     * @param properties 列别名-属性集合
     * @return {@code this}
     */
    Chain selects(Map<String, String> properties);

    /**
     * 添加查询列
     * @param columns 列名数组
     * @return {@code this}
     */
    default Chain directSelects(String... columns) {
        return directSelects(ArrayUtil.toList(columns));
    }

    /**
     * 添加查询列
     * @param columns 列名集合
     * @return {@code this}
     */
    Chain directSelects(Collection<String> columns);

    /**
     * 添加查询列
     * @param columns 列别名-列名集合
     * @return {@code this}
     */
    Chain directSelects(Map<String, String> columns);

    /**
     * 添加查询列
     * @param tableAlias 表别名
     * @param columns    列别名-列名集合
     * @return {@code this}
     */
    Chain directSelects(String tableAlias, Map<String, String> columns);

    /**
     * 添加查询列
     * @param tableAlias 表别名
     * @param columns    列名数组
     * @return {@code this}
     */
    default Chain directQueriesWithAlias(String tableAlias, String... columns) {
        return directSelects(tableAlias, ArrayUtil.toList(columns));
    }

    /**
     * 添加查询列
     * @param tableAlias 表别名
     * @param columns    列名集合
     * @return {@code this}
     */
    Chain directSelects(String tableAlias, Collection<String> columns);

    /**
     * 添加子查询列
     * @param criteriaAlias 子查询条件对象别名
     * @param properties    属性数组
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> Chain subSelects(String criteriaAlias, String... properties) {
        return subSelects(criteriaAlias, ArrayUtil.toList(properties));
    }

    /**
     * 添加子查询列
     * @param criteriaAlias 子查询条件对象别名
     * @param properties    属性集合
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> Chain subSelects(String criteriaAlias, Collection<String> properties) {
        return subSelects(searchSub(criteriaAlias), properties);
    }

    /**
     * 添加子查询列
     * @param criteria   子查询条件对象
     * @param properties 属性数组
     * @param <E>        实体类型
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default <E> Chain subSelects(SubCriteria<E> criteria, Property<E, ?>... properties) {
        return subSelects(criteria, criteria.lambdaToProperties(properties));
    }

    /**
     * 添加子查询列
     * @param criteria   子查询条件对象
     * @param properties 属性数组
     * @param <E>        实体类型
     * @return {@code this}
     */
    default <E> Chain subSelects(SubCriteria<E> criteria, String... properties) {
        return subSelects(criteria, ArrayUtil.toList(properties));
    }

    /**
     * 添加子查询列
     * @param criteria   子查询条件对象
     * @param properties 属性集合
     * @param <E>        实体类型
     * @return {@code this}
     */
    <E> Chain subSelects(SubCriteria<E> criteria, Collection<String> properties);

    /**
     * 添加子查询列
     * @param criteriaAlias 子查询条件对象别名
     * @param properties    列别名-属性集合
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> Chain subSelects(String criteriaAlias, Map<String, String> properties) {
        return subSelects(searchSub(criteriaAlias), properties);
    }


    /**
     * 添加子查询列
     * @param criteria   子查询条件对象
     * @param properties 列别名-属性集合
     * @param <E>        实体类型
     * @return {@code this}
     */
    <E> Chain subSelects(SubCriteria<E> criteria, Map<String, String> properties);

    /**
     * 排除查询列
     * @param property 属性
     * @return {@code this}
     */
    default Chain exclude(Property<T, ?> property) {
        return exclude(lambdaToProperty(property));
    }

    /**
     * 排除查询列
     * @param property 属性
     * @return {@code this}
     */
    default Chain exclude(String property) {
        return excludes(property);
    }

    /**
     * 排除查询列
     * @param properties 属性数组
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default Chain excludes(Property<T, ?>... properties) {
        return excludes(lambdaToProperties(properties));
    }

    /**
     * 排除查询列
     * @param properties 属性数组
     * @return {@code this}
     */
    default Chain excludes(String... properties) {
        return excludes(ArrayUtil.toList(properties));
    }

    /**
     * 排除查询列
     * @param properties 属性集合
     * @return {@code this}
     */
    Chain excludes(Collection<String> properties);

    /**
     * 排除查询列
     * @param columns 列名数组
     * @return {@code this}
     */
    default Chain directExcludes(String... columns) {
        return directExcludes(ArrayUtil.toList(columns));
    }

    /**
     * 排除查询列
     * @param columns 列名集合
     * @return {@code this}
     */
    Chain directExcludes(Collection<String> columns);

    /**
     * 获取查询字段片段
     * @return SQL字符串
     */
    String getQuerySegment();

}
