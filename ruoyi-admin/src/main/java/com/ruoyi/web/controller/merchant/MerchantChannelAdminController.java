package com.ruoyi.web.controller.merchant;

import com.payment.admin.dto.MerchantChannelConfigDTO;
import com.payment.admin.dto.PageDTO;
import com.payment.admin.service.channel.MerchantChannelAdminService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.web.entity.ApiResponse;
import com.payment.admin.entity.MerchantChannelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by M. on 2021/4/1.
 */

@RestController
@RequestMapping("/admin/merchant/channel")
public class MerchantChannelAdminController {

    @Autowired
    private MerchantChannelAdminService merchantChannelAdminService;
    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/list")
    public ApiResponse getMerchantChannelList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, String merchantName, String merchantId, String bindUserName, HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        Boolean isAdmin = this.sysRoleService.isAdminByRole(loginUser.getUser());
        if (!isAdmin) {
            bindUserName = loginUser.getUsername();
        }
        PageDTO<MerchantChannelConfigDTO> pageDTO = this.merchantChannelAdminService.getMerchantChannelList(pageNumber, pageSize, merchantName, merchantId, bindUserName);
        return ApiResponse.newSuccessInstance(pageDTO);
    }

    @PostMapping("/save")
    public ApiResponse saveMerchantChannel(@RequestBody MerchantChannelConfig merchantChannelConfig) {
        this.merchantChannelAdminService.saveMerchantChannel(merchantChannelConfig);
        return ApiResponse.newSuccessInstance();
    }

}
