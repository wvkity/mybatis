package com.wvkity.mybatis.core.wrapper.basic;

import com.wvkity.mybatis.core.segment.Segment;
import com.wvkity.mybatis.utils.Constants;

import java.util.stream.Collectors;

/**
 * 排序片段容器
 * @author wvkity
 */
public class SortSegmentWrapper extends AbstractSegmentWrapper<AbstractSortWrapper<?>> {

    private static final long serialVersionUID = 3568038760419441925L;

    @Override
    public String getSegment() {
        if (isNotEmpty()) {
            return " ORDER BY " + this.segments.stream().map(Segment::getSegment)
                    .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return "";
    }

}
