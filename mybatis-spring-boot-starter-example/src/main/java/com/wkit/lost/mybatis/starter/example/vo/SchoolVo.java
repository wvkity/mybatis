package com.wkit.lost.mybatis.starter.example.vo;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class SchoolVo implements Serializable {

    private static final long serialVersionUID = 354458074373338348L;
    private String id;

    private String name;

    private String address;

    private Integer level;

    private String createUser;

    private String modifyUser;

    private OffsetDateTime gmtCreate;

    private OffsetDateTime gmtModify;

    public SchoolVo() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel( Integer level ) {
        this.level = level;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser( String createUser ) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser( String modifyUser ) {
        this.modifyUser = modifyUser;
    }

    public OffsetDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate( OffsetDateTime gmtCreate ) {
        this.gmtCreate = gmtCreate;
    }

    public OffsetDateTime getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify( OffsetDateTime gmtModify ) {
        this.gmtModify = gmtModify;
    }
}
