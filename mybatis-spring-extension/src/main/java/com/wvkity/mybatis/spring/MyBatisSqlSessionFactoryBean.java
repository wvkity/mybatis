package com.wvkity.mybatis.spring;

import com.wvkity.mybatis.builder.xml.MyBatisXMLConfigBuilder;
import com.wvkity.mybatis.config.MyBatisConfigCache;
import com.wvkity.mybatis.config.MyBatisCustomConfiguration;
import com.wvkity.mybatis.core.data.auditing.MetadataAuditable;
import com.wvkity.mybatis.core.injector.Injector;
import com.wvkity.mybatis.core.parser.EntityParser;
import com.wvkity.mybatis.core.parser.FieldParser;
import com.wvkity.mybatis.core.snowflake.sequence.Sequence;
import com.wvkity.mybatis.keygen.GuidGenerator;
import com.wvkity.mybatis.keygen.KeyGenerator;
import com.wvkity.mybatis.plugins.batch.BatchParameterFilterInterceptor;
import com.wvkity.mybatis.plugins.batch.BatchStatementInterceptor;
import com.wvkity.mybatis.plugins.data.auditing.SystemBuiltinAuditingInterceptor;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import com.wvkity.mybatis.session.MyBatisSqlSessionFactoryBuilder;
import com.wvkity.mybatis.type.handlers.EnumSupport;
import com.wvkity.mybatis.type.handlers.EnumTypeHandler;
import com.wvkity.mybatis.type.handlers.StandardOffsetDateTimeTypeHandler;
import com.wvkity.mybatis.utils.ArrayUtil;
import com.wvkity.mybatis.utils.CollectionUtil;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class MyBatisSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean,
        ApplicationListener<ApplicationEvent> {

    private static final Logger log = LoggerFactory.getLogger(MyBatisSqlSessionFactoryBean.class);

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    private Resource configLocation;
    private MyBatisConfiguration configuration;
    private Resource[] mapperLocations;
    private DataSource dataSource;
    private TransactionFactory transactionFactory;
    private Properties configurationProperties;
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new MyBatisSqlSessionFactoryBuilder();
    private SqlSessionFactory sqlSessionFactory;
    //EnvironmentAware requires spring 3.1
    private String environment = MyBatisSqlSessionFactoryBean.class.getSimpleName();
    private boolean failFast;
    private Interceptor[] plugins;
    private TypeHandler<?>[] typeHandlers;
    private String typeHandlersPackage;
    private Class<?>[] typeAliases;
    private String typeAliasesPackage;
    private String typeEnumsPackage;
    private Class<?> typeAliasesSuperType;
    private DatabaseIdProvider databaseIdProvider;
    private Class<? extends VFS> vfs;
    private Cache cache;
    private ObjectFactory objectFactory;
    private ObjectWrapperFactory objectWrapperFactory;

    /**
     * 上下文对象
     */
    private ApplicationContext applicationContext;
    private DefaultListableBeanFactory beanFactory;
    private AutowireCapableBeanFactory autowireBeanFactory;

    /**
     * 自定义配置
     */
    private MyBatisCustomConfiguration customConfiguration;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(dataSource, "Property `dataSource` is required");
        notNull(sqlSessionFactoryBuilder, "Property `sqlSessionBuilder` is required");
        state((configuration == null && configLocation == null) || !(configuration != null && configLocation != null),
                "Property `configuration` and 'configLocation` can not specified with together");
        this.sqlSessionFactory = buildSqlSessionFactory();
    }

    /**
     * Build a {@code SqlSessionFactory} instance.
     * <p>
     * The default implementation uses the standard MyBatis {@code XMLConfigBuilder} API to build a
     * {@code SqlSessionFactory} instance based on an Reader.
     * Since 1.3.0, it can be specified a {@link MyBatisConfiguration} instance directly(without config file).
     * @return SqlSessionFactory
     * @throws Exception if configuration is failed
     */
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
        final MyBatisConfiguration targetConfiguration;
        // 
        MyBatisXMLConfigBuilder xmlConfigBuilder;
        if (this.configuration != null) {
            targetConfiguration = configuration;
            if (targetConfiguration.getVariables() == null) {
                targetConfiguration.setVariables(this.configurationProperties);
            } else if (this.configurationProperties != null) {
                targetConfiguration.getVariables().putAll(this.configurationProperties);
            }
            xmlConfigBuilder = null;
        } else if (this.configLocation != null) {
            xmlConfigBuilder = new MyBatisXMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
            targetConfiguration = xmlConfigBuilder.getConfiguration();
        } else {
            log.debug(() -> "Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");
            targetConfiguration = new MyBatisConfiguration();
            Optional.ofNullable(this.configurationProperties).ifPresent(targetConfiguration::setVariables);
            xmlConfigBuilder = null;
        }
        Optional.ofNullable(this.objectFactory).ifPresent(targetConfiguration::setObjectFactory);
        Optional.ofNullable(this.objectWrapperFactory).ifPresent(targetConfiguration::setObjectWrapperFactory);
        Optional.ofNullable(this.vfs).ifPresent(targetConfiguration::setVfsImpl);

        if (hasLength(this.typeAliasesPackage)) {
            scanClasses(this.typeAliasesPackage, this.typeAliasesSuperType)
                    .forEach(targetConfiguration.getTypeAliasRegistry()::registerAlias);
        }

        // 自定义枚举类型注册
        if (hasLength(this.typeEnumsPackage)) {
            Set<Class<?>> typeEnumsClasses = scanClasses(this.typeEnumsPackage, Class.class);
            if (!CollectionUtils.isEmpty(typeEnumsClasses)) {
                TypeHandlerRegistry typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
                String customClassName = EnumTypeHandler.class.getCanonicalName();
                String originalClassName = EnumOrdinalTypeHandler.class.getCanonicalName();
                for (Class<?> clazz : typeEnumsClasses) {
                    if (clazz.isEnum()) {
                        if (EnumSupport.class.isAssignableFrom(clazz)) {
                            // 自定义EnumTypeHandler注册
                            typeHandlerRegistry.register(clazz.getName(), customClassName);
                        } else {
                            // 原生EnumOrdinalTypeHandler注册
                            typeHandlerRegistry.register(clazz.getName(), originalClassName);
                        }
                    }
                }
            }
        }

        if (!isEmpty(this.typeAliases)) {
            Stream.of(this.typeAliases).forEach(typeAlias -> {
                targetConfiguration.getTypeAliasRegistry().registerAlias(typeAlias);
                log.debug(() -> "Registered type alias: `" + typeAlias + "`");
            });
        }

        if (this.customConfiguration == null) {
            this.customConfiguration = MyBatisConfigCache.defaults();
        }
        // SQL注入器
        ifPresent(Injector.class, customConfiguration::setInjector);
        // 实体解析器
        ifPresent(EntityParser.class, customConfiguration::setEntityParser);
        // 属性解析器
        ifPresent(FieldParser.class, customConfiguration::setFieldParser);
        // 主键生成器
        if (hasBeanFromContext(KeyGenerator.class)) {
            customConfiguration.setKeyGenerator(getBean(KeyGenerator.class));
        } else {
            customConfiguration.setKeyGenerator(new GuidGenerator());
        }
        // 雪花算法主键生成器
        ifPresent(Sequence.class, customConfiguration::setSequence);
        // 元数据审计
        ifPresent(MetadataAuditable.class, customConfiguration::setMetadataAuditable);
        targetConfiguration.setCustomConfiguration(this.customConfiguration);

        // 注册内置的插件
        List<Interceptor> interceptors = new ArrayList<>(ArrayUtil.toList(this.plugins));
        registerPlugins(this.customConfiguration, interceptors);
        if (CollectionUtil.hasElement(interceptors)) {
            this.plugins = interceptors.toArray(new Interceptor[0]);
            interceptors.forEach(it -> {
                targetConfiguration.addInterceptor(it);
                log.debug(() -> "Registered plugin: `" + it + "`");
            });
        }

        if (hasLength(this.typeHandlersPackage)) {
            scanClasses(this.typeAliasesPackage, TypeHandler.class).stream()
                    .filter(clazz -> !clazz.isInterface())
                    .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                    .filter(clazz -> ClassUtils.getConstructorIfAvailable(clazz) != null)
                    .forEach(targetConfiguration.getTypeHandlerRegistry()::register);
        }

        // 注册JDK8+ time api(JSR-310)
        if (targetConfiguration.getTypeHandlerRegistry()
                .getMappingTypeHandler(StandardOffsetDateTimeTypeHandler.class) == null) {
            Optional.of(scanClasses("com.wvkity.mybatis.type", TypeHandler.class)).ifPresent(classes ->
                    classes.stream().filter(clazz -> !clazz.isInterface())
                            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                            .filter(clazz -> ClassUtils.getConstructorIfAvailable(clazz) != null)
                            .forEach(targetConfiguration.getTypeHandlerRegistry()::register));
        }


        if (!isEmpty(this.typeHandlers)) {
            Stream.of(this.typeHandlers).forEach(typeHandler -> {
                targetConfiguration.getTypeHandlerRegistry().register(typeHandler);
                log.debug(() -> "Registered type handler: `" + typeHandler + "`");
            });
        }

        //fix #64 set databaseId before parse mapper xml
        if (this.databaseIdProvider != null) {
            try {
                targetConfiguration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch (SQLException e) {
                throw new NestedIOException("Failed getting a databaseId", e);
            }
        }

        Optional.ofNullable(this.cache).ifPresent(targetConfiguration::addCache);

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();
                log.debug(() -> "Parsed configuration file: `" + this.configLocation + "`");
            } catch (Exception e) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
        targetConfiguration.setEnvironment(new Environment(this.environment,
                this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory,
                this.dataSource));

        // 自定义操作(针对MyBatisCache)
        customConfiguration.cacheSelf(targetConfiguration);
        if (this.mapperLocations != null) {
            if (this.mapperLocations.length == 0) {
                log.warn(() -> "Property 'mapperLocations' was specified but matching resources are not found.");
            } else {
                for (Resource mapperLocation : this.mapperLocations) {
                    if (mapperLocation == null) {
                        continue;
                    }
                    try {
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
                                targetConfiguration, mapperLocation.toString(), targetConfiguration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    } catch (Exception e) {
                        throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                    } finally {
                        ErrorContext.instance().reset();
                    }
                    log.debug(() -> "Parsed mapper file: `" + mapperLocation + "`");
                }
            }
        } else {
            log.debug(() -> "Property 'mapperLocations' was not specified.");
        }
        SqlSessionFactory factory = this.sqlSessionFactoryBuilder.build(targetConfiguration);
        customConfiguration.setSqlSessionFactory(factory);
        return factory;
    }

    /**
     * 注册默认提供的插件
     * @param customConfiguration mybatis自定义配置
     * @param interceptorList     插件(拦截器)集合
     */
    private void registerPlugins(MyBatisCustomConfiguration customConfiguration, List<Interceptor> interceptorList) {
        // 存在多个插件，由于内部使用代理(代理类又被代理)，越是在外面优先级越高
        List<Class<? extends Interceptor>> plugins = customConfiguration.getPlugins();
        if (!CollectionUtils.isEmpty(plugins)) {
            for (Class<? extends Interceptor> plugin : new LinkedHashSet<>(plugins)) {
                if (plugin != null && pluginRegistrable(plugin)) {
                    Optional.ofNullable(newInstance(plugin))
                            .ifPresent(interceptor -> {
                                interceptorList.add(interceptor);
                                registerExistingInterceptorBean(interceptor);
                            });
                }
            }
        }
        /////// 注入内置拦截器 ///////
        if (customConfiguration.isAutoRegisterPlugin()) {
            // 默认审计插件(主键、逻辑删除)
            if (pluginRegistrable(SystemBuiltinAuditingInterceptor.class)) {
                Interceptor interceptor = new SystemBuiltinAuditingInterceptor();
                interceptorList.add(interceptor);
                registerExistingInterceptorBean(interceptor);
            }

            // 批量保存操作Statement插件
            if (pluginRegistrable(BatchStatementInterceptor.class)) {
                Interceptor interceptor = new BatchStatementInterceptor();
                interceptorList.add(interceptor);
                registerExistingInterceptorBean(interceptor);
            }

            // 批量保存操作参数拦截插件
            if (pluginRegistrable(BatchParameterFilterInterceptor.class)) {
                Interceptor interceptor = new BatchParameterFilterInterceptor();
                interceptorList.add(interceptor);
                registerExistingInterceptorBean(interceptor);
            }
        }
    }

    /**
     * 检查插件是否可注册
     * @param clazz 具体插件类
     * @param <T>   类型
     * @return true: 是 false: 否
     */
    private <T extends Interceptor> boolean pluginRegistrable(Class<T> clazz) {
        if (!ArrayUtil.isEmpty(this.plugins)) {
            for (Interceptor plugin : this.plugins) {
                if (plugin.getClass().isAssignableFrom(clazz)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 将{@link MyBatisCustomConfiguration}配置的插件注入到Spring容器中
     * @param existingInterceptor 插件(拦截器)
     */
    private void registerExistingInterceptorBean(Interceptor existingInterceptor) {
        if (this.beanFactory != null && existingInterceptor != null) {
            try {
                // 注入到Spring容器中
                String beanName = existingInterceptor.getClass().getSimpleName();
                this.beanFactory.registerSingleton((Character.toLowerCase(beanName.charAt(0)) +
                        beanName.substring(1)), existingInterceptor);
                // 注入依赖
                if (this.autowireBeanFactory != null) {
                    this.autowireBeanFactory.autowireBean(existingInterceptor);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private <T> T newInstance(Class<T> clazz) {
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    private <T> void ifPresent(Class<T> clazz, Consumer<T> consumer) {
        if (hasBeanFromContext(clazz)) {
            consumer.accept(getBean(clazz));
        }
    }

    private boolean hasBeanFromContext(final Class<?> target) {
        return this.applicationContext.getBeanNamesForType(target, false, false).length > 0;
    }

    private <T> T getBean(Class<T> clazz) {
        return this.applicationContext.getBean(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }
        return this.sqlSessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends SqlSessionFactory> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (failFast && event instanceof ContextRefreshedEvent) {
            // fail-fast -> check all statements are completed
            this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }
    }

    private Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
        Set<Class<?>> classes = new HashSet<>();
        String[] packagePatternArray = tokenizeToStringArray(packagePatterns, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (String packagePattern : packagePatternArray) {
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
            for (Resource resource : resources) {
                try {
                    ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                    Class<?> clazz = Resources.classForName(classMetadata.getClassName());
                    if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
                        classes.add(clazz);
                    }
                } catch (Throwable e) {
                    log.warn(() -> "Cannot load the `" + resource + "`. Cause by " + e.toString());
                }
            }
        }
        return classes;
    }

    /**
     * Sets the ObjectFactory.
     * @param objectFactory a custom ObjectFactory
     * @since 1.1.2
     */
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    /**
     * Sets the ObjectWrapperFactory.
     * @param objectWrapperFactory a specified ObjectWrapperFactory
     * @since 1.1.2
     */
    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    /**
     * Gets the DatabaseIdProvider
     * @return a specified DatabaseIdProvider
     * @since 1.1.0
     */
    public DatabaseIdProvider getDatabaseIdProvider() {
        return databaseIdProvider;
    }

    /**
     * Sets the DatabaseIdProvider.
     * As of version 1.2.2 this variable is not initialized by default.
     * @param databaseIdProvider a DatabaseIdProvider
     * @since 1.1.0
     */
    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        this.databaseIdProvider = databaseIdProvider;
    }

    /**
     * Gets the VFS.
     * @return a specified VFS
     */
    public Class<? extends VFS> getVfs() {
        return vfs;
    }

    /**
     * Sets the VFS.
     * @param vfs a VFS
     */
    public void setVfs(Class<? extends VFS> vfs) {
        this.vfs = vfs;
    }

    /**
     * Gets the Cache.
     * @return a specified Cache
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * Sets the Cache.
     * @param cache a Cache
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Mybatis plugin list.
     * @param plugins list of plugins
     * @since 1.0.1
     */
    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    /**
     * Packages to search for type aliases.
     * <p>Since 2.0.1, allow to specify a wildcard such as {@code com.example.*.model}.
     * @param typeAliasesPackage package to scan for domain objects
     * @since 1.0.1
     */
    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    /**
     * Super class which domain objects have to extend to have a type alias created.
     * No effect if there is no package to scan configured.
     * @param typeAliasesSuperType super class for domain objects
     * @since 1.1.2
     */
    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    /**
     * Packages to search for type handlers.
     * <p>Since 2.0.1, allow to specify a wildcard such as {@code com.example.*.typehandler}.
     * @param typeHandlersPackage package to scan for type handlers
     * @since 1.0.1
     */
    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    /**
     * Set type handlers. They must be annotated with {@code MappedTypes} and optionally with {@code MappedJdbcTypes}
     * @param typeHandlers Type handler list
     * @since 1.0.1
     */
    public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    /**
     * List of type aliases to register. They can be annotated with {@code Alias}
     * @param typeAliases Type aliases list
     * @since 1.0.1
     */
    public void setTypeAliases(Class<?>[] typeAliases) {
        this.typeAliases = typeAliases;
    }

    /**
     * If true, a final check is done on Configuration to assure that all mapped
     * statements are fully loaded and there is no one still pending to resolve
     * includes. Defaults to false.
     * @param failFast enable failFast
     * @since 1.0.1
     */
    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    /**
     * Set the location of the MyBatis {@code SqlSessionFactory} config file. A typical value is
     * "WEB-INF/mybatis-configuration.xml".
     * @param configLocation a location the MyBatis config file
     */
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * Set a customized MyBatis configuration.
     * @param configuration MyBatis configuration
     * @since 1.3.0
     */
    public void setConfiguration(MyBatisConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Set locations of MyBatis mapper files that are going to be merged into the {@code SqlSessionFactory}
     * configuration at runtime.
     * <p>
     * This is an alternative to specifying "&lt;sqlmapper&gt;" entries in an MyBatis config file.
     * This property being based on Spring's resource abstraction also allows for specifying
     * resource patterns here: e.g. "classpath*:sqlmap/*-mapper.xml".
     * @param mapperLocations location of MyBatis mapper files
     */
    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    /**
     * Set optional properties to be passed into the SqlSession configuration, as alternative to a
     * {@code &lt;properties&gt;} tag in the configuration xml file. This will be used to
     * resolve placeholders in the config file.
     * @param sqlSessionFactoryProperties optional properties for the SqlSessionFactory
     */
    public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
        this.configurationProperties = sqlSessionFactoryProperties;
    }

    /**
     * Set the JDBC {@code DataSource} that this instance should manage transactions for. The {@code DataSource}
     * should match the one used by the {@code SqlSessionFactory}: for example, you could specify the same
     * JNDI DataSource for both.
     * <p>
     * A transactional JDBC {@code Connection} for this {@code DataSource} will be provided to application code
     * accessing this {@code DataSource} directly via {@code DataSourceUtils} or {@code DataSourceTransactionManager}.
     * <p>
     * The {@code DataSource} specified here should be the target {@code DataSource} to manage transactions for, not
     * a {@code TransactionAwareDataSourceProxy}. Only data access code may work with
     * {@code TransactionAwareDataSourceProxy}, while the transaction manager needs to work on the
     * underlying target {@code DataSource}. If there's nevertheless a {@code TransactionAwareDataSourceProxy}
     * passed in, it will be unwrapped to extract its target {@code DataSource}.
     * @param dataSource a JDBC {@code DataSource}
     */
    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            // If we got a TransactionAwareDataSourceProxy, we need to perform
            // transactions for its underlying target DataSource, else data
            // access code won't see properly exposed transactions (i.e.
            // transactions for the target DataSource).
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }
    }

    /**
     * Sets the {@code SqlSessionFactoryBuilder} to use when creating the {@code SqlSessionFactory}.
     * <p>
     * This is mainly meant for testing so that mock SqlSessionFactory classes can be injected. By
     * default, {@code SqlSessionFactoryBuilder} creates {@code DefaultSqlSessionFactory} instances.
     * @param sqlSessionFactoryBuilder a SqlSessionFactoryBuilder
     */
    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    /**
     * Set the MyBatis TransactionFactory to use. Default is {@code SpringManagedTransactionFactory}
     * <p>
     * The default {@code SpringManagedTransactionFactory} should be appropriate for all cases:
     * be it Spring transaction management, EJB CMT or plain JTA. If there is no active transaction,
     * SqlSession operations will execute SQL statements non-transactionally.
     *
     * <b>It is strongly recommended to use the default {@code TransactionFactory}.</b> If not used, any
     * attempt at getting an SqlSession through Spring's MyBatis framework will throw an exception if
     * a transaction is active.
     * @param transactionFactory the MyBatis TransactionFactory
     * @see SpringManagedTransactionFactory
     */
    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    /**
     * <b>NOTE:</b> This class <em>overrides</em> any {@code Environment} you have set in the MyBatis
     * config file. This is used only as a placeholder name. The default value is
     * {@code SqlSessionFactoryBean.class.getSimpleName()}.
     * @param environment the environment name
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setTypeEnumsPackage(String typeEnumsPackage) {
        this.typeEnumsPackage = typeEnumsPackage;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private void initBeanFactory() {
        try {
            if (applicationContext instanceof GenericApplicationContext) {
                GenericApplicationContext context = ((GenericApplicationContext) applicationContext);
                this.beanFactory = context.getDefaultListableBeanFactory();
                try {
                    this.autowireBeanFactory = context.getAutowireCapableBeanFactory();
                } catch (Exception e) {
                    // ignore
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public void setCustomConfiguration(MyBatisCustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }
}

