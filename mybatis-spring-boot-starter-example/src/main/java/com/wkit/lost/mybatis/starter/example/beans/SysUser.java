package com.wkit.lost.mybatis.starter.example.beans;

import com.wkit.lost.mybatis.annotation.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;

@Data
@Accessors( chain = true )
@NoArgsConstructor
public class SysUser implements Serializable {

    private static final long serialVersionUID = 5751338709413689380L;

    /**
     * ID
     */
    private Long id;

    /**
     * 唯一编码
     */
    private Integer state;

    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 创建时间
     */
    private Instant gmtCreate;

    /**
     * 修改时间
     */
    private OffsetDateTime gmtModify;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 测试聚合函数
     */
    @Transient
    private Long count;

    public SysUser( Long id, Integer state, String userName, String password, String createUser, String modifyUser, Instant gmtCreate, OffsetDateTime gmtModify ) {
        this.id = id;
        this.state = state;
        this.userName = userName;
        this.password = password;
        this.createUser = createUser;
        this.modifyUser = modifyUser;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
    }
}
