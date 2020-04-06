package com.wkit.lost.mybatis.core.conditional.expression;

import com.wkit.lost.mybatis.core.constant.Logic;
import com.wkit.lost.mybatis.core.constant.Symbol;
import com.wkit.lost.mybatis.core.lambda.Property;
import com.wkit.lost.mybatis.core.metadata.ColumnWrapper;
import com.wkit.lost.mybatis.core.wrapper.criteria.Criteria;

/**
 * IS NULL条件
 * @param <T> 实体类
 * @author wvkity
 */
public class Null<T> extends AbstractEmptyExpression<T> {

    private static final long serialVersionUID = 1553843773012605762L;

    /**
     * 构造方法
     * @param criteria 条件包装对象
     * @param column   字段包装对象
     * @param logic    逻辑符号
     */
    Null( Criteria<T> criteria, ColumnWrapper column, Logic logic ) {
        this.criteria = criteria;
        this.column = column;
        this.logic = logic;
        this.symbol = Symbol.NULL;
    }

    /**
     * 创建IS NULL条件对象
     * @param criteria 条件包装对象
     * @param property 属性
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> Null<T> create( Criteria<T> criteria, Property<T, ?> property ) {
        return create( criteria, property, Logic.AND );
    }

    /**
     * 创建IS NULL条件对象
     * @param criteria 条件包装对象
     * @param property 属性
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> Null<T> create( Criteria<T> criteria, Property<T, ?> property, Logic logic ) {
        if ( criteria != null && property != null ) {
            return create( criteria, criteria.searchColumn( property ), logic );
        }
        return null;
    }

    /**
     * 创建IS NULL条件对象
     * @param criteria 条件包装对象
     * @param property 属性
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> Null<T> create( Criteria<T> criteria, String property ) {
        return create( criteria, property, Logic.AND );
    }

    /**
     * 创建IS NULL条件对象
     * @param criteria 条件包装对象
     * @param property 属性
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> Null<T> create( Criteria<T> criteria, String property, Logic logic ) {
        if ( criteria != null && hasText( property ) ) {
            return create( criteria, criteria.searchColumn( property ), logic );
        }
        return null;
    }

    /**
     * 创建IS NULL条件对象
     * @param criteria 条件包装对象
     * @param column   字段包装对象
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> Null<T> create( Criteria<T> criteria, ColumnWrapper column ) {
        return create( criteria, column, Logic.AND );
    }

    /**
     * 创建IS NULL条件对象
     * @param criteria 条件包装对象
     * @param column   字段包装对象
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> Null<T> create( Criteria<T> criteria, ColumnWrapper column, Logic logic ) {
        if ( criteria != null && column != null ) {
            return new Null<>( criteria, column, logic );
        }
        return null;
    }
}
