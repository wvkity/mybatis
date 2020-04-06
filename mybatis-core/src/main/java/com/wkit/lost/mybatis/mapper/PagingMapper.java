package com.wkit.lost.mybatis.mapper;

import com.wkit.lost.mybatis.core.wrapper.criteria.Criteria;
import com.wkit.lost.mybatis.utils.Constants;
import com.wkit.lost.paging.Pageable;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分页查询接口
 * @param <T> 泛型类型
 * @param <V> 返回值类型
 */
public interface PagingMapper<T, V> {

    /**
     * 查询数据
     * @param criteria 条件对象
     * @return Object集合
     */
    List<Object> objectList( @Param( Constants.PARAM_CRITERIA ) final Criteria<T> criteria );

    /**
     * 查询数据
     * @param criteria 条件对象
     * @return Object集合
     */
    List<Object[]> arrayList( @Param( Constants.PARAM_CRITERIA ) final Criteria<T> criteria );

    /**
     * 查询数据
     * @param criteria 条件对象
     * @return Map
     */
    List<Map<String, Object>> mapList( @Param( Constants.PARAM_CRITERIA ) final Criteria<T> criteria );

    /**
     * 分页查询记录
     * @param entity   指定对象
     * @param pageable 分页对象
     * @return 多条记录
     */
    List<V> pageableList( @Param( Constants.PARAM_ENTITY ) T entity,
                          @Param( Constants.PARAM_PAGEABLE ) Pageable pageable );

    /**
     * 分页查询列表
     * @param pageable 分页对象
     * @param criteria 条件对象
     * @return 列表
     */
    List<V> pageableListByCriteria( @Param( Constants.PARAM_CRITERIA ) final Criteria<T> criteria,
                                    @Param( Constants.PARAM_PAGEABLE ) final Pageable pageable );


}
