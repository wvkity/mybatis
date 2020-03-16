package com.wkit.lost.mybatis.core.criteria;

import com.wkit.lost.mybatis.core.metadata.ColumnWrapper;
import com.wkit.lost.mybatis.lambda.Property;
import com.wkit.lost.mybatis.utils.CollectionUtil;
import com.wkit.lost.mybatis.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 条件工具类
 * @author wvkity
 */
public abstract class CriteriaUtil {

    /**
     * 属性转成对应字段
     * @param criteria   条件对象
     * @param properties 属性集合
     * @param <T>        泛型类型
     * @return 字段集合
     */
    public static <T> List<ColumnWrapper> propertyToColumn( Criteria<T> criteria, Collection<String> properties ) {
        return CollectionUtil.hasElement( properties ) ? properties.stream()
                .filter( StringUtil::hasText )
                .map( criteria::searchColumn )
                .collect( Collectors.toList() ) : new ArrayList<>( 0 );
    }

    /**
     * 属性转成对应字段
     * @param criteria   条件对象
     * @param properties 属性集合
     * @param <T>        泛型类型
     * @return 字段集合
     */
    public static <T> List<ColumnWrapper> lambdaToColumn( Criteria<T> criteria, Collection<Property<T, ?>> properties ) {
        return CollectionUtil.hasElement( properties ) ? properties.stream()
                .filter( Objects::nonNull )
                .map( criteria::searchColumn )
                .collect( Collectors.toList() ) : new ArrayList<>( 0 );
    }
}
