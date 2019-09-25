package com.wkit.lost.mybatis.snowflake.factory;

import com.wkit.lost.mybatis.snowflake.sequence.Sequence;
import com.wkit.lost.mybatis.snowflake.worker.WorkerAssigner;

/**
 * ID序列工厂接口
 * @author DT
 */
public interface SequenceFactory {

    /**
     * 构建ID序列实例(默认根据MAC地址分配workerId、dataCenterId)
     * @return ID序列实例
     */
    Sequence build();

    /**
     * 构建ID序列实例
     * @param workerAssigner 分配器
     * @return ID序列实例
     */
    Sequence build( WorkerAssigner workerAssigner );

    /**
     * 构建ID序列
     * @param workerId     机器标识ID
     * @param dataCenterId 数据中心ID
     * @return ID序列
     */
    Sequence build( long workerId, long dataCenterId );
}
