package com.wkit.lost.mybatis.data.auditing.date.provider;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * {@link Timestamp}时间类型提供者
 * @author wvkity
 */
public class TimestampProvider extends AbstractProvider {
    
    @Override
    public Timestamp getNow() {
        return Timestamp.from( Instant.now() );
    }
}
