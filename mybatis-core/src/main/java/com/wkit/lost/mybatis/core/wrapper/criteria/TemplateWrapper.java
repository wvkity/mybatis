package com.wkit.lost.mybatis.core.wrapper.criteria;

import com.wkit.lost.mybatis.core.conditional.expression.DirectTemplate;
import com.wkit.lost.mybatis.core.converter.Property;
import com.wkit.lost.mybatis.core.converter.PropertyConverter;
import com.wkit.lost.mybatis.utils.ArrayUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板条件接口
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 * @see com.wkit.lost.mybatis.core.conditional.expression.Template
 * @see DirectTemplate
 */
public interface TemplateWrapper<T, Chain extends CompareWrapper<T, Chain>> extends PropertyConverter<T> {

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain template(String template, Property<T, V> property, Object value) {
        return template(template, convert(property), value);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    Chain template(String template, String property, Object value);

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orTemplate(String template, Property<T, V> property, Object value) {
        return orTemplate(template, convert(property), value);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    Chain orTemplate(String template, String property, Object value);

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain template(String template, Property<T, V> property, Object... values) {
        return template(template, property, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain template(String template, Property<T, V> property, Collection<Object> values) {
        return template(template, convert(property), values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    default Chain template(String template, String property, Object... values) {
        return template(template, property, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    Chain template(String template, String property, Collection<Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    default <V> Chain orTemplate(String template, Property<T, V> property, Object... values) {
        return orTemplate(template, property, ArrayUtil.toList(values));
    }


    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    default <V> Chain orTemplate(String template, Property<T, V> property, Collection<Object> values) {
        return orTemplate(template, convert(property), values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    default Chain orTemplate(String template, String property, Object... values) {
        return orTemplate(template, property, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    Chain orTemplate(String template, String property, Collection<Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain template(String template, Property<T, V> property, String k1, Object v1, String k2, Object v2) {
        return template(template, convert(property), k1, v1, k2, v2);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain template(String template, Property<T, V> property, String k1, Object v1,
                               String k2, Object v2, String k3, Object v3) {
        return template(template, convert(property), k1, v1, k2, v2, k3, v3);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain template(String template, Property<T, V> property, Map<String, Object> values) {
        return template(template, convert(property), values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @return {@code this}
     */
    default Chain template(String template, String property, String k1, Object v1, String k2, Object v2) {
        Map<String, Object> values = new HashMap<>(2);
        values.put(k1, v1);
        values.put(k2, v2);
        return template(template, property, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @return {@code this}
     */
    default Chain template(String template, String property, String k1,
                           Object v1, String k2, Object v2, String k3, Object v3) {
        Map<String, Object> values = new HashMap<>(3);
        values.put(k1, v1);
        values.put(k2, v2);
        values.put(k3, v3);
        return template(template, property, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    Chain template(String template, String property, Map<String, Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orTemplate(String template, Property<T, V> property, String k1, Object v1, String k2, Object v2) {
        return orTemplate(template, convert(property), k1, v1, k2, v2);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orTemplate(String template, Property<T, V> property, String k1, Object v1,
                                 String k2, Object v2, String k3, Object v3) {
        return orTemplate(template, convert(property), k1, v1, k2, v2, k3, v3);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> Chain orTemplate(String template, Property<T, V> property, Map<String, Object> values) {
        return orTemplate(template, convert(property), values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @return {@code this}
     */
    default Chain orTemplate(String template, String property, String k1, Object v1, String k2, Object v2) {
        Map<String, Object> values = new HashMap<>(2);
        values.put(k1, v1);
        values.put(k2, v2);
        return orTemplate(template, property, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @return {@code this}
     */
    default Chain orTemplate(String template, String property, String k1,
                             Object v1, String k2, Object v2, String k3, Object v3) {
        Map<String, Object> values = new HashMap<>(3);
        values.put(k1, v1);
        values.put(k2, v2);
        values.put(k3, v3);
        return orTemplate(template, property, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    Chain orTemplate(String template, String property, Map<String, Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param value    值
     * @return {@code this}
     */
    Chain directTemplate(String template, Object value);

    /**
     * TEMPLATE
     * @param template 模板
     * @param values   值
     * @return {@code this}
     */
    default Chain directTemplate(String template, Object... values) {
        return directTemplate(template, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param values   值
     * @return {@code this}
     */
    Chain directTemplate(String template, Collection<Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @return {@code this}
     */
    default Chain directTemplate(String template, String k1, Object v1, String k2, Object v2) {
        Map<String, Object> values = new HashMap<>(2);
        values.put(k1, v1);
        values.put(k2, v2);
        return directTemplate(template, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @return {@code this}
     */
    default Chain directTemplate(String template, String k1, Object v1,
                                 String k2, Object v2, String k3, Object v3) {
        Map<String, Object> values = new HashMap<>(3);
        values.put(k1, v1);
        values.put(k2, v2);
        values.put(k3, v3);
        return directTemplate(template, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param values   值
     * @return {@code this}
     */
    Chain directTemplate(String template, Map<String, Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@code this}
     */
    Chain directTemplate(String template, String column, Object value);

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param values   值
     * @return {@code this}
     */
    default Chain directTemplate(String template, String column, Object... values) {
        return directTemplate(template, column, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param values   值
     * @return {@code this}
     */
    Chain directTemplate(String template, String column, Collection<Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @return {@code this}
     */
    default Chain directTemplate(String template, String column, String k1, Object v1, String k2, Object v2) {
        Map<String, Object> values = new HashMap<>(2);
        values.put(k1, v1);
        values.put(k2, v2);
        return directTemplate(template, column, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @return {@code this}
     */
    default Chain directTemplate(String template, String column, String k1, Object v1,
                                 String k2, Object v2, String k3, Object v3) {
        Map<String, Object> values = new HashMap<>(3);
        values.put(k1, v1);
        values.put(k2, v2);
        values.put(k3, v3);
        return directTemplate(template, column, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param values   值
     * @return {@code this}
     */
    Chain directTemplate(String template, String column, Map<String, Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param value    值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, Object value);

    /**
     * TEMPLATE
     * @param template 模板
     * @param values   值
     * @return {@code this}
     */
    default Chain orDirectTemplate(String template, Object... values) {
        return orDirectTemplate(template, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param values   值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, Collection<Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @return {@code this}
     */
    default Chain orDirectTemplate(String template, String k1, Object v1, String k2, Object v2) {
        Map<String, Object> values = new HashMap<>(2);
        values.put(k1, v1);
        values.put(k2, v2);
        return orDirectTemplate(template, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @return {@code this}
     */
    default Chain orDirectTemplate(String template, String k1, Object v1,
                                   String k2, Object v2, String k3, Object v3) {
        Map<String, Object> values = new HashMap<>(3);
        values.put(k1, v1);
        values.put(k2, v2);
        values.put(k3, v3);
        return orDirectTemplate(template, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param values   值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, Map<String, Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, String column, Object value);

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param values   值
     * @return {@code this}
     */
    default Chain orDirectTemplate(String template, String column, Object... values) {
        return orDirectTemplate(template, column, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param values   值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, String column, Collection<Object> values);

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @return {@code this}
     */
    default Chain orDirectTemplate(String template, String column, String k1, Object v1, String k2, Object v2) {
        Map<String, Object> values = new HashMap<>(2);
        values.put(k1, v1);
        values.put(k2, v2);
        return orDirectTemplate(template, column, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param k1       占位符key1
     * @param v1       key1对应值
     * @param k2       占位符key2
     * @param v2       key2对应值
     * @param k3       占位符key3
     * @param v3       key3对应值
     * @return {@code this}
     */
    default Chain orDirectTemplate(String template, String column, String k1, Object v1,
                                   String k2, Object v2, String k3, Object v3) {
        Map<String, Object> values = new HashMap<>(3);
        values.put(k1, v1);
        values.put(k2, v2);
        values.put(k3, v3);
        return orDirectTemplate(template, column, values);
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param column   字段
     * @param values   值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, String column, Map<String, Object> values);

}
