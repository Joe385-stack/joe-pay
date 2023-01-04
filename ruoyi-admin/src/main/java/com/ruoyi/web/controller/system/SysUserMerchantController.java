package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserMerchantService;
import com.ruoyi.web.entity.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by M. on 2022/12/17.
 */
@RestController
@RequestMapping("/system/user/merchant")
public class SysUserMerchantController extends BaseController {

    @Autowired
    private ISysUserMerchantService sysUserMerchantService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/list")
    public ApiResponse list(HttpServletRequest request) {

        LoginUser loginUser = this.tokenService.getLoginUser(request);
        List<String> list = this.sysUserMerchantService.getBindMerchantList(loginUser.getUser().getUserId());
        return ApiResponse.newSuccessInstance(list);
    }
}
