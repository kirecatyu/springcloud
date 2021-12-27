package com.local.springboot.sso.ssoserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Administrator
 * @email
 * @date 2021-11-26 19:31:03
 */
@Data
@TableName("t_sys_user")
public class TSysUserEntity extends BaseEntity {

    /**
     *
     */
    @TableId
    private String id;
    /**
     *
     */
    private String loginName;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String userName;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String openId;
    /**
     *
     */
    private String image;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
    /**
     *
     */
    private Integer logicDel;

}
