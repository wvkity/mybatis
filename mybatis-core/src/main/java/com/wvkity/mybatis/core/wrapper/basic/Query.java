package com.wvkity.mybatis.core.wrapper.basic;

import com.wvkity.mybatis.core.converter.Property;
import com.wvkity.mybatis.core.mapping.sql.utils.ScriptUtil;
import com.wvkity.mybatis.core.metadata.ColumnWrapper;
import com.wvkity.mybatis.core.wrapper.criteria.Criteria;
import com.wvkity.mybatis.core.wrapper.utils.CriteriaUtil;
import com.wvkity.mybatis.utils.ArrayUtil;
import com.wvkity.mybatis.utils.CollectionUtil;
import com.wvkity.mybatis.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查询字段(字段包装对象)
 * @author wvkity
 */
public class Query extends AbstractQueryWrapper<ColumnWrapper> {

    private static final long serialVersionUID = 9171953090394342598L;

    /**
     * 构造方法
     * @param criteria 条件对象
     * @param column   字段对象
     */
    private Query(Criteria<?> criteria, ColumnWrapper column) {
        this.criteria = criteria;
        this.column = column;
    }

    /**
     * 构造方法
     * @param criteria 条件对象
     * @param column   字段对象
     * @param alias    字段别名
     */
    private Query(Criteria<?> criteria, ColumnWrapper column, String alias) {
        this.criteria = criteria;
        this.column = column;
        this.columnAlias = alias;
    }

    /**
     * 获取属性
     * @return 属性
     */
    public String getProperty() {
        return this.column.getProperty();
    }

    @Override
    public String columnName() {
        return this.column.getColumn();
    }

    @Override
    public String getSegment() {
        return getSegment(true);
    }

    @Override
    public String getSegment(boolean applyQuery) {
        String tableAlias = this.criteria.isEnableAlias() ? this.criteria.as() : null;
        if (StringUtil.hasText(this.columnAlias)) {
            return ScriptUtil.convertQueryArg(tableAlias, column.getColumn(), this.columnAlias);
        } else {
            return ScriptUtil.convertQueryArg(tableAlias, column, criteria.getReference(),
                    applyQuery && criteria.isPropertyAutoMappingAlias());
        }
    }

    /**
     * 单个查询列
     */
    public static final class Single {
        private Single() {
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param property 属性Lambda对象
         * @param <T>      实体类型
         * @param <V>      属性值类型
         * @return 查询列对象
         */
        public static <T, V> Query query(Criteria<T> criteria, Property<T, V> property) {
            return query(criteria, criteria.searchColumn(property));
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param property 属性
         * @return 查询列对象
         */
        public static Query query(Criteria<?> criteria, String property) {
            return query(criteria, criteria.searchColumn(property));
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param property 属性Lambda对象
         * @param alias    字段别名
         * @param <T>      实体类型
         * @param <V>      属性值类型
         * @return 查询列对象
         */
        public static <T, V> Query query(Criteria<T> criteria, Property<T, V> property, String alias) {
            return query(criteria, criteria.searchColumn(property), alias);
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param property 属性
         * @param alias    字段别名
         * @return 查询列对象
         */
        public static Query query(Criteria<?> criteria, String property, String alias) {
            return query(criteria, criteria.searchColumn(property), alias);
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param column   字段包装对象
         * @return 查询列对象
         */
        public static Query query(Criteria<?> criteria, ColumnWrapper column) {
            return column != null ? new Query(criteria, column) : null;
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param column   字段包装对象
         * @param alias    字段别名
         * @return 查询列对象
         */
        public static Query query(Criteria<?> criteria, ColumnWrapper column, String alias) {
            return column != null ? new Query(criteria, column, alias) : null;
        }
    }

    /**
     * 多个查询列
     */
    public static final class Multi {
        private Multi() {
        }

        /**
         * 查询字段
         * @param criteria   条件对象
         * @param properties 属性数组
         * @param <T>        实体类型
         * @param <V>        属性值类型
         * @return 查询对象集合
         */
        @SafeVarargs
        public static <T, V> ArrayList<Query> query(Criteria<T> criteria, Property<T, V>... properties) {
            return query(criteria, CriteriaUtil.lambdaToColumn(criteria, ArrayUtil.toList(properties)));
        }

        /**
         * 查询字段
         * @param criteria   条件对象
         * @param properties 属性数组
         * @return 查询对象集合
         */
        public static ArrayList<Query> query(Criteria<?> criteria, String... properties) {
            return query(criteria, ArrayUtil.toList(properties));
        }

        /**
         * 查询字段
         * @param criteria   条件对象
         * @param properties 属性集合
         * @return 查询对象集合
         */
        public static ArrayList<Query> query(Criteria<?> criteria, Collection<String> properties) {
            Set<String> props = distinct(properties);
            return props.isEmpty() ? new ArrayList<>(0) :
                    props.stream().map(it -> Single.query(criteria, it))
                            .collect(Collectors.toCollection(ArrayList::new));
        }

        /**
         * 查询字段
         * @param criteria 条件对象
         * @param columns  字段包装对象集合
         * @return 查询对象集合
         */
        public static ArrayList<Query> query(Criteria<?> criteria, List<ColumnWrapper> columns) {
            if (CollectionUtil.hasElement(columns)) {
                return columns.stream().filter(Objects::nonNull).map(it -> Single.query(criteria, it))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ArrayList<>(0);
        }

        /**
         * 查询字段
         * @param criteria   条件对象
         * @param properties 字段别名-属性集合
         * @return 字符串查询列对象集合
         */
        public static ArrayList<Query> query(Criteria<?> criteria, Map<String, String> properties) {
            Map<String, String> its = filterNullValue(properties);
            if (CollectionUtil.hasElement(its)) {
                ArrayList<Query> list = new ArrayList<>(its.size());
                for (Map.Entry<String, String> entry : its.entrySet()) {
                    list.add(Single.query(criteria, entry.getValue(), entry.getKey()));
                }
                return list;
            }
            return new ArrayList<>(0);
        }
    }

}
