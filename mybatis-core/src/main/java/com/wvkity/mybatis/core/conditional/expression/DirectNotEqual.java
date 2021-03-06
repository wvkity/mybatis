package com.wvkity.mybatis.core.conditional.expression;

import com.wvkity.mybatis.core.constant.Logic;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.wrapper.criteria.Criteria;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 不等于条件
 * @author wvkity
 */
public class DirectNotEqual extends DirectSimple {

    private static final long serialVersionUID = -1800609049494847517L;

    /**
     * 构造方法
     * @param criteria   条件包装对象
     * @param tableAlias 表别名
     * @param column     列名
     * @param value      值
     * @param logic      逻辑符号
     */
    DirectNotEqual(Criteria<?> criteria, String tableAlias, String column, Object value, Logic logic) {
        super(criteria, tableAlias, column, value, Symbol.NE, logic);
    }

    /**
     * 创建条件构建器
     * @return 构建器
     */
    public static DirectNotEqual.Builder create() {
        return new DirectNotEqual.Builder();
    }

    /**
     * 条件构建器
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
         * 表别名
         */
        private String alias;
        /**
         * 条件包装对象
         */
        private String column;
        /**
         * 值
         */
        private Object value;
        /**
         * 逻辑符号
         */
        private Logic logic;

        /**
         * 构建条件对象
         * @return 条件对象
         */
        public DirectNotEqual build() {
            if (isEmpty(this.column) || this.value == null) {
                return null;
            }
            return new DirectNotEqual(this.criteria, this.alias, this.column, this.value, this.logic);
        }
    }
}
