package com.wvkity.mybatis.core.constant;

import com.wvkity.mybatis.core.segment.Segment;

/**
 * LIKE匹配模式枚举类
 * @author wvkity
 */
public enum Match implements Segment {

    /**
     * LIKE 'value'
     */
    EXACT {
        @Override
        public String getSegment(String value) {
            return value;
        }
    },

    /**
     * LIKE 'value%'
     */
    START {
        @Override
        public String getSegment(String value) {
            return value + '%';
        }
    },

    /**
     * LIKE '%value'
     */
    END {
        @Override
        public String getSegment(String value) {
            return '%' + value;
        }
    },

    /**
     * LIKE '%value%'
     */
    ANYWHERE {
        @Override
        public String getSegment(String value) {
            return '%' + value + '%';
        }
    };


    @Override
    public String getSegment() {
        return this.name();
    }

    /**
     * 转成SQL片段
     * @param value 值
     * @return SQL字符串
     */
    public abstract String getSegment(String value);

}
