package com.wvkity.mybatis.core.data.auditing.time.provider;

/**
 * 时间提供接口
 * @author wvkity
 */
public interface DateTimeProvider {

    /**
     * 当前时间
     * @return 时间
     */
    Object getNow();
}
