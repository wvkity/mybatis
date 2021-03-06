package com.wvkity.mybatis.mapper;

import com.wvkity.mybatis.utils.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 删除数据操作接口
 * @param <T> 泛型类
 * @param <PK> 主键
 * @author wvkity
 */
public interface DeleteMapper<T, PK> {

    /**
     * 根据指定对象删除记录
     * @param entity 指定对象
     * @return 受影响行数
     */
    int delete(@Param(Constants.PARAM_ENTITY) T entity);

    /**
     * 根据主键删除记录
     * @param id 主键
     * @return 受影响行数
     */
    int deleteById(PK id);

    /**
     * 逻辑删除
     * @param entity 实体
     * @return 受影响行数
     */
    int logicDelete(@Param(Constants.PARAM_ENTITY) T entity);

    /**
     * 根据指定对象批量删除记录
     * @param entities 对象集合
     * @return 受影响行数
     */
    int batchDelete(@Param(Constants.PARAM_ENTITIES) Collection<T> entities);

    /**
     * 根据主键批量删除记录
     * @param idList 主键集合
     * @return 受影响行数
     */
    int batchDeleteById(@Param(Constants.PARAM_PRIMARY_KEYS) List<PK> idList);

}
