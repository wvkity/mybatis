package com.wkit.lost.mybatis.filling.gen;

import java.time.LocalDateTime;

/**
 * {@link LocalDateTime}生成器
 * @author wvkity
 */
public class LocalDateTimeGenerator extends AbstractGenerator {

    @Override
    public Object getValue() {
        return LocalDateTime.now();
    }
}
