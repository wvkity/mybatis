package com.wvkity.mybatis.core.data.auditing;

import com.wvkity.mybatis.core.data.auditing.time.provider.DateTimeProvider;
import com.wvkity.mybatis.core.data.auditing.time.proxy.DateTimeProviderFactory;
import com.wvkity.mybatis.core.handler.TableHandler;
import com.wvkity.mybatis.core.metadata.ColumnWrapper;
import com.wvkity.mybatis.core.metadata.TableWrapper;
import com.wvkity.mybatis.core.wrapper.criteria.AbstractUpdateCriteriaWrapper;
import com.wvkity.mybatis.utils.Ascii;
import com.wvkity.mybatis.utils.Constants;
import com.wvkity.mybatis.utils.MetaObjectUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Objects;
import java.util.Optional;

/**
 * 抽象元数据审计处理器
 * @author wvkity
 */
public abstract class AbstractMetadataAuditable implements MetadataAuditable {

    protected static final String METHOD_UPDATE_BY_CRITERIA = "updateByCriteria";

    public AbstractMetadataAuditable() {
    }

    @Override
    public boolean isAuditable(MetaObject metadata, String property, Object value) {
        return Optional.ofNullable(getTable(metadata)).map(it ->
                it.search(property).map(column -> column.isAuditable()
                        && column.getJavaType().isAssignableFrom(value.getClass()))
                        .orElse(false)).orElse(false);
    }

    @Override
    public boolean isAuditable(MetaObject metadata, String property, Object value, AuditMatching matching) {
        return Optional.ofNullable(getTable(metadata))
                .map(it -> it.search(property)
                        .map(column -> column.isAuditable(matching)
                                && column.getJavaType().isAssignableFrom(value.getClass()))
                        .orElse(false)).orElse(false);
    }

    @Override
    public MetadataAuditable invoke(MetaObject metadata, String property, Object value) {
        if (metadata != null && value != null) {
            if (metadata.hasGetter(property) && metadata.hasSetter(property)) {
                if (isEmpty(metadata, property) && isAuditable(metadata, property, value)) {
                    metadata.setValue(property, value);
                }
            } else if (metadata.hasGetter(Constants.PARAM_ENTITY)) {
                Object entity = metadata.getValue(Constants.PARAM_ENTITY);
                if (entity != null) {
                    return invoke(MetaObjectUtil.forObject(entity), property, value);
                }
            } else {
                Object criteria = Optional.ofNullable(getCriteriaParameter(metadata))
                        .orElseGet(metadata::getOriginalObject);
                if (criteria instanceof AbstractUpdateCriteriaWrapper) {
                    return invoke((AbstractUpdateCriteriaWrapper<?>) criteria, property, value);
                }
            }
        }
        return this;
    }

    @Override
    public MetadataAuditable invoke(MetaObject metadata, String property, Object value, AuditMatching matching) {
        if (metadata != null && value != null) {
            if (metadata.hasGetter(property) && metadata.hasSetter(property)) {
                if (isEmpty(metadata, property) && isAuditable(metadata, property, value, matching)) {
                    metadata.setValue(property, value);
                }
            } else if (metadata.hasGetter(Constants.PARAM_ENTITY)) {
                Object entity = metadata.getValue(Constants.PARAM_ENTITY);
                if (entity != null) {
                    return invoke(MetaObjectUtil.forObject(entity), property, value, matching);
                }
            } else {
                Object criteria = Optional.ofNullable(getCriteriaParameter(metadata))
                        .orElseGet(metadata::getOriginalObject);
                if (criteria instanceof AbstractUpdateCriteriaWrapper) {
                    return invoke((AbstractUpdateCriteriaWrapper<?>) criteria, property, value);
                }
            }
        }
        return this;
    }

    @Override
    public MetadataAuditable invoke(AbstractUpdateCriteriaWrapper<?> criteria, String property, Object value) {
        Optional.ofNullable(criteria).filter(it -> it.notExists(property)).ifPresent(it -> it.set(property, value));
        return this;
    }

    /**
     * 时间审计
     * @param metadata 元数据对象
     * @param column   字段映射对象
     * @param matching 匹配模式
     */
    protected void dateTimeAuditing(MetaObject metadata, ColumnWrapper column, AuditMatching matching) {
        dateTimeAuditing(metadata, column.getProperty(),
                DateTimeProviderFactory.ProviderBuilder.create().target(column.getJavaType()).build(), matching);
    }

    /**
     * 时间审计
     * @param metadata 元数据对象
     * @param property 属性
     * @param provider 时间提供者
     * @param matching 匹配模式
     */
    protected void dateTimeAuditing(MetaObject metadata, String property,
                                    DateTimeProvider provider, AuditMatching matching) {
        Optional.ofNullable(provider).ifPresent(it -> invoke(metadata, property, it.getNow(), matching));
    }

    /**
     * 根据元数据获取表映射对象信息
     * @param metadata 元数据
     * @return 表信息
     */
    TableWrapper getTable(MetaObject metadata) {
        if (metadata != null) {
            TableWrapper table = TableHandler.getTable(metadata.getOriginalObject().getClass());
            if (table == null && metadata.hasGetter(Constants.PARAM_ENTITY)) {
                Object entity = metadata.getValue(Constants.PARAM_ENTITY);
                if (entity != null) {
                    return getTable(MetaObjectUtil.forObject(entity));
                }
            }
            return table;
        }
        return null;
    }

    /**
     * 根据属性查找字段信息
     * @param metadata 元数据对象
     * @param property 属性
     * @return 字段信息
     */
    Optional<ColumnWrapper> search(MetaObject metadata, String property) {
        return Optional.ofNullable(getTable(metadata))
                .flatMap(it -> it.search(property));
    }

    /**
     * 检查元数据中指定属性值是否为null
     * @param metadata 元数据
     * @param property 属性
     * @return true: 是, false: 否
     */
    boolean isEmpty(MetaObject metadata, String property) {
        if (Ascii.hasText(property)) {
            return Optional.ofNullable(metadata)
                    .filter(it -> it.hasGetter(property))
                    .map(it -> Objects.isNull(it.getValue(property)))
                    .orElse(false);
        }
        return false;
    }

    /**
     * 获取条件包装参数
     * @param metadata 元数据
     * @return 条件包装对象
     */
    protected Object getCriteriaParameter(MetaObject metadata) {
        if (metadata.hasGetter(Constants.PARAM_CRITERIA)) {
            // 条件包装对象
            return metadata.getValue(Constants.PARAM_CRITERIA);
        }
        return null;
    }
}
