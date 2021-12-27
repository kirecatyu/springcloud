package com.local.springboot.sso.ssoserver.serice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.local.springboot.sso.ssoserver.entity.TSysUserEntity;
import com.local.springboot.sso.ssoserver.mapper.TSysUserMapper;
import com.local.springboot.sso.ssoserver.serice.TSysUserService;
import com.local.springboot.sso.ssoserver.vo.TSysUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service()
public class TSysUserServiceImpl extends ServiceImpl<TSysUserMapper, TSysUserEntity> implements TSysUserService {


    @Override
    public TSysUserEntity getUserByName(String userName) {
        LambdaQueryWrapper<TSysUserEntity> queryWrapper = new QueryWrapper<TSysUserEntity>().lambda();
        queryWrapper.eq(TSysUserEntity::getUserName,userName);
        queryWrapper.eq(TSysUserEntity::getLogicDel,1);

        TSysUserEntity entity = baseMapper.selectOne(queryWrapper);

        return entity;
    }

    @Override
    public TSysUserVo getVoById(String id) {
        TSysUserEntity entity = getById(id);
        if (entity != null) {
            TSysUserVo vo = new TSysUserVo();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }
        return null;
    }

    @Override
    public void del(String id) {
        TSysUserEntity entity = getById(id);
        if (entity != null) {
            updateById(entity);
        }
    }
}
