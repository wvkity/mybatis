package com.wkit.lost.mybatis.config;

import com.wkit.lost.mybatis.annotation.extension.Dialect;
import com.wkit.lost.mybatis.annotation.naming.NamingStrategy;
import com.wkit.lost.mybatis.core.injector.Injector;
import com.wkit.lost.mybatis.core.injector.DefaultInjector;
import com.wkit.lost.mybatis.exception.MapperException;
import com.wkit.lost.mybatis.exception.MyBatisException;
import com.wkit.lost.mybatis.session.MyBatisConfiguration;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * MyBatis自定义配置缓存
 * @author wvkity
 */
@Log4j2
public class MyBatisConfigCache {

    /**
     * 全局配置信息缓存
     */
    private static final Map<String, MyBatisCustomConfiguration> CONFIGURATION_CACHE = new ConcurrentHashMap<>();

    /**
     * 接口注册
     */
    private static final Map<String, Set<String>> MAPPER_INTERFACE_CACHE = new ConcurrentHashMap<>( 128 );

    /**
     * 默认配置
     */
    private static final MyBatisCustomConfiguration DEFAULT_CONFIG = defaults();

    /**
     * 默认配置
     * @return 自定义配置对象
     */
    public static MyBatisCustomConfiguration defaults() {
        MyBatisCustomConfiguration config = new MyBatisCustomConfiguration();
        config.setDialect( Dialect.MYSQL );
        config.setStrategy( NamingStrategy.CAMEL_HUMP_UPPERCASE );
        return config;
    }

    /**
     * 获取自定义配置对象
     * @param configuration MyBatis配置
     * @return {@link MyBatisConfigCache}
     */
    public static MyBatisCustomConfiguration getCustomConfiguration( final Configuration configuration ) {
        if ( configuration == null ) {
            throw new MapperException( "The configuration object cannot be empty. You need initialize Configuration." );
        }
        if ( configuration instanceof MyBatisConfiguration ) {
            return ( ( MyBatisConfiguration ) configuration ).getCustomConfiguration();
        }
        return getCustomConfiguration( configuration.toString() );
    }

    /**
     * 获取自定义配置对象
     * @param mark MyBatis配置标识
     * @return {@link MyBatisConfigCache}
     */
    public static MyBatisCustomConfiguration getCustomConfiguration( final String mark ) {
        MyBatisCustomConfiguration cache = CONFIGURATION_CACHE.get( mark );
        if ( cache == null ) {
            CONFIGURATION_CACHE.putIfAbsent( mark, DEFAULT_CONFIG );
            log.debug( "MyBatis custom configuration initializing: `{}`", DEFAULT_CONFIG );
            return CONFIGURATION_CACHE.get( mark );
        }
        return cache;
    }

    /**
     * 缓存全局配置
     * @param configuration       MyBatis配置
     * @param customConfiguration 自定义全局配置
     */
    public static void cacheCustomConfiguration( final Configuration configuration, MyBatisCustomConfiguration customConfiguration ) {
        if ( configuration == null || customConfiguration == null ) {
            throw new MyBatisException( "Mybatis configuration object cannot be empty, please initialize it first" );
        }
        if ( configuration instanceof MyBatisConfiguration ) {
            // 覆盖原有的自定义配置
            ( ( MyBatisConfiguration ) configuration ).setCustomConfiguration( customConfiguration );
        } else {
            CONFIGURATION_CACHE.put( configuration.toString(), customConfiguration );
        }
    }

    /**
     * 缓存接口
     * @param configuration   配置对象
     * @param mapperInterface 接口
     */
    public static void registryMapperInterface( final Configuration configuration, final Class<?> mapperInterface ) {
        String mark = configuration.toString();
        Set<String> cache = MAPPER_INTERFACE_CACHE.get( mark );
        if ( cache != null ) {
            cache.add( mapperInterface.getName() );
        } else {
            Set<String> newCache = new ConcurrentSkipListSet<>();
            newCache.add( mapperInterface.getName() );
            MAPPER_INTERFACE_CACHE.putIfAbsent( mark, newCache );
        }
    }

    /**
     * 获取Mapper接口注册缓存信息
     * @param configuration MyBatis配置对象
     * @return Mapper接口注册缓存信息
     */
    public static Set<String> getMapperInterfaceCache( final Configuration configuration ) {
        String mark = configuration.toString();
        Set<String> cache = MAPPER_INTERFACE_CACHE.get( mark );
        if ( cache != null ) {
            return cache;
        } else {
            MAPPER_INTERFACE_CACHE.putIfAbsent( mark, new ConcurrentSkipListSet<>() );
            return MAPPER_INTERFACE_CACHE.get( mark );
        }
    }

    /**
     * 获取SQL注入器
     * @param configuration 配置信息
     * @return {@link Injector}
     */
    public static Injector getInjector( final Configuration configuration ) {
        MyBatisCustomConfiguration customConfiguration = getCustomConfiguration( configuration );
        Injector injector = customConfiguration.getInjector();
        if ( injector != null ) {
            return injector;
        } else {
            Injector instance = new DefaultInjector();
            customConfiguration.setInjector( instance );
            return instance;
        }
    }

    /**
     * 注入CURD-SQL
     * @param assistant       构建器
     * @param mapperInterface Mapper接口
     */
    public static void inject( final MapperBuilderAssistant assistant, Class<?> mapperInterface ) {
        Configuration configuration = assistant.getConfiguration();
        Optional.ofNullable( getCustomConfiguration( configuration ).getBaseMapperClass() ).ifPresent( it -> {
            if ( it.isAssignableFrom( mapperInterface ) ) {
                getInjector( configuration ).inject( assistant, mapperInterface );
            }
        } );
    }

    /**
     * 获取数据库类型
     * @param configuration 配置对象
     * @return 数据类型
     */
    public static Dialect getDialect( final Configuration configuration ) {
        return getCustomConfiguration( configuration ).getDialect();
    }
}
