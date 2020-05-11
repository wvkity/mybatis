package com.wvkity.mybatis.core.injector.method;

import com.wvkity.mybatis.core.mapping.sql.Provider;
import com.wvkity.mybatis.core.metadata.TableWrapper;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 抽象通用查询方法注入
 * @param <T> SQL提供类
 * @author wvkity
 */
public abstract class AbstractGeneralQueryMethod<T extends Provider> extends AbstractGeneralMethod<T> {

    @Override
    public MappedStatement injectMappedStatement(TableWrapper table, Class<?> mapperInterface, Class<?> resultType) {
        return addSelectMappedStatement(mapperInterface, resultType, table, target());
    }

}