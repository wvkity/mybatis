package com.wvkity.mybatis.spring.boot.autoconfigure;

import org.apache.ibatis.session.Configuration;

@FunctionalInterface
public interface ConfigurationCustomizer {

    void customize(Configuration configuration);
}
