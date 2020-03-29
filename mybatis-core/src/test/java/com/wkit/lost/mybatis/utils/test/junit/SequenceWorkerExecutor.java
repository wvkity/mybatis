package com.wkit.lost.mybatis.utils.test.junit;

import com.wkit.lost.mybatis.snowflake.worker.SequenceGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceWorkerExecutor implements Runnable {

    static final int MAX = 4000;
    static final CountDownLatch LATCH = new CountDownLatch( MAX );
    static final SequenceWorkerExecutor EXECUTOR = new SequenceWorkerExecutor();
    static final Set<Long> SEQUENCE_CACHE = Collections.synchronizedSet( new LinkedHashSet<>( MAX ) );
    static final List<String> REPEAT_SEQUENCE_CACHE = Collections.synchronizedList( new ArrayList<>() );
    static final AtomicInteger INDEX = new AtomicInteger();

    @Override
    public void run() {
        try {
            Thread.sleep( 1000 );
            System.out.println( "当前执行数：" + INDEX.incrementAndGet() );
            long id = SequenceGenerator.nextValue();
            if ( SEQUENCE_CACHE.contains( id ) ) {
                REPEAT_SEQUENCE_CACHE.add( SequenceGenerator.parse( id ) );
            }
            SEQUENCE_CACHE.add( id );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            LATCH.countDown();
        }
    }

    public static void main( String[] args ) {
        try {
            ExecutorService exec = Executors.newFixedThreadPool( 4000 );
            for ( int i = 0; i < 4000; i++ ) {
                exec.submit( EXECUTOR );
            }
            LATCH.await();
            System.out.println( "Fire!" );
            exec.shutdown();
            // 重复ID
            System.out.println( "检查是否出现重复数据: " + !REPEAT_SEQUENCE_CACHE.isEmpty() );
            if ( !REPEAT_SEQUENCE_CACHE.isEmpty() ) {
                REPEAT_SEQUENCE_CACHE.forEach( System.out::println );
            } else {
                SEQUENCE_CACHE.stream().map( SequenceGenerator::parse ).forEach( System.out::println );
                long size = SEQUENCE_CACHE.size();
                long oSize = SEQUENCE_CACHE.stream().filter( value -> value % 2 == 0 ).count();
                System.out.println( "偶数：" + oSize + " 奇数：" + ( size - oSize ) );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
