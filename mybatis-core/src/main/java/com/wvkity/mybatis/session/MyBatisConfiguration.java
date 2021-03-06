package com.wvkity.mybatis.session;

import com.wvkity.mybatis.binding.MyBatisMapperRegistry;
import com.wvkity.mybatis.config.MyBatisConfigCache;
import com.wvkity.mybatis.config.MyBatisCustomConfiguration;
import com.wvkity.mybatis.executor.MyBatisBatchExecutor;
import com.wvkity.mybatis.executor.MyBatisReuseExecutor;
import com.wvkity.mybatis.executor.MyBatisSimpleExecutor;
import com.wvkity.mybatis.executor.resultset.MyBatisResultSetHandler;
import com.wvkity.mybatis.reflection.wrapper.MyBatisObjectWrapperFactory;
import com.wvkity.mybatis.scripting.xmltags.MyBatisXMLLanguageDriver;
import com.wvkity.mybatis.type.handlers.StandardInstantTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardJapaneseDateTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardLocalDateTimeTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardLocalDateTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardLocalTimeTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardOffsetDateTimeTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardOffsetTimeTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardZonedDateTimeTypeHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseDate;

/**
 * {@inheritDoc}
 */
public class MyBatisConfiguration extends Configuration {

    private static final Log log = LogFactory.getLog(MyBatisConfiguration.class);

    protected final MyBatisMapperRegistry myBatisMapperRegistry = new MyBatisMapperRegistry(this);

    /**
     * 自定义配置
     */
    @Getter
    @Setter
    private MyBatisCustomConfiguration customConfiguration = MyBatisConfigCache.defaults();

    public MyBatisConfiguration() {
        super();
        setDefaultScriptingLanguage(MyBatisXMLLanguageDriver.class);
        // registry JDK8+ time api(JSR-310)
        TypeHandlerRegistry typeHandlerRegistry = this.getTypeHandlerRegistry();
        typeHandlerRegistry.register(Instant.class, StandardInstantTypeHandler.class);
        typeHandlerRegistry.register(JapaneseDate.class, StandardJapaneseDateTypeHandler.class);
        typeHandlerRegistry.register(LocalDateTime.class, StandardLocalDateTimeTypeHandler.class);
        typeHandlerRegistry.register(LocalDate.class, StandardLocalDateTypeHandler.class);
        typeHandlerRegistry.register(LocalTime.class, StandardLocalTimeTypeHandler.class);
        typeHandlerRegistry.register(OffsetDateTime.class, StandardOffsetDateTimeTypeHandler.class);
        typeHandlerRegistry.register(OffsetTime.class, StandardOffsetTimeTypeHandler.class);
        typeHandlerRegistry.register(ZonedDateTime.class, StandardZonedDateTimeTypeHandler.class);
        // 设置ObjectWrapperFactory对象
        this.objectWrapperFactory = new MyBatisObjectWrapperFactory();
        // 默认开启驼峰转换
        this.setMapUnderscoreToCamelCase(true);
    }

    public MyBatisConfiguration(Environment environment) {
        this();
        this.environment = environment;
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        if (mappedStatements.containsKey(ms.getId())) {
            // log.warn( "Mapper `" + ms.getId() + "` is ignored, because is's exists, maybe from xml file." );
            return;
        }
        super.addMappedStatement(ms);
    }

    @Override
    public void setDefaultScriptingLanguage(Class<? extends LanguageDriver> driver) {
        if (driver == null) {
            driver = MyBatisXMLLanguageDriver.class;
        }
        super.setDefaultScriptingLanguage(driver);
    }

    public MyBatisMapperRegistry getMyBatisMapperRegistry() {
        return this.myBatisMapperRegistry;
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        this.myBatisMapperRegistry.addMapper(type);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        this.myBatisMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        this.myBatisMapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.myBatisMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return this.myBatisMapperRegistry.hasMapper(type);
    }

    @Override
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement,
                                                RowBounds rowBounds, ParameterHandler parameterHandler,
                                                ResultHandler resultHandler, BoundSql boundSql) {
        ResultSetHandler resultSetHandler = new MyBatisResultSetHandler(executor, mappedStatement,
                parameterHandler, resultHandler, boundSql, rowBounds);
        resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }

    @Override
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new MyBatisBatchExecutor(this, transaction);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new MyBatisReuseExecutor(this, transaction);
        } else {
            executor = new MyBatisSimpleExecutor(this, transaction);
        }
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }
}
