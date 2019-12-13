package com.wkit.lost.mybatis.snowflake.sequence;

/**
 * 模式
 * @author wvkity
 */
public enum Mode {

    /**
     * 根据MAC地址自动获取
     */
    MAC,

    /**
     * 指定机器码-数据中心
     */
    SPECIFIED,

    /**
     * 未知(采用默认: {@link #SPECIFIED})
     */
    UNKNOWN
}
