package com.ruoyi.system.mapper;

import java.util.List;

/**
 * Created by M. on 2022/12/17.
 */
public interface SysUserMerchantMapper {

    List<String> getBindMerchantList(Long userId);
}
