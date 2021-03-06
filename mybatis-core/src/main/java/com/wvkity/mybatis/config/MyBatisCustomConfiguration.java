package com.wvkity.mybatis.config;

import com.wvkity.mybatis.annotation.extension.Dialect;
import com.wvkity.mybatis.annotation.naming.NamingStrategy;
import com.wvkity.mybatis.core.data.auditing.MetadataAuditable;
import com.wvkity.mybatis.core.injector.Injector;
import com.wvkity.mybatis.core.metadata.PrimaryKey;
import com.wvkity.mybatis.core.naming.DefaultPhysicalNamingStrategy;
import com.wvkity.mybatis.core.naming.PhysicalNamingStrategy;
import com.wvkity.mybatis.core.parser.EntityParser;
import com.wvkity.mybatis.core.parser.FieldParser;
import com.wvkity.mybatis.core.snowflake.sequence.Sequence;
import com.wvkity.mybatis.keygen.KeyGenerator;
import com.wvkity.mybatis.plugins.batch.BatchParameterFilterInterceptor;
import com.wvkity.mybatis.plugins.batch.BatchStatementInterceptor;
import com.wvkity.mybatis.plugins.data.auditing.SystemBuiltinAuditingInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.util.List;

/**
 * MyBatis自定义配置
 * @author wvkity
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class MyBatisCustomConfiguration implements Serializable {

    private static final long serialVersionUID = -3928045766093460184L;

    /**
     * 数据库类型
     */
    private Dialect dialect;

    /**
     * 当前{@link org.apache.ibatis.session.Configuration}的{@link SqlSessionFactory}
     */
    private SqlSessionFactory sqlSessionFactory;

    /**
     * SqlSession对象
     */
    private SqlSession sqlSession;

    /**
     * 实体解析器
     */
    private EntityParser entityParser;

    /**
     * 属性解析器
     */
    private FieldParser fieldParser;

    /**
     * SQL注入器
     */
    private Injector injector;

    /**
     * 命名策略
     */
    private NamingStrategy strategy;

    /**
     * 命名处理
     */
    private PhysicalNamingStrategy physicalNamingStrategy = new DefaultPhysicalNamingStrategy();

    /**
     * 主键生成器执行类型
     */
    private boolean before;

    /**
     * 表名前缀
     */
    private String tablePrefix;

    /**
     * 数据库catalog
     */
    private String catalog;

    /**
     * 数据库schema
     */
    private String schema;

    /**
     * 全局主键生成方式
     */
    private PrimaryKey primaryKey;

    /**
     * 自动识别主键(缺省@Id注解时)
     */
    private boolean autoScanPrimaryKey = false;

    /**
     * 自动识别审计字段(缺省相关审计注解时[如: @CreatedDate, @CreateUser, ...]))
     */
    private boolean autoScanAuditProperty = false;

    /**
     * 布尔值属性映射的字段是否自动添加is前缀
     */
    private boolean autoAddIsPrefixedWithBooleanProperty = true;

    /**
     * 是否自动注册内置插件
     */
    private boolean autoRegisterPlugin = true;

    /**
     * 全局主键标识(用于自动识别主键)
     */
    private String[] primaryKeys = {"id"};

    /**
     * 主键接口
     */
    private KeyGenerator keyGenerator;

    /**
     * 使用简单类型
     */
    private boolean useSimpleType = true;

    /**
     * 是否使用Java类型
     */
    private boolean useJavaType;

    /**
     * JDBC类型自动映射(缺省jdbcType)
     */
    private boolean jdbcTypeAutoMapping;

    /**
     * 枚举类型学转简单类型
     */
    private boolean enumAsSimpleType;

    /**
     * 关键字
     */
    private String wrapKeyWord = "";

    /**
     * 非空判断
     */
    private boolean checkNotEmpty = true;

    /**
     * 是否启用方法上的注解
     */
    private boolean enableMethodAnnotation = false;

    /**
     * Mybatis插件
     */
    private List<Class<? extends Interceptor>> plugins;

    /**
     * 逻辑删除字段
     */
    private String logicDeletedProperty;

    /**
     * 逻辑删除真值
     */
    private String logicDeletedTrueValue = "1";

    /**
     * 逻辑删除假值
     */
    private String logicDeletedFalseValue = "0";

    /**
     * 主键序列(雪花算法)
     */
    private Sequence sequence;

    /**
     * 元数据审计处理器
     */
    private MetadataAuditable metadataAuditable;

    /**
     * 根据属性查找字段不匹配是否抛出异常
     */
    private boolean notMatchingWithThrows = true;

    /**
     * 缓存当前对象
     * @param factory {@link SqlSessionFactory}
     * @return {@link SqlSessionFactory}
     */
    public SqlSessionFactory cacheSelf(SqlSessionFactory factory) {
        if (factory != null) {
            cacheSelf(factory.getConfiguration());
        }
        return factory;
    }

    /**
     * 缓存当前对象
     * @param configuration {@link Configuration}
     */
    public void cacheSelf(Configuration configuration) {
        if (configuration != null) {
            MyBatisConfigCache.cacheCustomConfiguration(configuration, this);
        }
    }
}
