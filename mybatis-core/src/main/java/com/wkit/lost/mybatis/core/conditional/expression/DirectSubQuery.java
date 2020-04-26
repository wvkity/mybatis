package com.wkit.lost.mybatis.core.conditional.expression;

import com.wkit.lost.mybatis.core.constant.Logic;
import com.wkit.lost.mybatis.core.constant.Symbol;
import com.wkit.lost.mybatis.core.wrapper.criteria.Criteria;
import com.wkit.lost.mybatis.core.wrapper.criteria.SubCriteria;
import com.wkit.lost.mybatis.utils.Constants;
import com.wkit.lost.mybatis.utils.StringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 子查询条件
 * @param <T> 实体类型
 * @author wvkity
 */
public class DirectSubQuery<T> extends DirectExpressionWrapper<T> {

    private static final long serialVersionUID = 498998912099294395L;

    /**
     * 子查询条件包装对象
     */
    private final SubCriteria<?> subCriteria;

    /**
     * 构造方法
     * @param criteria   条件包装对象
     * @param tableAlias 表别名
     * @param column     列名
     * @param sc         子查询条件包装对象
     * @param symbol     条件符号
     * @param logic      逻辑符号
     */
    DirectSubQuery(Criteria<T> criteria, String tableAlias, String column,
                   SubCriteria<?> sc, Symbol symbol, Logic logic) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.subCriteria = sc;
        this.symbol = symbol;
        this.logic = logic;
    }

    @Override
    public String getSegment() {
        if (this.symbol != Symbol.NULL && this.symbol != Symbol.NOT_NULL && this.symbol != Symbol.BETWEEN
                && this.symbol != Symbol.NOT_BETWEEN) {
            StringBuilder builder = new StringBuilder(60);
            if (this.logic != null) {
                builder.append(this.logic.getSegment()).append(Constants.SPACE);
            }
            String realSegment = this.subCriteria.getSegmentForCondition();
            if (this.column != null) {
                String realAlias = getAlias();
                if (StringUtil.hasText(realAlias)) {
                    builder.append(realAlias).append(Constants.DOT);
                }
                builder.append(this.column);
            }
            builder.append(Constants.SPACE).append(this.symbol.getSegment()).append(Constants.SPACE);
            builder.append(realSegment);
            return builder.toString();
        }
        return "";
    }

    /**
     * 创建条件构建器
     * @param <T> 实体类型
     * @return 构建器
     */
    public static <T> DirectSubQuery.Builder<T> create() {
        return new DirectSubQuery.Builder<>();
    }

    /**
     * 条件构建器
     * @param <T> 实体类型
     */
    @Setter
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder<T> {
        /**
         * 条件包装对象
         */
        private Criteria<T> criteria;
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
        private SubCriteria<?> sc;
        /**
         * 条件符号
         */
        private Symbol symbol;
        /**
         * 逻辑符号
         */
        private Logic logic;

        /**
         * 构建条件对象
         * @return 条件对象
         */
        public DirectSubQuery<T> build() {
            return new DirectSubQuery<>(this.criteria, this.alias, this.column, this.sc, this.symbol, this.logic);
        }
    }
}
