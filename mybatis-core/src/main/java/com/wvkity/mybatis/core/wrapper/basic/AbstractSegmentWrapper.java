package com.wvkity.mybatis.core.wrapper.basic;

import com.wvkity.mybatis.core.segment.Segment;
import com.wvkity.mybatis.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象SQL片段
 * @author wvkity
 */
@SuppressWarnings({"serial"})
public abstract class AbstractSegmentWrapper<E> implements Segment {

    /**
     * SQL片段集合
     */
    protected final List<E> segments = new ArrayList<>();

    /**
     * 添加单个片段对象
     * @param segment 片段对象
     */
    public void add(E segment) {
        if (segment != null) {
            this.segments.add(segment);
        }
    }

    /**
     * 添加多个片段对象
     * @param segments 片段对象集合
     */
    public void addAll(Collection<E> segments) {
        if (CollectionUtil.hasElement(segments)) {
            Set<E> its = segments.stream().filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (CollectionUtil.hasElement(its)) {
                this.segments.addAll(its);
            }
        }
    }

    /**
     * 检查是否存在SQL片段
     * @return boolean
     */
    public boolean isNotEmpty() {
        return CollectionUtil.hasElement(this.segments);
    }
}
