package com.wkit.lost.mybatis.data.auditing.date.provider;

import java.time.LocalDate;

/**
 * {@link LocalDate}时间类型提供者
 * @author wvkity
 */
public class LocalDateProvider extends AbstractProvider {
    
    @Override
    public LocalDate getNow() {
        return LocalDate.now();
    }
}
