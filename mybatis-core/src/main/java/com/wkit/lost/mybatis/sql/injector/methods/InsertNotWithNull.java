package com.wkit.lost.mybatis.sql.injector.methods;

import com.wkit.lost.mybatis.core.metadata.TableWrapper;
import com.wkit.lost.mybatis.sql.mapping.insert.InsertNotWithNullSqlBuilder;
import com.wkit.lost.mybatis.sql.method.AbstractInsertMethod;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 保存记录
 * @author wvkity
 */
public class InsertNotWithNull extends AbstractInsertMethod {

    @Override
    public MappedStatement injectMappedStatement( Class<?> mapperInterface, Class<?> resultType, TableWrapper table ) {
        /*KeyGenerator keyGenerator = createKeyGenerator( table, ( mapperInterface.getName() + "." + mappedMethod() ) );
        Column primary = table.getPrimaryKey();
        Class<?> entity = table.getEntity();
        DefaultXmlScriptBuilder scriptBuilder = new DefaultXmlScriptBuilder( entity, null, table, new InsertSelectiveSqlBuilder() );
        return addInsertMappedStatement( mapperInterface, entity, mappedMethod(), createSqlSource( scriptBuilder, entity ), keyGenerator, primary.getProperty(), primary.getColumn() );*/
        return addInsertMappedStatement( mapperInterface, resultType, table, new InsertNotWithNullSqlBuilder() );
    }

    @Override
    public String mappedMethod() {
        return "insertNotWithNull";
    }
}