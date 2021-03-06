package com.wvkity.mybatis.core.mapping.sql.query;

import com.wvkity.mybatis.core.constant.Execute;
import com.wvkity.mybatis.core.constant.Logic;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.mapping.sql.AbstractCreator;
import com.wvkity.mybatis.core.mapping.sql.utils.ScriptUtil;
import com.wvkity.mybatis.core.metadata.ColumnWrapper;

import java.util.stream.Collectors;

public class ExistsCreator extends AbstractCreator {

    @Override
    public String build() {
        ColumnWrapper primaryKey = table.getPrimaryKey();
        String script = "CASE WHEN COUNT(" + (primaryKey == null ? "*" : primaryKey.getColumn()) + ") > 0 " +
                "THEN 1 ELSE 0 END COUNT";
        return select(script, ScriptUtil.convertWhereTag(table.columns().stream().map(it ->
                ScriptUtil.convertIfTagWithNotNull(null, it, PARAM_ENTITY, true, true, Symbol.EQ, Logic.AND, EMPTY,
                        Execute.REPLACE)).collect(Collectors.joining(EMPTY, NEW_LINE, NEW_LINE))));

    }
}
