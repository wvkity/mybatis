package com.wvkity.mybatis.core.conditional.expression;

import com.wvkity.mybatis.core.constant.Logic;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.converter.Property;
import com.wvkity.mybatis.core.metadata.ColumnWrapper;
import com.wvkity.mybatis.core.wrapper.criteria.Criteria;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 小于条件
 * @author wvkity
 */
public class LessThan extends Simple {

    private static final long serialVersionUID = 771632679424527146L;

    /**
     * 构造方法
     * @param criteria 条件包装对象
     * @param column   字段包装对象
     * @param value    值
     * @param logic    逻辑符号
     */
    LessThan(Criteria<?> criteria, ColumnWrapper column, Object value, Logic logic) {
        super(criteria, column, value, Symbol.LT, logic);
    }

    /**
     * 创建条件构建器
     * @return 构建器
     */
    public static LessThan.Builder create() {
        return new LessThan.Builder();
    }

    /**
     * 条件对象构建器
     */
    @Setter
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        /**
         * 条件包装对象
         */
        private Criteria<?> criteria;
        /**
         * 条件包装对象
         */
        private ColumnWrapper column;
        /**
         * 属性
         */
        @Setter(AccessLevel.NONE)
        private String property;
        /**
         * 属性
         */
        @Setter(AccessLevel.NONE)
        private Property<?, ?> lambdaProperty;
        /**
         * 值
         */
        private Object value;
        /**
         * 逻辑符号
         */
        private Logic logic;

        /**
         * 属性
         * @param property 属性
         * @return {@link LessThan.Builder}
         */
        public LessThan.Builder property(String property) {
            this.property = property;
            return this;
        }

        /**
         * 属性
         * @param property 属性
         * @param <T>      实体类型
         * @param <V>      属性值类型
         * @return {@link LessThan.Builder}
         */
        public <T, V> LessThan.Builder property(Property<T, V> property) {
            this.lambdaProperty = property;
            return this;
        }

        /**
         * 构建条件对象
         * @return 条件对象
         */
        public LessThan build() {
            if (this.value == null) {
                return null;
            }
            if (this.column != null) {
                return new LessThan(this.criteria, this.column, this.value, this.logic);
            }
            if (this.criteria == null) {
                return null;
            }
            if (hasText(this.property)) {
                ColumnWrapper wrapper = this.criteria.searchColumn(this.property);
                if (wrapper != null) {
                    return new LessThan(this.criteria, wrapper, this.value, this.logic);
                }
            }
            if (lambdaProperty != null) {
                ColumnWrapper wrapper = this.criteria.searchColumn(lambdaProperty);
                if (wrapper != null) {
                    return new LessThan(this.criteria, wrapper, this.value, this.logic);
                }
            }
            return null;
        }
    }
}
