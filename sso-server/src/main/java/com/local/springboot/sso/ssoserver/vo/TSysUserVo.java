package com.local.springboot.sso.ssoserver.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 * @email
 * @date 2021-11-26 19:31:03
 */
@Data
public class TSysUserVo extends BaseVo {

    /**
     *
     */
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
