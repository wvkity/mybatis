package com.wkit.lost.mybatis.sql.mapping.update;

import com.wkit.lost.mybatis.core.Execute;
import com.wkit.lost.mybatis.core.meta.Column;
import com.wkit.lost.mybatis.sql.mapping.AbstractSqlBuilder;
import com.wkit.lost.mybatis.utils.Constants;

import java.util.Set;

/**
 * 根据指定对象更新记录SQL构建器
 * @author DT
 */
public class UpdateSqlBuilder extends AbstractSqlBuilder {

    @Override
    public String build() {
        Set<Column> columns = table.getUpdatableColumns();
        StringBuffer buffer = new StringBuffer( 200 );
        buffer.append( "\n<trim prefix=\"SET\" suffixOverrides=\",\">\n" );
        for ( Column column : columns ) {
            buffer.append( " " ).append( column.convertToArg( Execute.REPLACE, Constants.PARAM_ENTITY ) ).append( "," );
        }
        buffer.append( "\n</trim>\n" );
        String condition = "WHERE " + this.table.getPrimaryKey().convertToArg( Execute.REPLACE, Constants.PARAM_ENTITY );
        return update( buffer.toString(), condition );
    }
}
