package com.wkit.lost.mybatis.core.wrapper.basic;

import com.wkit.lost.mybatis.core.wrapper.criteria.Criteria;
import com.wkit.lost.mybatis.utils.ArrayUtil;
import com.wkit.lost.mybatis.utils.StringUtil;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排序(字符串字段)
 * @param <T> 实体类型
 * @author wvkity
 */
public class DirectOrder<T> extends AbstractOrderWrapper<T, String> {

    private static final long serialVersionUID = 7837358423348936221L;

    /**
     * 表别名
     */
    @Getter
    private String alias;

    /**
     * 构造方法
     * @param criteria  条件包装对象
     * @param ascending 排序方式(是否为ASC排序)
     * @param columns   字段集合
     */
    public DirectOrder(Criteria<T> criteria, boolean ascending, Collection<String> columns) {
        this.criteria = criteria;
        this.ascending = ascending;
        this.columns = distinct(columns);
    }

    /**
     * 构造方法
     * @param alias     表别名
     * @param ascending 排序方式(是否为ASC排序)
     * @param columns   字段集合
     */
    public DirectOrder(String alias, boolean ascending, Collection<String> columns) {
        this.alias = alias;
        this.ascending = ascending;
        this.columns = distinct(columns);
    }

    @Override
    public String getSegment() {
        if (notEmpty()) {
            String orderMode = ascending ? " ASC" : " DESC";
            String realAlias = StringUtil.hasText(this.alias) ? (this.alias + ".") :
                    (this.criteria != null && criteria.isEnableAlias() ? (criteria.as() + ".") : "");
            return this.columns.stream().map(it -> realAlias + it + orderMode)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    /**
     * ASC排序
     * @param columns 字段
     * @param <T>     泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> asc(String... columns) {
        return new DirectOrder<>((Criteria<T>) null, true, ArrayUtil.toList(columns));
    }

    /**
     * ASC排序
     * @param criteria 条件包装对象
     * @param columns  字段
     * @param <T>      泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> asc(Criteria<T> criteria, String... columns) {
        return new DirectOrder<>(criteria, true, ArrayUtil.toList(columns));
    }

    /**
     * ASC排序
     * @param alias   表别名
     * @param columns 字段
     * @param <T>     泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> ascWithAlias(String alias, String... columns) {
        return ascWithAlias(alias, ArrayUtil.toList(columns));
    }

    /**
     * ASC排序
     * @param alias   表别名
     * @param columns 字段
     * @param <T>     泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> ascWithAlias(String alias, List<String> columns) {
        return new DirectOrder<>(alias, true, columns);
    }

    /**
     * DESC排序
     * @param columns 字段
     * @param <T>     泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> desc(String... columns) {
        return new DirectOrder<>((Criteria<T>) null, false, ArrayUtil.toList(columns));
    }

    /**
     * DESC排序
     * @param criteria 条件包装对象
     * @param columns  字段
     * @param <T>      泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> desc(Criteria<T> criteria, String... columns) {
        return new DirectOrder<>(criteria, false, ArrayUtil.toList(columns));
    }

    /**
     * DESC排序
     * @param alias   表别名
     * @param columns 字段
     * @param <T>     泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> descWithAlias(String alias, String... columns) {
        return descWithAlias(alias, ArrayUtil.toList(columns));
    }

    /**
     * DESC排序
     * @param alias   表别名
     * @param columns 字段
     * @param <T>     泛型类型
     * @return 排序对象
     */
    public static <T> DirectOrder<T> descWithAlias(String alias, List<String> columns) {
        return new DirectOrder<>(alias, false, columns);
    }

}
