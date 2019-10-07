package com.wkit.lost.mybatis.core.condition.expression;

import com.wkit.lost.mybatis.core.Criteria;
import com.wkit.lost.mybatis.core.Logic;
import com.wkit.lost.mybatis.core.Operator;
import com.wkit.lost.mybatis.core.meta.Column;
import com.wkit.lost.mybatis.handler.EntityHandler;
import com.wkit.lost.mybatis.utils.StringUtil;
import lombok.Getter;

import java.util.Optional;

/**
 * 属性值相等条件类
 * @author DT
 */
public class PropertyEqualExpression<T> extends AbstractExpression<T> {

    private static final long serialVersionUID = -2062164176332255718L;

    /**
     * 其他条件对象
     */
    @Getter
    private Criteria<?> other;

    /**
     * 其他条件对象属性
     */
    @Getter
    private String otherProperty;

    /**
     * 构造方法
     * @param property      属性
     * @param other         其他条件对象
     * @param otherProperty 其他条件对象属性
     */
    public PropertyEqualExpression( String property, Criteria<?> other, String otherProperty ) {
        this( property, other, otherProperty, Logic.AND );
    }

    /**
     * 构造方法
     * @param property      属性
     * @param other         其他条件对象
     * @param otherProperty 其他条件对象属性
     * @param logic         逻辑操作
     */
    public PropertyEqualExpression( String property, Criteria<?> other, String otherProperty, Logic logic ) {
        this.property = property;
        this.other = other;
        this.otherProperty = otherProperty;
        this.logic = logic;
        this.operator = Operator.EQ;
    }

    /**
     * 构造方法
     * @param criteria      条件对象
     * @param property      属性
     * @param other         其他条件对象
     * @param otherProperty 其他条件对象属性
     */
    public PropertyEqualExpression( Criteria<T> criteria, String property, Criteria<?> other, String otherProperty ) {
        this( criteria, property, other, otherProperty, Logic.AND );
    }

    /**
     * 构造方法
     * @param criteria      条件对象
     * @param property      属性
     * @param other         其他条件对象
     * @param otherProperty 其他条件对象属性
     * @param logic         逻辑操作
     */
    public PropertyEqualExpression( Criteria<T> criteria, String property, Criteria<?> other, String otherProperty, Logic logic ) {
        this.criteria = criteria;
        this.property = property;
        this.other = other;
        this.otherProperty = otherProperty;
        this.logic = logic;
        this.operator = Operator.EQ;
    }

    @Override
    public String getSqlSegment() {
        Column column;
        if ( StringUtil.isBlank( property ) ) {
            column = EntityHandler.getTable( criteria.getEntity() ).getPrimaryKey();
        } else {
            column = getColumn();
        }
        Column otherColumn = other.searchColumn( otherProperty );
        String realColumn = Optional.ofNullable( column ).map( Column::getColumn ).orElse( this.property );
        String otherRealColumn = Optional.ofNullable( otherColumn ).map( Column::getColumn ).orElse( this.otherProperty );
        if ( column != null && otherColumn != null ) {
            StringBuffer buffer = new StringBuffer( 40 );
            buffer.append( " " );
            buffer.append( logic.getSqlSegment() );
            buffer.append( " " );
            if ( criteria.isEnableAlias() ) {
                buffer.append( criteria.getAlias() ).append( "." );
            }
            buffer.append( realColumn ).append( " " ).append( operator.getSqlSegment() ).append( " " );
            if ( other.isEnableAlias() ) {
                buffer.append( other.getAlias() ).append( "." );
            }
            buffer.append( otherRealColumn );
            return buffer.toString();
        }
        return "";
    }
}
