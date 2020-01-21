package com.wkit.lost.mybatis.data.auditing.date.provider;

import java.time.LocalTime;

/**
 * {@link LocalTime}时间类型提供者
 * @author wvkity
 */
public class LocalTimeProvider extends AbstractProvider {
    
    @Override
    public LocalTime getNow() {
        return LocalTime.now();
    }
}
