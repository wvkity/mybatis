package com.wkit.lost.mybatis.sql.injector.methods;

import com.wkit.lost.mybatis.sql.method.AbstractCriteriaMethod;

public class ListForObject extends AbstractCriteriaMethod {

    @Override
    public String mappedMethod() {
        return "listForObject";
    }

    @Override
    public Class<?> getResultType() {
        return Object.class;
    }
}
