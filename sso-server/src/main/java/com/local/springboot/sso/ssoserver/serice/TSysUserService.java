package com.local.springboot.sso.ssoserver.serice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.local.springboot.sso.ssoserver.entity.TSysUserEntity;
import com.local.springboot.sso.ssoserver.vo.TSysUserVo;

/**
 * @author Administrator
 * @email
 * @date 2021-11-26 19:31:03
 */
public interface TSysUserService extends IService<TSysUserEntity> {


    TSysUserEntity getUserByName(String userName);

    /**
     * 通过主键获取VO信息
     *
     * @param id
     * @return
     */
    TSysUserVo getVoById(String id);

    /**
     * 删除
     *
     * @param id
     */
    void del(String id);
}

