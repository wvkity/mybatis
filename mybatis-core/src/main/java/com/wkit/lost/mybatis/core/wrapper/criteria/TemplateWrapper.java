package com.wkit.lost.mybatis.core.wrapper.criteria;

import com.wkit.lost.mybatis.core.conditional.expression.DirectTemplate;
import com.wkit.lost.mybatis.core.lambda.LambdaConverter;
import com.wkit.lost.mybatis.utils.ArrayUtil;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件接口
 * @param <Chain> 子类
 * @param <P>     Lambda类
 * @author wvkity
 * @see com.wkit.lost.mybatis.core.conditional.expression.Template
 * @see DirectTemplate
 */
public interface TemplateWrapper<Chain extends TemplateWrapper<Chain, P>, P> extends LambdaConverter<P> {

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default Chain template(String template, P property, Object value) {
        return template(template, lambdaToProperty(property), value);
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
     * @return {@code this}
     */
    default Chain orTemplate(String template, P property, Object value) {
        return orTemplate(template, lambdaToProperty(property), value);
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
     * @return {@code this}
     */
    default Chain template(String template, P property, Object... values) {
        return template(template, property, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    default Chain template(String template, P property, Collection<Object> values) {
        return template(template, lambdaToProperty(property), values);
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
    default Chain orTemplate(String template, P property, Object... values) {
        return orTemplate(template, property, ArrayUtil.toList(values));
    }

    /**
     * TEMPLATE
     * @param template 模板
     * @param property 属性
     * @param values   值
     * @return {@code this}
     */
    default Chain orTemplate(String template, P property, Collection<Object> values) {
        return orTemplate(template, lambdaToProperty(property), values);
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
     * @param values   值
     * @return {@code this}
     */
    default Chain template(String template, P property, Map<String, Object> values) {
        return template(template, lambdaToProperty(property), values);
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
     * @param values   值
     * @return {@code this}
     */
    default Chain orTemplate(String template, P property, Map<String, Object> values) {
        return orTemplate(template, lambdaToProperty(property), values);
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
     * @param values   值
     * @return {@code this}
     */
    Chain orDirectTemplate(String template, String column, Map<String, Object> values);

}