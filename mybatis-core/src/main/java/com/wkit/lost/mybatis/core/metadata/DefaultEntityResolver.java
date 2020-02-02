package com.wkit.lost.mybatis.core.metadata;

import com.wkit.lost.mybatis.annotation.ColumnExt;
import com.wkit.lost.mybatis.annotation.Entity;
import com.wkit.lost.mybatis.annotation.GeneratedValue;
import com.wkit.lost.mybatis.annotation.Identity;
import com.wkit.lost.mybatis.annotation.LogicDeletion;
import com.wkit.lost.mybatis.annotation.OrderBy;
import com.wkit.lost.mybatis.annotation.SequenceGenerator;
import com.wkit.lost.mybatis.annotation.Transient;
import com.wkit.lost.mybatis.annotation.Version;
import com.wkit.lost.mybatis.annotation.SnowflakeSequence;
import com.wkit.lost.mybatis.annotation.auditing.CreatedDate;
import com.wkit.lost.mybatis.annotation.auditing.CreatedUser;
import com.wkit.lost.mybatis.annotation.auditing.CreatedUserName;
import com.wkit.lost.mybatis.annotation.auditing.DeletedDate;
import com.wkit.lost.mybatis.annotation.auditing.DeletedUser;
import com.wkit.lost.mybatis.annotation.auditing.DeletedUserName;
import com.wkit.lost.mybatis.annotation.auditing.LastModifiedDate;
import com.wkit.lost.mybatis.annotation.auditing.LastModifiedUser;
import com.wkit.lost.mybatis.annotation.auditing.LastModifiedUserName;
import com.wkit.lost.mybatis.annotation.extension.Dialect;
import com.wkit.lost.mybatis.annotation.extension.Executing;
import com.wkit.lost.mybatis.annotation.extension.GenerationType;
import com.wkit.lost.mybatis.annotation.extension.UseJavaType;
import com.wkit.lost.mybatis.annotation.extension.Validated;
import com.wkit.lost.mybatis.annotation.naming.Naming;
import com.wkit.lost.mybatis.annotation.naming.NamingStrategy;
import com.wkit.lost.mybatis.config.MyBatisCustomConfiguration;
import com.wkit.lost.mybatis.core.criteria.PropertyMappingForLambda;
import com.wkit.lost.mybatis.exception.MapperParserException;
import com.wkit.lost.mybatis.handler.ColumnHandler;
import com.wkit.lost.mybatis.javax.JavaxPersistence;
import com.wkit.lost.mybatis.keyword.SqlKeyWords;
import com.wkit.lost.mybatis.naming.DefaultPhysicalNamingStrategy;
import com.wkit.lost.mybatis.naming.PhysicalNamingStrategy;
import com.wkit.lost.mybatis.resolver.EntityResolver;
import com.wkit.lost.mybatis.resolver.FieldResolver;
import com.wkit.lost.mybatis.type.handlers.SimpleTypeRegistry;
import com.wkit.lost.mybatis.type.registry.JdbcTypeMappingRegister;
import com.wkit.lost.mybatis.utils.AnnotationUtil;
import com.wkit.lost.mybatis.utils.ArrayUtil;
import com.wkit.lost.mybatis.utils.Ascii;
import com.wkit.lost.mybatis.utils.Constants;
import com.wkit.lost.mybatis.utils.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * 默认实体-表映射解析器
 * @author wvkity
 */
@Deprecated
@Log4j2
public class DefaultEntityResolver implements EntityResolver {

    /**
     * 雪花算法字符串主键
     */
    private static final Set<String> WORKER_KEYS = new HashSet<>( Arrays.asList( "WORKER_SEQUENCE",
            "WORKER_SEQUENCE_STRING", Constants.GENERATOR_SNOWFLAKE_SEQUENCE_STRING ) );

    /**
     * 自定义配置
     */
    private MyBatisCustomConfiguration configuration;

    /**
     * 命名策略
     */
    private NamingStrategy strategy;

    /**
     * 命名处理器
     */
    private PhysicalNamingStrategy physicalNamingStrategy;

    /**
     * 属性解析器
     */
    private FieldResolver fieldResolver;

    /**
     * 构造方法
     * @param configuration 配置
     */
    public DefaultEntityResolver( MyBatisCustomConfiguration configuration ) {
        this.configuration = configuration;
        init();
    }

    private void init() {
        // 命名策略处理器
        if ( configuration == null ) {
            this.physicalNamingStrategy = new DefaultPhysicalNamingStrategy();
            this.fieldResolver = new DefaultFieldResolver();
        } else {
            this.physicalNamingStrategy = Optional.ofNullable( configuration.getPhysicalNamingStrategy() ).orElse( new DefaultPhysicalNamingStrategy() );
            // 类属性解析器
            this.fieldResolver = Optional.ofNullable( configuration.getFieldResolver() ).orElse( new DefaultFieldResolver() );
            if ( configuration.getFieldResolver() == null ) {
                configuration.setFieldResolver( fieldResolver );
            }
        }
    }

    @Override
    public Table resolve( Class<?> entity, String namespace ) {
        if ( entity == null ) {
            throw new MapperParserException( "The entity class parameter cannot be empty." );
        }
        // 命名策略
        NamingStrategy strategy = configuration.getStrategy();
        // 检查实体类是否存在@Naming注解
        if ( AnnotationUtil.isAnnotationPresent( entity, Naming.class ) ) {
            // 优先级高于全局配置的命名策略
            Naming naming = entity.getAnnotation( Naming.class );
            strategy = naming.value();
        }
        if ( strategy == null ) {
            strategy = NamingStrategy.CAMEL_HUMP_UPPERCASE;
        }
        this.strategy = strategy;
        // 解析@Table注解(处理表映射信息)
        Table table = processTableMappingFromEntity( entity );
        table.setNamespace( namespace );
        // 处理字段映射信息
        List<Field> fields;
        if ( configuration.isEnableMethodAnnotation() ) {
            fields = ColumnHandler.merge( entity, fieldResolver );
        } else {
            fields = ColumnHandler.getAllAttributes( entity, fieldResolver );
        }
        boolean enableAutoJdbcMapping = this.configuration.isJdbcTypeAutoMapping();
        fields.stream().filter( this::filter )
                .forEach( attribute -> processAttribute( table, attribute, enableAutoJdbcMapping ) );
        // 初始化定义信息
        table.initDefinition();
        // 缓存映射信息
        PropertyMappingForLambda.createCache( table );
        return table;
    }

    /**
     * 处理表映射信息
     * @param entity 实体类
     * @return 表映射信息
     */
    private Table processTableMappingFromEntity( final Class<?> entity ) {
        String tableName = null;
        String catalog = null;
        String schema = null;
        String prefix = null;
        // 检查是否存在注解
        if ( AnnotationUtil.hasAnnotation( entity ) ) {
            // 自定义@Table注解
            if ( entity.isAnnotationPresent( com.wkit.lost.mybatis.annotation.Table.class ) ) {
                com.wkit.lost.mybatis.annotation.Table tableAnnotation =
                        entity.getDeclaredAnnotation( com.wkit.lost.mybatis.annotation.Table.class );
                tableName = tableAnnotation.name();
                catalog = tableAnnotation.catalog();
                schema = tableAnnotation.schema();
                prefix = tableAnnotation.prefix();
            } else {
                // javax定义的@Table注解
                if ( AnnotationUtil.isAnnotationPresent( entity, JavaxPersistence.TABLE ) ) {
                    AnnotationMetadata metaObject = AnnotationMetadata.forObject( entity, JavaxPersistence.TABLE );
                    tableName = metaObject.stringValue( "name" );
                    catalog = metaObject.stringValue( "catalog" );
                    schema = metaObject.stringValue( "schema" );
                }
            }
            //  解析@Entity注解
            if ( Ascii.isNullOrEmpty( tableName ) ) {
                tableName = processEntityAnnotation( entity );
            }
        }
        if ( StringUtil.isBlank( catalog ) ) {
            catalog = this.configuration.getCatalog();
        }
        if ( StringUtil.isBlank( schema ) ) {
            schema = this.configuration.getSchema();
        }
        if ( StringUtil.isBlank( tableName ) ) {
            tableName = entity.getSimpleName();
        }
        tableName = transformStrategy( true, tableName, this.configuration );
        // 表名前缀
        if ( StringUtil.isBlank( prefix ) ) {
            prefix = this.configuration.getTablePrefix();
        }
        String refer = this.strategy.name().toUpperCase( Locale.ENGLISH );
        int mode = refer.contains( "LOWERCASE" ) ? 0 : refer.contains( "UPPERCASE" ) ? 1 : 2;
        // 表名前缀
        prefix = Optional.ofNullable( prefix ).map( it -> mode == 0 ? it.toLowerCase( Locale.ENGLISH ) :
                mode == 1 ? it.toUpperCase( Locale.ENGLISH ) : it ).orElse( "" );
        Table table = new Table( ( prefix + tableName ), catalog, schema );
        table.setEntity( entity ).setPrefix( prefix );
        return table;
    }

    /**
     * 处理@Entity注解
     * @param entity 实体类
     * @return 表名
     */
    protected String processEntityAnnotation( final Class<?> entity ) {
        if ( entity.isAnnotationPresent( Entity.class ) ) {
            return entity.getDeclaredAnnotation( Entity.class ).name();
        } else if ( AnnotationUtil.isAnnotationPresent( entity, JavaxPersistence.ENTITY ) ) {
            return AnnotationMetadata.forObject( entity, JavaxPersistence.ENTITY ).stringValue( "name" );
        }
        return null;
    }

    /**
     * 处理属性
     * @param table                 表映射对象
     * @param field                 属性对象
     * @param enableAutoJdbcMapping 是否开启自动映射JDBC类型
     */
    private void processAttribute( final Table table, final Field field, final boolean enableAutoJdbcMapping ) {
        if ( field.isAnnotationPresent( Transient.class, JavaxPersistence.TRANSIENT ) ) {
            return;
        }
        // 处理@Column注解
        Column column = processColumnAnnotation( table.getEntity(), field, enableAutoJdbcMapping );
        //var column = new Column( table.getEntity() );
        column.setField( field );
        // 检查是否存在@Id或@Worker注解
        if ( field.isAnnotationPresentOfId() ) {
            // 检查是否存在主键
            if ( table.hasPrimaryKey() ) {
                // 组合主键需要清空主键
                table.setPrimaryKey( null );
            } else {
                // 设置主键
                column.setPrimaryKey( true );
                table.setPrimaryKey( column );
            }
        }
        // 处理@LogicDelete注解
        processLogicDeleteAnnotation( table, column, field, configuration.getLogicDeletedProperty() );
        // 处理排序
        processOrderByAnnotation( table, column, field );
        // 处理主键策略
        processKeyGenerator( table, column, field );
        // 非主动标识主键则根据全局配置自动识别主键
        if ( !column.isPrimaryKey() ) {
            processAutoDiscernPrimaryKey( table, column, field );
        }
        // 处理审计注解
        processAuditAnnotation( column, field );
        // 检查是否为主键
        if ( column.isPrimaryKey() ) {
            column.setUpdatable( false );
            table.addPrimaryKey( column );
            // 全局配置-ID值生成方式
            processCustomKeyGenerator( table, column );
        }
        // 乐观锁
        if ( field.isAnnotationPresent( Version.class, JavaxPersistence.VERSION ) ) {
            if ( table.getOptimisticLockerColumn() == null ) {
                column.setVersion( true );
                table.setOptimisticLockerColumn( column );
            } else {
                log.warn( "The current Entity class({}) already has a `{}` for the @version tag. " +
                                "The framework only supports one optimistic lock. The system will ignore other " +
                                "@version tag properties ({}).", table.getEntity().getCanonicalName(),
                        table.getOptimisticLockerColumn().getProperty(), column.getProperty() );
            }
        }
        table.addColumn( column );
    }

    /**
     * 处理属性上的{@code @Column}注解
     * @param entity                实体类
     * @param field                 属性对象
     * @param enableAutoJdbcMapping 是否开启自动映射JDBC类型
     */
    private Column processColumnAnnotation( final Class<?> entity, final Field field, final boolean enableAutoJdbcMapping ) {
        String columnName = null;
        boolean insertable = true;
        boolean updatable = true;
        JdbcType jdbcType = null;
        Class<? extends TypeHandler<?>> typeHandler = null;
        boolean checkNotEmpty = this.configuration.isCheckNotEmpty();
        boolean useJavaType = this.configuration.isUseJavaType();
        boolean blob = false;
        if ( field.isAnnotationPresent( com.wkit.lost.mybatis.annotation.Column.class ) ) {
            // 自定义@Column注解
            com.wkit.lost.mybatis.annotation.Column columnAnnotation = field.getAnnotation( com.wkit.lost.mybatis.annotation.Column.class );
            insertable = columnAnnotation.insertable();
            updatable = columnAnnotation.updatable();
            columnName = columnAnnotation.name();
        } else if ( field.isAnnotationPresent( JavaxPersistence.COLUMN ) ) {
            AnnotationMetadata metaObject = AnnotationMetadata.forObject( field.getField(), JavaxPersistence.COLUMN );
            // JPA @Column注解
            insertable = metaObject.booleanValue( "insertable", true );
            updatable = metaObject.booleanValue( "updatable", true );
            columnName = metaObject.stringValue( "name" );
        }
        // 处理扩展注解
        if ( field.isAnnotationPresent( ColumnExt.class ) ) {
            ColumnExt columnExt = field.getAnnotation( ColumnExt.class );
            blob = columnExt.blob();
            if ( StringUtil.isBlank( columnName ) && StringUtil.hasText( columnExt.column() ) ) {
                columnName = columnExt.column();
            }
            // JDBC类型
            if ( columnExt.jdbcType() != JdbcType.UNDEFINED ) {
                jdbcType = columnExt.jdbcType();
            }
            if ( columnExt.typeHandler() != UnknownTypeHandler.class ) {
                typeHandler = columnExt.typeHandler();
            }
            // 字符串类型空值校验
            Validated validate = columnExt.empty();
            if ( validate != Validated.CONFIG ) {
                checkNotEmpty = validate == Validated.REQUIRED;
            }
            // 使用JAVA类型
            UseJavaType using = columnExt.useJavaType();
            if ( using != UseJavaType.CONFIG ) {
                useJavaType = using == UseJavaType.REQUIRED;
            }
        }
        if ( jdbcType == null && enableAutoJdbcMapping ) {
            // 开启自动映射
            jdbcType = JdbcTypeMappingRegister.getJdbcType( field.getJavaType() );
        }
        if ( StringUtil.isBlank( columnName ) ) {
            columnName = field.getName();
        }
        // 字段名策略处理
        columnName = transformStrategy( false, columnName, this.configuration );
        // 关键字处理
        String wrapKeyWord = this.configuration.getWrapKeyWord();
        if ( StringUtil.hasText( wrapKeyWord ) && SqlKeyWords.containsWord( columnName ) ) {
            columnName = MessageFormat.format( wrapKeyWord, columnName );
        }
        Column column = new Column( entity, field.getName(), columnName );
        column.setInsertable( insertable ).setUpdatable( updatable );
        column.setBlob( blob ).setCheckNotEmpty( checkNotEmpty );
        column.setJdbcType( jdbcType ).setTypeHandler( typeHandler );
        column.setJavaType( field.getJavaType() ).setUseJavaType( useJavaType );
        // 使用基本类型警告
        if ( column.getJavaType().isPrimitive() ) {
            log.warn( "Warning: The `{}` attribute in the `{}` entity is defined as a primitive type. " +
                            "The primitive type is not null at any time in dynamic SQL because it has a default value. It is " +
                            "recommended to modify the primitive type to the corresponding wrapper type!", column.getProperty(),
                    column.getEntity().getCanonicalName() );
        }
        return column;
    }

    /**
     * 解析逻辑删除注解
     * <p>{@link LogicDeletion @LogicDeletion}优先级高于全局配置</p>
     * @param table               实体类
     * @param column              字段映射对象
     * @param field               属性对象
     * @param logicDeleteProperty 全局逻辑删除属性
     */
    private void processLogicDeleteAnnotation( Table table, Column column, Field field, String logicDeleteProperty ) {
        if ( field.isAnnotationPresent( LogicDeletion.class )
                || column.getProperty().equals( logicDeleteProperty ) ) {
            if ( table.isEnableLogicDeletion() ) {
                throw new MapperParserException( "There are already `" + table.getLogicDeletionColumn()
                        .getProperty() + "` attributes in `" + table.getEntity().getName()
                        + "` entity class identified as logical deleted. Only one deleted attribute " +
                        "can exist in an entity class. Please check the entity class attributes." );
            }
            // @LogicDeletion优先级大于全局配置
            LogicDeletion logicDeletion = field.getAnnotation( LogicDeletion.class );
            String deletedValue, notDeletedValue;
            if ( logicDeletion != null ) {
                deletedValue = Optional.of( logicDeletion.trueValue() )
                        .filter( StringUtil::hasText )
                        .orElseGet( configuration::getLogicDeletedTrueValue );
                notDeletedValue = Optional.of( logicDeletion.falseValue() )
                        .filter( StringUtil::hasText )
                        .orElseGet( configuration::getLogicDeletedFalseValue );
            } else {
                deletedValue = configuration.getLogicDeletedTrueValue();
                notDeletedValue = configuration.getLogicDeletedFalseValue();
            }
            column.setLogicDelete( true ).setLogicDeletedTrueValue( deletedValue )
                    .setLogicDeletedFalseValue( notDeletedValue );
            table.setEnableLogicDeletion( true ).setLogicDeletionColumn( column );
        }
    }

    /**
     * 处理属性上的{@code @OrderBy}注解
     * @param table  表映射信息对象
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processOrderByAnnotation( final Table table, final Column column, final Field field ) {
        String orderValue = null;
        if ( field.isAnnotationPresent( OrderBy.class ) ) {
            orderValue = field.getAnnotation( OrderBy.class ).value();
        } else if ( field.isAnnotationPresent( JavaxPersistence.ORDER_BY ) ) {
            orderValue = AnnotationMetadata.forObject( field.getField(), JavaxPersistence.ORDER_BY ).stringValue();
        }
        if ( orderValue != null ) {
            orderValue = "".equals( StringUtil.strip( orderValue ) ) ? "ASC" : orderValue;
            column.setOrderBy( orderValue );
        }
    }

    /**
     * 处理主键生成策略
     * @param table  表映射信息对象
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processKeyGenerator( final Table table, final Column column, final Field field ) {
        if ( field.isAnnotationPresent( Identity.class ) ) {
            // @Identity优先级最高
            processIdentityAnnotation( table, column, field );
        } else if ( field.isAnnotationPresent( SequenceGenerator.class, JavaxPersistence.SEQUENCE_GENERATOR ) ) {
            // @SequenceGenerator序列
            processSequenceGeneratorAnnotation( table, column, field );
        } else if ( field.isAnnotationPresent( GeneratedValue.class, JavaxPersistence.GENERATED_VALUE ) ) {
            // @GeneratedValue
            processGeneratedValueAnnotation( table, column, field );
        } else if ( field.isAnnotationPresent( SnowflakeSequence.class ) ) {
            SnowflakeSequence snowflakeSequence = field.getAnnotation( SnowflakeSequence.class );
            if ( snowflakeSequence.value() ) {
                column.setSnowflakeSequenceString( true );
            } else {
                column.setSnowflakeSequence( true );
            }
        }
    }

    /**
     * 处理{@code @Identity}注解
     * @param table  表映射信息对象
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processIdentityAnnotation( final Table table, final Column column, final Field field ) {
        Identity identity = field.getAnnotation( Identity.class );
        if ( identity.useJdbc() ) {
            column.setIdentity( true ).setGenerator( "JDBC" );
            table.addPrimaryKeyProperty( column.getProperty() );
        } else if ( identity.dialect() != Dialect.UNDEFINED ) {
            column.setIdentity( true ).setExecuting( Executing.AFTER ).setGenerator( identity.dialect().getKeyGenerator() );
        } else {
            if ( StringUtil.isBlank( identity.sql() ) ) {
                throw new MapperParserException( StringUtil.format( "The @identity annotation on the '{}' " +
                                "class's attribute '{}' is invalid",
                        column.getEntity().getCanonicalName(), column.getProperty() ) );
            }
            column.setIdentity( true ).setExecuting( identity.executing() ).setGenerator( identity.sql() );
        }
    }

    /**
     * 处理{@code @SequenceGenerator}注解
     * @param table  表映射信息对象
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processSequenceGeneratorAnnotation( final Table table, final Column column, final Field field ) {
        String sequenceName = null;
        if ( field.isAnnotationPresent( SequenceGenerator.class ) ) {
            // 自定义@SequenceGenerator注解
            SequenceGenerator sequenceAnnotation = field.getAnnotation( SequenceGenerator.class );
            sequenceName = sequenceAnnotation.name();
            if ( StringUtil.isBlank( sequenceName ) ) {
                sequenceName = sequenceAnnotation.sequenceName();
            }
        } else if ( field.isAnnotationPresent( JavaxPersistence.SEQUENCE_GENERATOR ) ) {
            // JPA @SequenceGenerator注解
            AnnotationMetadata metaObject = AnnotationMetadata.forObject( field.getField(), JavaxPersistence.SEQUENCE_GENERATOR );
            sequenceName = metaObject.stringValue( "name" );
            if ( StringUtil.isBlank( sequenceName ) ) {
                sequenceName = metaObject.stringValue( "sequenceName" );
            }
        }
        if ( StringUtil.isBlank( sequenceName ) ) {
            throw new MapperParserException( StringUtil.format( "The @SequenceGenerator on the `{}` " +
                            "attribute of the `{}` class does not specify the sequenceName value.",
                    column.getProperty(), column.getEntity().getCanonicalName() ) );
        }
        column.setSequenceName( sequenceName );
    }

    /**
     * 处理{@code @GeneratedValue}注解
     * @param table  表映射信息对象
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processGeneratedValueAnnotation( final Table table, final Column column, final Field field ) {
        boolean isIdentity = false;
        String generator = null;
        if ( field.isAnnotationPresent( GeneratedValue.class ) ) {
            // 自定义@GeneratedValue
            GeneratedValue generatedValueAnnotation = field.getAnnotation( GeneratedValue.class );
            generator = generatedValueAnnotation.generator();
            isIdentity = generatedValueAnnotation.strategy() == GenerationType.IDENTITY;
        } else if ( field.isAnnotationPresent( JavaxPersistence.GENERATED_VALUE ) ) {
            // JPA @GeneratedValue
            AnnotationMetadata metaObject = AnnotationMetadata.forObject( field.getField(), JavaxPersistence.GENERATED_VALUE );
            generator = metaObject.stringValue( "generator" );
            Enum<?> enumValue = metaObject.enumValue( "strategy", null );
            isIdentity = enumValue != null && "IDENTITY".equals( enumValue.name() );
        }
        if ( "UUID".equalsIgnoreCase( generator ) ) {
            column.setUuid( true );
        } else if ( "JDBC".equalsIgnoreCase( generator ) ) {
            column.setIdentity( true ).setGenerator( generator.toUpperCase( Locale.ENGLISH ) );
            table.addPrimaryKeyProperty( column.getProperty() );
            table.addPrimaryKeyColumn( column.getColumn() );
        } else if ( Constants.GENERATOR_SNOWFLAKE_SEQUENCE.equalsIgnoreCase( generator ) ) {
            column.setSnowflakeSequence( true );
        } else if ( generator != null && WORKER_KEYS.contains( generator.toUpperCase( Locale.ENGLISH ) ) ) {
            column.setSnowflakeSequenceString( true );
        } else {
            if ( isIdentity ) {
                column.setIdentity( true );
                if ( StringUtil.hasText( generator ) ) {
                    Dialect dialect = Dialect.getDBDialect( generator );
                    if ( dialect != null ) {
                        generator = dialect.getKeyGenerator();
                    }
                    column.setGenerator( generator );
                }
            } else {
                throw new MapperParserException( StringUtil.format( "The @generatedValue annotation on the '{}' class's attribute '{}' supports the following form: \n1.{}\n2.{}\n3.{}",
                        column.getEntity().getCanonicalName(), column.getProperty(),
                        "@GeneratedValue(generator = \"UUID\")",
                        "@GeneratedValue(generator = \"JDBC\")",
                        "@GeneratedValue(generator = \"WORKER_SEQUENCE\")",
                        "@GeneratedValue(generator = \"WORKER_SEQUENCE_STRING\")",
                        "@GeneratedValue(strategy = GenerationType.IDENTITY, [ generator = \"[ MySql, SQLServer... ]\" ])" ) );
            }
        }
    }

    /**
     * 处理自动识别主键
     * @param table  表映射信息对象
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processAutoDiscernPrimaryKey( final Table table, final Column column, final Field field ) {
        if ( this.configuration.isAutoDiscernPrimaryKey() ) {
            String property = column.getProperty();
            String[] array = this.configuration.getPrimaryKeys();
            boolean include = ArrayUtil.isEmpty( array ) ? "id".equalsIgnoreCase( property ) : StringUtil.include( array, property );
            if ( include ) {
                column.setPrimaryKey( true );
                table.setPrimaryKey( column );
            }
        }
    }

    /**
     * 处理审计注解
     * @param column 字段映射对象
     * @param field  属性信息对象
     */
    private void processAuditAnnotation( Column column, Field field ) {
        boolean canModified = !column.isLogicDelete() && column.isUpdatable();
        column.setCreatedDate( field.isAnnotationPresent( CreatedDate.class ) )
                .setCreatedUser( field.isAnnotationPresent( CreatedUser.class ) )
                .setCreatedUserName( field.isAnnotationPresent( CreatedUserName.class ) );
        column.setDeletedDate( canModified && field.isAnnotationPresent( DeletedDate.class ) )
                .setDeletedUser( canModified && field.isAnnotationPresent( DeletedUser.class ) )
                .setDeletedUserName( canModified && field.isAnnotationPresent( DeletedUserName.class ) );
        column.setLastModifiedDate( canModified && field.isAnnotationPresent( LastModifiedDate.class ) )
                .setLastModifiedUser( canModified && field.isAnnotationPresent( LastModifiedUser.class ) )
                .setLastModifiedUserName( canModified && field.isAnnotationPresent( LastModifiedUserName.class ) );
    }

    private void processCustomKeyGenerator( final Table table, final Column column ) {
        // 检测是否存在主键值生成方式
        if ( !column.isUuid() && !column.isIdentity() && !column.isSnowflakeSequence() && !column.isSnowflakeSequenceString()
                && StringUtil.isBlank( column.getGenerator() ) ) {
            // 直接使用全局主键
            PrimaryKeyType keyType = this.configuration.getPrimaryKeyType();
            column.setUuid( keyType == PrimaryKeyType.UUID );
            column.setIdentity( keyType == PrimaryKeyType.IDENTITY );
            column.setSnowflakeSequence( keyType == PrimaryKeyType.SNOWFLAKE_SEQUENCE );
            column.setSnowflakeSequenceString( keyType == PrimaryKeyType.SNOWFLAKE_SEQUENCE_STRING );
        }
    }

    /**
     * 命名转换
     * @param value         值
     * @param configuration 自定义配置
     * @return 新字符串
     */
    private String transformStrategy( boolean isTable, String value, MyBatisCustomConfiguration configuration ) {
        if ( StringUtil.isBlank( value ) ) {
            return null;
        }
        if ( isTable ) {
            return this.physicalNamingStrategy.tableNameValueOf( value, this.strategy );
        } else {
            return this.physicalNamingStrategy.columnNameValueOf( value, this.strategy );
        }
    }

    /**
     * 过滤属性
     * @param field 属性信息
     * @return boolean
     */
    private boolean filter( final Field field ) {
        return !( !configuration.isUseSimpleType()
                && field.isAnnotationPresent( com.wkit.lost.mybatis.annotation.Column.class, JavaxPersistence.COLUMN )
                && field.isAnnotationPresent( ColumnExt.class )
                && ( SimpleTypeRegistry.isSimpleType( field.getJavaType() )
                || ( this.configuration.isEnumAsSimpleType() && Enum.class.isAssignableFrom( field.getJavaType() ) ) ) );
    }
}