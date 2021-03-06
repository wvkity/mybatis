package com.wvkity.mybatis.spring.boot.sequence;

import com.wvkity.mybatis.core.snowflake.sequence.Level;
import com.wvkity.mybatis.core.snowflake.sequence.Rule;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WorkerSequence配置类
 * @author wvkity
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "wvkity.sequence", ignoreInvalidFields = true)
public class SequenceProperties {

    /**
     * 开始时间(默认为2020-01-25 00:00:00)
     */
    private long epochTimestamp = -1L;

    /**
     * 机器ID
     */
    private long workerId = 1L;

    /**
     * 注册中心ID
     */
    private long dataCenterId = 1L;

    /**
     * 级别
     */
    private Level level = Level.MILLISECONDS;

    /**
     * 规则
     */
    private Rule rule = Rule.SPECIFIED;
}
