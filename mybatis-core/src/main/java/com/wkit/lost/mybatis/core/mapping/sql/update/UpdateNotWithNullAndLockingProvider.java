package com.wkit.lost.mybatis.core.mapping.sql.update;

import com.wkit.lost.mybatis.core.constant.Execute;
import com.wkit.lost.mybatis.core.constant.Symbol;
import com.wkit.lost.mybatis.core.mapping.sql.AbstractProvider;
import com.wkit.lost.mybatis.core.mapping.sql.utils.ScriptUtil;
import com.wkit.lost.mybatis.core.metadata.ColumnWrapper;
import com.wkit.lost.mybatis.utils.Constants;

import java.util.Set;

public class UpdateNotWithNullAndLockingProvider extends AbstractProvider {

    @Override
    public String build() {
        StringBuilder script = new StringBuilder( 200 );
        Set<ColumnWrapper> columns = table.updatableColumns();
        for ( ColumnWrapper it : columns ) {
            script.append( ScriptUtil.convertIfTagWithNotNull( null, it,
                    Constants.PARAM_ENTITY, true, false, Symbol.EQ,
                    null, Constants.CHAR_COMMA, Execute.REPLACE ) );
        }
        return update(
                ScriptUtil.convertTrimTag( script.toString(), "SET", null,
                        null, Constants.CHAR_COMMA ),
                ( " WHERE " + ScriptUtil.convertPartArg( table.getPrimaryKey(), Constants.PARAM_ENTITY, Execute.REPLACE ) )
        );
    }
}
