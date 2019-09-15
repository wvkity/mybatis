package com.wkit.lost.mybatis.sql.injector.methods;

import com.wkit.lost.mybatis.sql.method.AbstractCriteriaMethod;

import java.util.LinkedHashMap;

public class ListForMap extends AbstractCriteriaMethod {

    @Override
    public String mappedMethod() {
        return "listForMap";
    }

    @Override
    public Class<?> getResultType() {
        return LinkedHashMap.class;
    }
}
