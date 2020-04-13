package com.wkit.lost.mybatis.core.conditional.expression;

import com.wkit.lost.mybatis.core.conditional.utils.Formatter;
import com.wkit.lost.mybatis.core.constant.Logic;
import com.wkit.lost.mybatis.core.constant.TemplateMatch;
import com.wkit.lost.mybatis.core.mapping.sql.utils.ScriptUtil;
import com.wkit.lost.mybatis.core.wrapper.criteria.Criteria;
import com.wkit.lost.mybatis.utils.Constants;
import com.wkit.lost.mybatis.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模板条件
 * <p>
 * "{&#64;&#64;}" is not mandatory in the template and is used to identify it as a placeholder for a database field,
 * which is automatically replaced with the specified field when it is converted into an SQL fragment.
 * </p>
 * <pre>
 *     // MYSQL
 *     // Examples
 *     &#64;Inject
 *     private GradeService gradeService;
 *
 *     QueryCriteria&lt;Grade&gt; criteria = new QueryCriteria&lt;&gt;(Grade.class);
 *
 *     // single parameter:
 *     // NO1.
 *     String template = "LEFT(NAME, 2) = {}";
 *     criteria.directTemplate(template, "S1");
 *     gradeService.list(criteria);
 *     return:
 *     SELECT column1, column2, ... FROM GRADE WHERE LEFT(NAME, 2) = ?
 *
 *     // NO2.
 *     String template = "LEFT({&#64;&#64;}, 2) = {}";
 *     criteria.directTemplate(template, "NAME", "S1");
 *     gradeService.list(criteria);
 *     return:
 *     SELECT column1, column2, ... FROM GRADE WHERE LEFT(NAME, 2) = ?
 *
 *     // multiple parameter:
 *     // NO3.
 *     String template = "LEFT(NAME, {}) = {}";
 *     criteria.directTemplate(template, 2, "S1");
 *     gradeService.list(criteria);
 *     return:
 *     SELECT column1, column2, ... FROM GRADE WHERE LEFT(NAME, ?) = ?
 *
 *     // NO4.
 *     String template = "LEFT({&#64;&#64;}, {}) = {}";
 *     criteria.directTemplate(template,"NAME", 2, "S1");
 *     gradeService.list(criteria);
 *     return:
 *     SELECT column1, column2, ... FROM GRADE WHERE LEFT(NAME, ?) = ?
 *
 *     // map parameter:
 *     // NO5.
 *     String template = "LEFT(NAME, ${left}) = ${name}";
 *     Map&lt;String, Object&gt; params = new HashMap&lt;&gt;();
 *     params.put("left", 2);
 *     params.put("name", "S1");
 *     criteria.directTemplate(template, params);
 *     gradeService.list(criteria);
 *     return:
 *     SELECT column1, column2, ... FROM GRADE WHERE LEFT(NAME, ?) = ?
 *
 *     // NO6.
 *     String template = "LEFT({&#64;&#64;}, ${left}) = ${name}";
 *     Map&lt;String, Object&gt; params = new HashMap&lt;&gt;();
 *     params.put("left", 2);
 *     params.put("name", "S1");
 *     criteria.directTemplate(template, "NAME", params);
 *     gradeService.list(criteria);
 *     return:
 *     SELECT column1, column2, ... FROM GRADE WHERE LEFT(NAME, ?) = ?
 * </pre>
 * @param <T> 实体类型
 * @author wvkity
 */
public class DirectTemplate<T> extends DirectExpressionWrapper<T> {

    private static final long serialVersionUID = 5788833497759949457L;

    /**
     * 字段占位符标识
     */
    private static final String COLUMN_PLACEHOLDER = "{@@}";

    /**
     * 匹配模式
     */
    private final TemplateMatch match;

    /**
     * 模板
     */
    @Getter
    private final String template;

    /**
     * 值
     */
    @Getter
    @Setter
    private Collection<Object> values;

    @Getter
    @Setter
    private Map<String, Object> mapValues;

    /**
     * 构造方法
     * @param template 模板
     * @param value    值
     * @param logic    逻辑符号
     */
    DirectTemplate(String template, Object value, Logic logic) {
        this.value = value;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.SINGLE;
    }

    /**
     * 构造方法
     * @param column   字段
     * @param value    值
     * @param template 模板
     * @param logic    逻辑符号
     */
    DirectTemplate(String column, Object value, String template, Logic logic) {
        this.column = column;
        this.value = value;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.SINGLE;
    }

    /**
     * 构造方法
     * @param tableAlias 表别名
     * @param column     字段
     * @param value      值
     * @param template   模板
     * @param logic      逻辑符号
     */
    DirectTemplate(String tableAlias, String column, Object value, String template, Logic logic) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.value = value;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.SINGLE;
    }

    /**
     * 构造方法
     * @param criteria 条件包装对象
     * @param column   字段
     * @param value    值
     * @param template 模板
     * @param logic    逻辑符号
     */
    DirectTemplate(Criteria<T> criteria, String column, Object value, String template, Logic logic) {
        this.criteria = criteria;
        this.column = column;
        this.value = value;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.SINGLE;
    }


    /**
     * 构造方法
     * @param template 模板
     * @param values   值
     * @param logic    逻辑符号
     */
    DirectTemplate(String template, Collection<Object> values, Logic logic) {
        this.values = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MULTIPLE;
    }

    /**
     * 构造方法
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     */
    DirectTemplate(String column, Collection<Object> values, String template, Logic logic) {
        this.column = column;
        this.values = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MULTIPLE;
    }

    /**
     * 构造方法
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param template   模板
     * @param logic      逻辑符号
     */
    DirectTemplate(String tableAlias, String column, Collection<Object> values, String template, Logic logic) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.values = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MULTIPLE;
    }

    /**
     * 构造方法
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     */
    DirectTemplate(Criteria<T> criteria, String column, Collection<Object> values,
                   String template, Logic logic) {
        this.criteria = criteria;
        this.column = column;
        this.values = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MULTIPLE;
    }

    /**
     * 构造方法
     * @param template 模板
     * @param values   值
     * @param logic    逻辑符号
     */
    DirectTemplate(String template, Map<String, Object> values, Logic logic) {
        this.mapValues = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MAP;
    }

    /**
     * 构造方法
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     */
    DirectTemplate(String column, Map<String, Object> values, String template, Logic logic) {
        this.column = column;
        this.mapValues = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MAP;
    }

    /**
     * 构造方法
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param template   模板
     * @param logic      逻辑符号
     */
    DirectTemplate(String tableAlias, String column, Map<String, Object> values, String template, Logic logic) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.mapValues = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MAP;
    }

    /**
     * 构造方法
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     */
    DirectTemplate(Criteria<T> criteria, String column, Map<String, Object> values,
                   String template, Logic logic) {
        this.criteria = criteria;
        this.column = column;
        this.mapValues = values;
        this.template = template;
        this.logic = logic;
        this.match = TemplateMatch.MAP;
    }

    @Override
    public String getSegment() {
        StringBuilder builder = new StringBuilder(60);
        builder.append(this.logic.getSegment()).append(Constants.SPACE);
        String realTemplate;
        if (this.template.contains(COLUMN_PLACEHOLDER)) {
            String realAlias = this.criteria.as();
            String columnName = (StringUtil.hasText(realAlias) ? (realAlias.trim() + ".") : "") + this.column;
            realTemplate = this.template.replaceAll("\\" + COLUMN_PLACEHOLDER, columnName);
        } else {
            realTemplate = this.template;
        }
        switch (this.match) {
            // 单个参数
            case SINGLE:
                builder.append(Formatter.format(realTemplate,
                        ScriptUtil.safeJoint(defaultPlaceholder(this.value))));
                break;
            // 多个参数    
            case MULTIPLE:
                builder.append(Formatter.format(realTemplate,
                        this.values.stream().map(it -> ScriptUtil.safeJoint(defaultPlaceholder(it)))
                                .collect(Collectors.toList())));
                break;
            // Map参数    
            default:
                builder.append(Formatter.format(realTemplate,
                        this.mapValues.entrySet().parallelStream()
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        it -> ScriptUtil.safeJoint(defaultPlaceholder(it.getValue()))))));
                break;
        }
        return builder.toString();
    }

    /**
     * 创建TEMPLATE对象
     * @param template 模板
     * @param value    值
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String template, Object value) {
        return create(template, value, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param template 模板
     * @param value    值
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String template, Object value, Logic logic) {
        if (hasText(template)) {
            return new DirectTemplate<>(template, value, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param template 模板
     * @param values   值
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String template, Collection<Object> values) {
        return create(template, values, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String template, Collection<Object> values, Logic logic) {
        if (hasText(template)) {
            return new DirectTemplate<>(template, values, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param template 模板
     * @param values   值
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String template, Map<String, Object> values) {
        return create(template, values, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String template, Map<String, Object> values, Logic logic) {
        if (hasText(template)) {
            return new DirectTemplate<>(template, values, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param column   字段
     * @param value    值
     * @param template 模板
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String column, Object value, String template) {
        return create(column, value, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param column   字段
     * @param value    值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String column, Object value, String template, Logic logic) {
        if (hasText(column) && hasText(template) && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(column, value, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String column, Collection<Object> values, String template) {
        return create(column, values, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String column, Collection<Object> values,
                                               String template, Logic logic) {
        if (hasText(column) && hasText(template) && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(column, values, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String column, Map<String, Object> values, String template) {
        return create(column, values, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String column, Map<String, Object> values,
                                               String template, Logic logic) {
        if (hasText(column) && hasText(template) && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(column, values, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param value      值
     * @param template   模板
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String tableAlias, String column,
                                               Object value, String template) {
        return create(tableAlias, column, value, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param value      值
     * @param template   模板
     * @param logic      逻辑符号
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String tableAlias, String column,
                                               Object value, String template, Logic logic) {
        if (hasText(column) && hasText(template) && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(tableAlias, column, value, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param template   模板
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String tableAlias, String column,
                                               Collection<Object> values, String template) {
        return create(tableAlias, column, values, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param template   模板
     * @param logic      逻辑符号
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String tableAlias, String column,
                                               Collection<Object> values, String template, Logic logic) {
        if (hasText(column) && hasText(template)
                && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(tableAlias, column, values, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param template   模板
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String tableAlias, String column,
                                               Map<String, Object> values, String template) {
        return create(tableAlias, column, values, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param tableAlias 表别名
     * @param column     字段
     * @param values     值
     * @param template   模板
     * @param logic      逻辑符号
     * @param <T>        实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(String tableAlias, String column,
                                               Map<String, Object> values, String template, Logic logic) {
        if (hasText(column) && hasText(template) && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(tableAlias, column, values, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param value    值
     * @param template 模板
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(Criteria<T> criteria, String column,
                                               Object value, String template) {
        return create(criteria, column, value, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param value    值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(Criteria<T> criteria, String column,
                                               Object value, String template, Logic logic) {
        if (criteria != null && hasText(column) && hasText(template)
                && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(criteria, column, value, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(Criteria<T> criteria, String column,
                                               Collection<Object> values, String template) {
        return create(criteria, column, values, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(Criteria<T> criteria, String column,
                                               Collection<Object> values, String template, Logic logic) {
        if (criteria != null && hasText(column) && hasText(template)
                && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(criteria, column, values, template, logic);
        }
        return null;
    }

    /**
     * 创建TEMPLATE对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(Criteria<T> criteria, String column,
                                               Map<String, Object> values, String template) {
        return create(criteria, column, values, template, Logic.AND);
    }

    /**
     * 创建TEMPLATE对象
     * @param criteria 条件包装对象
     * @param column   字段
     * @param values   值
     * @param template 模板
     * @param logic    逻辑符号
     * @param <T>      实体类型
     * @return 条件对象
     */
    public static <T> DirectTemplate<T> create(Criteria<T> criteria, String column,
                                               Map<String, Object> values, String template, Logic logic) {
        if (criteria != null && hasText(column) && hasText(template)
                && template.contains(COLUMN_PLACEHOLDER)) {
            return new DirectTemplate<>(criteria, column, values, template, logic);
        }
        return null;
    }

}
