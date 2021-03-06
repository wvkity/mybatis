package com.wvkity.mybatis.mapper;

/**
 * 实体、返回值一样Mapper通用接口
 * @param <T>  实体类型、返回值类型
 * @param <PK> 主键类型
 * @author wvkity
 */
public interface UniformMapper<T, PK> extends BaseMapper<T, T, PK> {
}
