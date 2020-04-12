package com.wkit.lost.mybatis.core.conditional.expression;

import com.wkit.lost.mybatis.core.constant.Logic;
import com.wkit.lost.mybatis.core.constant.Symbol;
import com.wkit.lost.mybatis.core.wrapper.criteria.Criteria;

import java.util.Collection;

/**
 * IN范围条件
 * @param <T> 实体类型
 * @author wvkity
 */
public class DirectNotIn<T> extends AbstractDirectRangeExpression<T> {

    private static final long serialVersionUID = 2503342275351623888L;

    /**
     * 构造方法
     * @param column 字段
     * @param values 值
     * @param logic  逻辑符号
     */
    DirectNotIn(String column, Collection<Object> values, Logic logic) {
        this.column = column;
        this.values = values;
        this.logic = logic;
        this.symbol = Symbol.NOT_IN;
    }

    /**
     * 构造方法
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param logic      逻辑符号
     */
    DirectNotIn(String tableAlias, String column, Collection<Object> values, Logic logic) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.values = values;
        this.logic = logic;
        this.symbol = Symbol.NOT_IN;
    }

    /**
     * 构造方法
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param logic    逻辑符号
     */
    DirectNotIn(Criteria<T> criteria, String column, Collection<Object> values, Logic logic) {
        this.criteria = criteria;
        this.column = column;
        this.values = values;
        this.logic = logic;
        this.symbol = Symbol.NOT_IN;
    }

    /**
     * 创建IN范围条件对象
     * @param column 字段
     * @param values 值
     * @param <T>    实体类型
     * @return 条件对象
     */
    public static <T> DirectNotIn<T> create(String column, Collection<Object> values) {
        return create(column, values, Logic.AND);
    }

    /**
     * 创建IN范围条件对象
     * @param column 字段
     * @param values 值
     * @param logic  逻辑符号
     * @param <T>    实体类型
     * @return 条件对象
     */
    public static <T> DirectNotIn<T> create(String column, Collection<Object> values, Logic logic) {
        if (hasText(column)) {
            return new DirectNotIn<>(column, values, logic);
        }
        return null;
    }

    /**
     * 创建IN范围条件对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectNotIn<T> create(String tableAlias, String column, Collection<Object> values) {
        return create(tableAlias, column, values, Logic.AND);
    }

    /**
     * 创建IN范围条件对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param logic      逻辑符号
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectNotIn<T> create(String tableAlias, String column,
                                            Collection<Object> values, Logic logic) {
        if (hasText(column)) {
            return new DirectNotIn<>(tableAlias, column, values, logic);
        }
        return null;
    }

    /**
     * 创建IN范围条件对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectNotIn<T> create(Criteria<T> criteria, String column, Collection<Object> values) {
        return create(criteria, column, values, Logic.AND);
    }

    /**
     * 创建IN范围条件对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectNotIn<T> create(Criteria<T> criteria, String column,
                                            Collection<Object> values, Logic logic) {
        if (criteria != null && hasText(column)) {
            return new DirectNotIn<>(criteria, column, values, logic);
        }
        return null;
    }

}