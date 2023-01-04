package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.SysUserMerchantMapper;
import com.ruoyi.system.service.ISysUserMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by M. on 2022/12/17.
 */
@Service
public class SysUserMerchantServiceImpl implements ISysUserMerchantService {

    @Autowired
    private SysUserMerchantMapper sysUserMerchantMapper;

    @Override
    public List<String> getBindMerchantList(Long userId) {
        return sysUserMerchantMapper.getBindMerchantList(userId);
    }
}
