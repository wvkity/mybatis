package com.wkit.lost.mybatis.core.segment;

import com.wkit.lost.mybatis.core.conditional.criterion.Criterion;
import com.wkit.lost.mybatis.core.wrapper.basic.AbstractGroupWrapper;
import com.wkit.lost.mybatis.core.wrapper.basic.AbstractOrderWrapper;
import com.wkit.lost.mybatis.utils.ArrayUtil;
import com.wkit.lost.mybatis.utils.StringUtil;

import java.util.Collection;

/**
 * SQL片段管理器
 * @author wvkity
 */
public class SegmentManager implements Segment {

    private static final long serialVersionUID = 4487583124797797922L;

    /**
     * WHERE片段容器
     */
    private WhereSegmentWrapper whereWrapper = new WhereSegmentWrapper();

    /**
     * 分组片段容器
     */
    private GroupSegmentWrapper groupWrapper = new GroupSegmentWrapper();

    /**
     * 分组筛选片段容器
     */
    private HavingSegmentWrapper havingWrapper = new HavingSegmentWrapper();

    /**
     * 排序片段容器
     */
    private OrderSegmentWrapper orderWrapper = new OrderSegmentWrapper();

    /**
     * 添加多个条件对象
     * @param segments 条件对象数组
     * @return {@code this}
     */
    public SegmentManager add(Criterion<?>... segments) {
        return addCondition(ArrayUtil.toList(segments));
    }

    /**
     * 添加多个条件对象
     * @param segments 条件对象集合
     * @return {@code this}
     */
    public SegmentManager addCondition(Collection<Criterion<?>> segments) {
        this.whereWrapper.addAll(segments);
        return this;
    }

    /**
     * 添加多个分组对象
     * @param segments 分组对象数组
     * @return {@code this}
     */
    public SegmentManager add(AbstractGroupWrapper<?, ?>... segments) {
        return addGroup(ArrayUtil.toList(segments));
    }

    /**
     * 添加多个分组对象
     * @param segments 分组对象集合
     * @return {@code this}
     */
    public SegmentManager addGroup(Collection<AbstractGroupWrapper<?, ?>> segments) {
        this.groupWrapper.addAll(segments);
        return this;
    }

    /**
     * 添加多个排序对象
     * @param segments 排序对象数组
     * @return {@code this}
     */
    public SegmentManager add(AbstractOrderWrapper<?, ?>... segments) {
        return addOrder(ArrayUtil.toList(segments));
    }

    /**
     * 添加多个排序对象
     * @param segments 排序对象集合
     * @return {@code this}
     */
    public SegmentManager addOrder(Collection<AbstractOrderWrapper<?, ?>> segments) {
        this.orderWrapper.addAll(segments);
        return this;
    }

    /**
     * 检查是否存在SQL片段对象
     * @return boolean
     */
    public boolean hasSegment() {
        return this.whereWrapper.isNotEmpty() || this.groupWrapper.isNotEmpty()
                || this.havingWrapper.isNotEmpty() || this.orderWrapper.isNotEmpty();
    }

    @Override
    public String getSegment() {
        return getWhereSegment() + this.groupWrapper.getSegment() +
                this.havingWrapper.getSegment() + this.orderWrapper.getSegment();
    }

    /**
     * 获取SQL片段
     * @param replacement group分组替换字符串
     * @return SQL片段
     */
    public String getSegment(String replacement) {
        return StringUtil.hasText(replacement) ? (getWhereSegment() + replacement + this.havingWrapper.getSegment()
                + this.orderWrapper.getSegment()) : getSegment();
    }

    private String getWhereSegment() {
        return this.whereWrapper.getSegment();
    }
}
