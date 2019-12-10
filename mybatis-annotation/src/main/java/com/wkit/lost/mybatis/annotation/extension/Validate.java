package com.wkit.lost.mybatis.annotation.extension;

/**
 * 字符串空值校验
 * @author wvkity
 */
public enum Validate {
    
    /**
     * 根据配置
     */
    CONFIG,

    /**
     * 忽略
     */
    IGNORED,

    /**
     * 校验
     */
    REQUIRED
}
