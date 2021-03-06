package com.wvkity.mybatis.core.wrapper.criteria;

import com.wvkity.mybatis.core.converter.Property;
import com.wvkity.mybatis.core.converter.PropertyConverter;

/**
 * 子查询条件接口
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 */
public interface SubQueryWrapper<T, Chain extends CompareWrapper<T, Chain>> extends PropertyConverter<T> {

    /**
     * 主键等于
     * @param sc 子查询条件包装对象
     * @return {@code this}
     */
    Chain idEq(SubCriteria<?> sc);

    /**
     * 或主键等于
     * @param sc 子查询条件包装对象
     * @return {@code this}
     */
    Chain orIdEq(SubCriteria<?> sc);

    /**
     * 等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain eq(Property<T, V> property, SubCriteria<?> sc) {
        return eq(convert(property), sc);
    }

    /**
     * 等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain eq(String property, SubCriteria<?> sc);

    /**
     * 或等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orEq(Property<T, V> property, SubCriteria<?> sc) {
        return orEq(convert(property), sc);
    }

    /**
     * 或等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orEq(String property, SubCriteria<?> sc);

    /**
     * 等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain eqWith(String column, SubCriteria<?> sc);

    /**
     * 等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain eqWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orEqWith(String column, SubCriteria<?> sc);

    /**
     * 等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orEqWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 不等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain ne(Property<T, V> property, SubCriteria<?> sc) {
        return ne(convert(property), sc);
    }

    /**
     * 不等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain ne(String property, SubCriteria<?> sc);

    /**
     * 或不等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orNe(Property<T, V> property, SubCriteria<?> sc) {
        return orNe(convert(property), sc);
    }

    /**
     * 或不等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orNe(String property, SubCriteria<?> sc);

    /**
     * 不等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain neWith(String column, SubCriteria<?> sc);

    /**
     * 或不等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orNeWith(String column, SubCriteria<?> sc);

    /**
     * 不等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain neWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或不等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orNeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 小于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain lt(Property<T, V> property, SubCriteria<?> sc) {
        return lt(convert(property), sc);
    }

    /**
     * 小于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain lt(String property, SubCriteria<?> sc);

    /**
     * 或小于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orLt(Property<T, V> property, SubCriteria<?> sc) {
        return orLt(convert(property), sc);
    }

    /**
     * 或小于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orLt(String property, SubCriteria<?> sc);

    /**
     * 小于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain ltWith(String column, SubCriteria<?> sc);

    /**
     * 或小于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orLtWith(String column, SubCriteria<?> sc);

    /**
     * 小于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain ltWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或小于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orLtWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 小于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain le(Property<T, V> property, SubCriteria<?> sc) {
        return le(convert(property), sc);
    }

    /**
     * 小于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain le(String property, SubCriteria<?> sc);

    /**
     * 或小于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orLe(Property<T, V> property, SubCriteria<?> sc) {
        return orLe(convert(property), sc);
    }

    /**
     * 或小于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orLe(String property, SubCriteria<?> sc);

    /**
     * 小于等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain leWith(String column, SubCriteria<?> sc);

    /**
     * 或小于等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orLeWith(String column, SubCriteria<?> sc);

    /**
     * 小于等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain leWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或小于等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orLeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 大于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain gt(Property<T, V> property, SubCriteria<?> sc) {
        return gt(convert(property), sc);
    }

    /**
     * 大于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain gt(String property, SubCriteria<?> sc);

    /**
     * 或大于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orGt(Property<T, V> property, SubCriteria<?> sc) {
        return orGt(convert(property), sc);
    }

    /**
     * 或大于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orGt(String property, SubCriteria<?> sc);

    /**
     * 大于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain gtWith(String column, SubCriteria<?> sc);

    /**
     * 或大于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orGtWith(String column, SubCriteria<?> sc);

    /**
     * 大于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain gtWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或大于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orGtWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 大于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain ge(Property<T, V> property, SubCriteria<?> sc) {
        return ge(convert(property), sc);
    }

    /**
     * 大于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain ge(String property, SubCriteria<?> sc);

    /**
     * 或大于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orGe(Property<T, V> property, SubCriteria<?> sc) {
        return orGe(convert(property), sc);
    }

    /**
     * 或大于等于
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orGe(String property, SubCriteria<?> sc);

    /**
     * 大于等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain geWith(String column, SubCriteria<?> sc);

    /**
     * 或大于等于
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orGeWith(String column, SubCriteria<?> sc);

    /**
     * 大于等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain geWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或大于等于
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orGeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain like(Property<T, V> property, SubCriteria<?> sc) {
        return like(convert(property), sc);
    }

    /**
     * LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain like(String property, SubCriteria<?> sc);

    /**
     * 或LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orLike(Property<T, V> property, SubCriteria<?> sc) {
        return orLike(convert(property), sc);
    }

    /**
     * 或LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orLike(String property, SubCriteria<?> sc);

    /**
     * NOT LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain notLike(Property<T, V> property, SubCriteria<?> sc) {
        return notLike(convert(property), sc);
    }

    /**
     * NOT LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain notLike(String property, SubCriteria<?> sc);

    /**
     * 或NOT LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orNotLike(Property<T, V> property, SubCriteria<?> sc) {
        return orNotLike(convert(property), sc);
    }

    /**
     * 或NOT LIKE
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotLike(String property, SubCriteria<?> sc);

    /**
     * LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain likeWith(String column, SubCriteria<?> sc);

    /**
     * 或LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orLikeWith(String column, SubCriteria<?> sc);

    /**
     * LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain likeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orLikeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * NOT LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain notLikeWith(String column, SubCriteria<?> sc);

    /**
     * 或NOT LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotLikeWith(String column, SubCriteria<?> sc);

    /**
     * NOT LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain notLikeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或NOT LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotLikeWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain in(Property<T, V> property, SubCriteria<?> sc) {
        return in(convert(property), sc);
    }

    /**
     * IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain in(String property, SubCriteria<?> sc);

    /**
     * 或IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orIn(Property<T, V> property, SubCriteria<?> sc) {
        return orIn(convert(property), sc);
    }

    /**
     * 或IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orIn(String property, SubCriteria<?> sc);

    /**
     * NOT IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain notIn(Property<T, V> property, SubCriteria<?> sc) {
        return notIn(convert(property), sc);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain notIn(String property, SubCriteria<?> sc);

    /**
     * 或NOT IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orNotIn(Property<T, V> property, SubCriteria<?> sc) {
        return orNotIn(convert(property), sc);
    }

    /**
     * 或NOT IN
     * @param property 属性
     * @param sc       子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotIn(String property, SubCriteria<?> sc);

    /**
     * LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain inWith(String column, SubCriteria<?> sc);

    /**
     * 或LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orInWith(String column, SubCriteria<?> sc);

    /**
     * LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain inWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orInWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * NOT LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain notInWith(String column, SubCriteria<?> sc);

    /**
     * 或NOT LIKE
     * @param column 字段
     * @param sc     子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotInWith(String column, SubCriteria<?> sc);

    /**
     * NOT LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain notInWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * 或NOT LIKE
     * @param tableAlias 表别名
     * @param column     字段
     * @param sc         子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotInWith(String tableAlias, String column, SubCriteria<?> sc);

    /**
     * EXISTS
     * @param sc 子查询条件包装对象
     * @return {@code this}
     */
    Chain exists(SubCriteria<?> sc);

    /**
     * 或EXISTS
     * @param sc 子查询条件包装对象
     * @return {@code this}
     */
    Chain orExists(SubCriteria<?> sc);

    /**
     * NOT EXISTS
     * @param sc 子查询条件包装对象
     * @return {@code this}
     */
    Chain notExists(SubCriteria<?> sc);

    /**
     * 或NOT EXISTS
     * @param sc 子查询条件包装对象
     * @return {@code this}
     */
    Chain orNotExists(SubCriteria<?> sc);
}
