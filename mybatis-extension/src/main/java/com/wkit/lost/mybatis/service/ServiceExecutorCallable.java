package com.wkit.lost.mybatis.service;

import com.wkit.lost.mybatis.factory.CriteriaBuilderFactory;
import com.wkit.lost.mybatis.mapper.MapperExecutorCallable;

/**
 * 业务泛型接口
 * @param <T>  泛型类
 * @param <R>  返回值类
 */
public interface ServiceExecutorCallable<T, R> extends ReaderService<T, R>, WriterService<T>, CriteriaBuilderFactory<T> {

    /**
     * 获取Mapper泛型接口
     * @return Mapper泛型接口
     */
    MapperExecutorCallable<T, R> getExecutor();
    
}