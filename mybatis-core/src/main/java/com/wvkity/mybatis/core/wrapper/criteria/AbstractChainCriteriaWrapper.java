package com.wvkity.mybatis.core.wrapper.criteria;

import com.wvkity.mybatis.core.converter.Property;
import com.wvkity.mybatis.core.metadata.ColumnWrapper;
import com.wvkity.mybatis.core.metadata.PropertyMappingCache;
import com.wvkity.mybatis.exception.MyBatisException;
import com.wvkity.mybatis.invoke.SerializedLambda;
import com.wvkity.mybatis.utils.CollectionUtil;
import com.wvkity.mybatis.utils.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象条件包装器
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 */
@Log4j2
@SuppressWarnings({"serial"})
abstract class AbstractChainCriteriaWrapper<T, Chain extends AbstractChainCriteriaWrapper<T, Chain>> extends
        AbstractBasicCriteriaWrapper<T, Chain> {

    /**
     * 属性-字段包装对象缓存(只读)
     */
    private Map<String, ColumnWrapper> _PROPERTY_COLUMN_CACHE;
    
    /**
     * 可更新字段名缓存(小写)
     */
    private Set<String> _UPDATABLE_COLUMN_NAME_CACHE;

    /**
     * 映射缓存是否标识已初始化
     */
    private boolean initialized = false;

    @Override
    protected void inits() {
        super.inits();
        this.initMappings(this.entityClass);
    }

    @Override
    public ColumnWrapper searchColumn(Property<?, ?> property) {
        if (property == null) {
            return null;
        }
        return getColumn(PropertyMappingCache.parse(property));
    }

    @Override
    public ColumnWrapper searchColumn(String property) {
        return StringUtil.hasText(property) ? getColumn(property) : null;
    }

    @Override
    public <V> String convert(Property<T, V> property) {
        if (property == null) {
            return null;
        }
        return methodToProperty(PropertyMappingCache.parse(property).getImplMethodName());
    }

    @Override
    public <E, V> String methodToProperty(Property<E, V> property) {
        if (property == null) {
            return null;
        }
        return methodToProperty(PropertyMappingCache.parse(property).getImplMethodName());
    }

    /**
     * 获取字段名
     * @param lambda Lambda
     * @return 字段名
     */
    private ColumnWrapper getColumn(SerializedLambda lambda) {
        return getColumn(methodToProperty(lambda.getImplMethodName()));
    }

    /**
     * 获取字段名
     * @param property 属性名
     * @return 字段名
     */
    protected ColumnWrapper getColumn(String property) {
        if (!initialized) {
        /*if ( this instanceof ForeignSubCriteria ) {
                // 子查询联表条件
                ForeignSubCriteria<?> foreign = ( ForeignSubCriteria<?> ) this;
                ColumnWrapper wrapper = null;
                if ( foreign.propertyForQueryCache != null && !foreign.propertyForQueryCache.isEmpty() ) {
                    wrapper = foreign.propertyForQueryCache.getOrDefault( property, null );
                }
                if ( wrapper == null && foreign.subCriteria != null ) {
                    return foreign.subCriteria.getColumn( property );
                }
                return wrapper;
            }*/
            initMappings(this.entityClass);
        }
        ColumnWrapper column = this._PROPERTY_COLUMN_CACHE.getOrDefault(property, null);
        if (column == null) {
            log.warn("The field mapping information for the entity class({}) cannot be found based on the `{}` " +
                    "attribute. Check to see if the attribute exists or is decorated using the @transient " +
                    "annotation.", this.entityClass.getCanonicalName(), property);
        }
        return column;
    }

    /**
     * 根据方法名获取属性(getXX|isXX)
     * @param methodName 方法名
     * @return 属性名
     */
    private String methodToProperty(String methodName) {
        return PropertyMappingCache.methodToProperty(methodName);
    }

    /**
     * 初始化表映射
     * @param klass    实体类
     * @param printing 是否打印警告信息
     */
    protected final void initMappingCache(Class<?> klass, boolean printing) {
        if (klass != null) {
            this._PROPERTY_COLUMN_CACHE = PropertyMappingCache.columnCache(klass);
            if (CollectionUtil.isEmpty(this._PROPERTY_COLUMN_CACHE)) {
                if (printing)
                    log.warn(("The corresponding table mapping information cannot be found in the cache according " +
                            "to the specified entity class name -- `[" + klass.getCanonicalName() + "]`, please check " +
                            "the configuration is correct!"));
            } else {
                this.initialized = true;
                this._UPDATABLE_COLUMN_NAME_CACHE = this._PROPERTY_COLUMN_CACHE.values().stream().map(it ->
                        it.getColumn().toLowerCase(Locale.ENGLISH)).collect(Collectors.toSet());
            }
        }
    }

    /**
     * 初始化属性-字段包装对象缓存
     * @param klass 实体类
     */
    private void initMappings(Class<?> klass) {
        if (!initialized) {
            initMappingCache(klass, false);
            if (CollectionUtil.isEmpty(this._PROPERTY_COLUMN_CACHE)) {
                throw new MyBatisException("The table field mapping information for the specified " +
                        "entity ['" + klass.getCanonicalName() + "'] could not be found");
            }
        }
    }

    /**
     * 检查字段是否可更新
     * @param column 字段名
     * @return boolean
     */
    final boolean checkCanUpdatable(final String column) {
        if (StringUtil.isBlank(column)) {
            return false;
        }
        if (CollectionUtil.isEmpty(this._UPDATABLE_COLUMN_NAME_CACHE)) {
            return true;
        }
        final String realColumn = column.toLowerCase(Locale.ENGLISH);
        return Optional.ofNullable(this._UPDATABLE_COLUMN_NAME_CACHE).filter(it ->
                it.contains(realColumn)).isPresent();
    }
}