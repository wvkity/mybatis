package com.wvkity.mybatis.core.metadata;

import com.wvkity.mybatis.annotation.extension.Executing;
import com.wvkity.mybatis.config.MyBatisCustomConfiguration;
import com.wvkity.mybatis.keyword.SqlKeyWords;
import com.wvkity.mybatis.utils.Ascii;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.text.MessageFormat;

/**
 * 数据库表字段-实体属性映射类构建器
 * @author wvkity
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class ColumnBuilder extends BuilderSupport implements Builder<ColumnWrapper> {

    /**
     * 实体类
     */
    private Class<?> entity;

    /**
     * 属性对象
     */
    private FieldWrapper field;

    /**
     * 属性
     */
    private String property;

    /**
     * 字段映射
     */
    private String column;

    /**
     * Java类型
     */
    private Class<?> javaType;

    /**
     * Jdbc类型
     */
    private JdbcType jdbcType;

    /**
     * 类型处理器
     */
    private Class<? extends TypeHandler<?>> typeHandler;

    /**
     * 序列名称
     */
    private String sequenceName;

    /**
     * 是否为主键
     */
    private boolean primaryKey = false;

    /**
     * 是否为UUID主键
     */
    private boolean uuid = false;

    /**
     * 是否为自增主键
     */
    private boolean identity = false;

    /**
     * 是否为雪花算法主键
     */
    private boolean snowflakeSequence = false;

    /**
     * 是否为雪花算法字符串主键
     */
    private boolean snowflakeSequenceString = false;

    /**
     * 是否为Blob类型
     */
    private boolean blob = false;

    /**
     * 是否可保存
     */
    private boolean insertable = true;

    /**
     * 是否可修改
     */
    private boolean updatable = true;

    /**
     * SQL语句是否设置Java类型
     */
    private boolean useJavaType = false;

    /**
     * 字符串非空校验
     */
    private boolean checkNotEmpty;

    /**
     * 乐观锁
     */
    private boolean version = false;

    /**
     * 排序方式
     */
    private String orderBy;

    /**
     * 主键生成方式(根据sql语句)
     */
    private String generator;

    /**
     * SQL执行时机
     */
    private Executing executing;

    /**
     * 值
     */
    private Object value;

    // 审计
    /**
     * 标识保存操作时间是否自动填充
     */
    private boolean createdDate;

    /**
     * 标识保存操作用户标识是否自动填充
     */
    private boolean createdUser;

    /**
     * 标识保存操作用户名是否自动填充
     */
    private boolean createdUserName;

    /**
     * 标识逻辑删除操作时间是否自动填充
     */
    private boolean deletedDate;

    /**
     * 标识逻辑删除操作用户标识是否自动填充
     */
    private boolean deletedUser;

    /**
     * 标识逻辑删除操作用户名是否自动填充
     */
    private boolean deletedUserName;

    /**
     * 标识更新操作时间是否自动填充
     */
    private boolean lastModifiedDate;

    /**
     * 标识更新操作用户标识是否自动填充
     */
    private boolean lastModifiedUser;

    /**
     * 标识更新操作用户名是否自动填充
     */
    private boolean lastModifiedUserName;

    /**
     * 是否为逻辑删除属性
     */
    private boolean logicDelete;

    /**
     * 逻辑删除真值
     */
    private Object logicDeletedTrueValue;

    /**
     * 逻辑删除假值
     */
    private Object logicDeletedFalseValue;

    /**
     * 自动添加IS前缀(针对Boolean类型属性)
     */
    private boolean autoAddIsPrefix;

    /**
     * 创建构建器
     * @return 构建器
     */
    public static ColumnBuilder create() {
        return new ColumnBuilder();
    }

    @Override
    public ColumnWrapper build() {
        String columnName = realColumnName();
        // 字段信息
        Descriptor descriptor = new Descriptor(this.field.getField(), this.javaType, this.property,
                this.field.getGetter(), this.field.getSetter());
        // 主键信息
        Unique unique = new Unique(this.uuid, this.identity, this.snowflakeSequence,
                this.snowflakeSequenceString, this.generator, this.executing);
        // 审计信息
        Auditor auditor = new Auditor(this.createdDate, this.createdUser, this.createdUserName, this.deletedDate,
                this.deletedUser, this.deletedUserName, this.lastModifiedDate, this.lastModifiedUser,
                this.lastModifiedUserName, this.logicDelete, this.logicDeletedTrueValue, this.logicDeletedFalseValue);
        return new ColumnWrapper(this.entity, this.property, columnName, this.jdbcType,
                this.typeHandler, this.sequenceName, this.primaryKey, this.blob, this.insertable, this.updatable,
                this.useJavaType, this.checkNotEmpty, this.version, descriptor, unique, auditor);
    }

    /**
     * 获取字段名
     * @return 字段名
     */
    private String realColumnName() {
        String realName;
        String realColumnName;
        if (Ascii.isNullOrEmpty(this.column)) {
            if (this.autoAddIsPrefix && Boolean.class.isAssignableFrom(this.javaType)) {
                realColumnName = "is" + Character.toUpperCase(this.property.charAt(0)) + this.property.substring(1);
            } else {
                realColumnName = this.property;
            }
            realName = columnNameTransform(realColumnName);
        } else {
            realName = this.column;
        }
        String keyWord = this.configuration.getWrapKeyWord();
        if (Ascii.hasText(keyWord) && SqlKeyWords.containsWord(realName)) {
            return MessageFormat.format(keyWord, realName);
        }
        return realName;
    }

    @Override
    public ColumnBuilder configuration(MyBatisCustomConfiguration configuration) {
        super.configuration(configuration);
        this.checkNotEmpty = this.configuration.isCheckNotEmpty();
        this.useJavaType = this.configuration.isUseJavaType();
        return this;
    }

    /**
     * 检查是否存在主键生成方式
     * @return true: 是, false: 否
     */
    public boolean hasKeyGenerator() {
        return this.uuid || this.identity || this.snowflakeSequence
                || this.snowflakeSequenceString || Ascii.hasText(this.generator);
    }
}
