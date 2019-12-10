package com.wkit.lost.mybatis.sql.injector.methods;

import com.wkit.lost.mybatis.core.meta.Table;
import com.wkit.lost.mybatis.sql.mapping.criteria.UpdateSqlBuilder;
import com.wkit.lost.mybatis.sql.mapping.script.DefaultXmlScriptBuilder;
import com.wkit.lost.mybatis.sql.method.AbstractCriteriaMethod;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 根据Criteria条件对象更新记录
 * @author wvkity
 */
public class UpdateByCriteria extends AbstractCriteriaMethod {

    @Override
    public MappedStatement injectMappedStatement( Class<?> mapperInterface, Class<?> resultType, Table table ) {
        Class<?> entity = table.getEntity();
        DefaultXmlScriptBuilder scriptBuilder = new DefaultXmlScriptBuilder( entity, null, table, new UpdateSqlBuilder() );
        return this.addUpdateMappedStatement( mapperInterface, entity, mappedMethod(), this.createSqlSource( scriptBuilder, entity ) );
    }

    @Override
    public Class<?> getResultType() {
        return null;
    }

    @Override
    public String mappedMethod() {
        return "updateByCriteria";
    }
}
