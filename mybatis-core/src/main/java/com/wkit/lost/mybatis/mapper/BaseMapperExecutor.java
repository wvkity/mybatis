package com.wkit.lost.mybatis.mapper;

/**
 * MyBatis泛型Mapper接口
 * @param <T> 实体类型
 * @param <V> 返回值类型
 * @author wvkity
 */
public interface BaseMapperExecutor<T, V, PK> extends InsertMapper<T>, UpdateMapper<T>, DeleteMapper<T>,
        QueryMapper<T, V, PK>, CriteriaMapper<T, V>, PagingMapper<T, V> {
}
