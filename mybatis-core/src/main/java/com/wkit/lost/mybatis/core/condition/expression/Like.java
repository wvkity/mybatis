package com.wkit.lost.mybatis.core.condition.expression;

import com.wkit.lost.mybatis.core.metadata.Column;
import com.wkit.lost.mybatis.utils.ColumnConvert;
import com.wkit.lost.mybatis.utils.StringUtil;
import com.wkit.lost.mybatis.core.criteria.Criteria;
import com.wkit.lost.mybatis.core.criteria.Logic;
import com.wkit.lost.mybatis.core.criteria.MatchMode;
import com.wkit.lost.mybatis.core.criteria.Operator;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * LIKE条件
 * @param <T> 泛型类型
 * @author wvkity
 */
public class Like<T> extends AbstractExpression<T> {

    private static final long serialVersionUID = -161970413795916260L;

    /**
     * 匹配模式
     */
    @Getter
    @Setter
    private MatchMode matchMode;

    /**
     * 转义字符
     */
    @Getter
    @Setter
    private Character escape;

    /**
     * 构造方法
     * @param property 属性
     * @param value    值
     */
    public Like( String property, String value ) {
        this( property, value, MatchMode.ANYWHERE, Logic.AND );
    }

    /**
     * 构造方法
     * @param property 属性
     * @param value    值
     */
    public Like( String property, String value, Logic logic ) {
        this( property, value, MatchMode.ANYWHERE, logic );
    }

    /**
     * 构造方法
     * @param property 属性
     * @param value    值
     * @param escape   转移字符
     */
    public Like( String property, String value, Character escape ) {
        this( property, value, MatchMode.ANYWHERE, escape, Logic.AND );
    }

    /**
     * 构造方法
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     */
    public Like( String property, String value, MatchMode matchMode ) {
        this( property, value, matchMode, Logic.AND );
    }

    /**
     * 构造方法
     * @param property 属性
     * @param value    值
     * @param escape   转移字符
     * @param logic    逻辑操作
     */
    public Like( String property, String value, Character escape, Logic logic ) {
        this( property, value, MatchMode.ANYWHERE, escape, logic );
    }

    /**
     * 构造方法
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     * @param logic     逻辑操作
     */
    public Like( String property, String value, MatchMode matchMode, Logic logic ) {
        this( property, value, matchMode, null, logic );
    }

    /**
     * 构造方法
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     * @param escape    转移字符
     */
    public Like( String property, String value, MatchMode matchMode, Character escape ) {
        this( property, value, matchMode, escape, Logic.AND );
    }


    /**
     * 构造方法
     * @param criteria 查询对象
     * @param property 属性
     * @param value    值
     */
    public Like( Criteria<T> criteria, String property, String value ) {
        this( criteria, property, value, MatchMode.ANYWHERE, Logic.AND );
    }

    /**
     * 构造方法
     * @param criteria 查询对象
     * @param property 属性
     * @param value    值
     * @param logic    逻辑操作
     */
    public Like( Criteria<T> criteria, String property, String value, Logic logic ) {
        this( criteria, property, value, MatchMode.ANYWHERE, logic );
    }

    /**
     * 构造方法
     * @param criteria 查询对象
     * @param property 属性
     * @param value    值
     * @param escape   转移字符
     */
    public Like( Criteria<T> criteria, String property, String value, Character escape ) {
        this( criteria, property, value, MatchMode.ANYWHERE, escape, Logic.AND );
    }

    /**
     * 构造方法
     * @param criteria  查询对象
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     */
    public Like( Criteria<T> criteria, String property, String value, MatchMode matchMode ) {
        this( criteria, property, value, matchMode, Logic.AND );
    }

    /**
     * 构造方法
     * @param criteria 查询对象
     * @param property 属性
     * @param value    值
     * @param escape   转移字符
     * @param logic    逻辑操作
     */
    public Like( Criteria<T> criteria, String property, String value, Character escape, Logic logic ) {
        this( criteria, property, value, MatchMode.ANYWHERE, escape, logic );
    }

    /**
     * 构造方法
     * @param criteria  查询对象
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     */
    public Like( Criteria<T> criteria, String property, String value, MatchMode matchMode, Logic logic ) {
        this( criteria, property, value, matchMode, null, logic );
    }

    /**
     * 构造方法
     * @param criteria  查询对象
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     * @param escape    转移字符
     */
    public Like( Criteria<T> criteria, String property, String value, MatchMode matchMode, Character escape ) {
        this( criteria, property, value, matchMode, escape, Logic.AND );
    }

    /**
     * 构造方法
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     * @param escape    转移字符
     * @param logic     逻辑操作
     */
    public Like( String property, String value, MatchMode matchMode, Character escape, Logic logic ) {
        this.property = property;
        this.value = value;
        this.logic = logic;
        this.operator = Operator.LIKE;
        this.matchMode = matchMode;
        this.escape = escape;
    }

    /**
     * 构造方法
     * @param criteria  查询对象
     * @param property  属性
     * @param value     值
     * @param matchMode 匹配模式
     * @param escape    转移字符
     * @param logic     逻辑操作
     */
    public Like( Criteria<T> criteria, String property, String value, MatchMode matchMode, Character escape, Logic logic ) {
        this.criteria = criteria;
        this.property = property;
        this.value = value;
        this.logic = logic;
        this.operator = Operator.LIKE;
        this.matchMode = matchMode;
        this.escape = escape;
    }

    @Override
    public String getSqlSegment() {
        StringBuilder builder = new StringBuilder( 60 );
        String placeholder = StringUtil.nvl( defaultPlaceholder( matchMode.getSqlSegment( value.toString() ) ), "" );
        Column column = getColumn();
        if ( column == null ) {
            builder.append( ColumnConvert.convertToCustomArg( this.property, placeholder, getAlias(), operator, logic.getSqlSegment() ) );
        } else {
            builder.append( ColumnConvert.convertToCustomArg( getColumn(), placeholder, getAlias(), operator, logic.getSqlSegment() ) );
        }
        if ( escape != null ) {
            builder.append( " ESCAPE " ).append( "'" ).append( escape ).append( "'" );
        }
        return builder.toString();
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof Like ) ) return false;
        if ( !super.equals( o ) ) return false;
        Like<?> like = ( Like<?> ) o;
        return matchMode == like.matchMode &&
                Objects.equals( escape, like.escape );
    }

    @Override
    public int hashCode() {
        return Objects.hash( super.hashCode(), matchMode, escape );
    }

    @Override
    protected String toJsonString() {
        return toJsonString( "matchMode", matchMode ) + toJsonString( "escape", escape );
    }
}
