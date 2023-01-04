package com.ruoyi.web.controller.merchant;

import com.payment.admin.constant.Constants;
import com.payment.admin.entity.MerchantChannelConfig;
import com.payment.admin.service.merchant.BankAdminService;
import com.payment.admin.service.merchant.BenificialAdminService;
import com.payment.admin.service.merchant.MerchantAdminService;
import com.payment.admin.service.merchant.PlatformConfigAdminService;
import com.payment.admin.vo.MerchantBankInfoVo;
import com.payment.admin.vo.MerchantInfoVo;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.entity.ApiResponse;
import com.payment.admin.entity.Beneficiary;
import com.payment.admin.entity.MerchantBankInfo;
import com.payment.admin.entity.MerchantPlatformConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by M. on 2021/3/31.
 */

@RestController
@RequestMapping("/admin/merchant")
public class MerchantAdminController {

    @Autowired
    private MerchantAdminService merchantAdminService;

    @Autowired
    private PlatformConfigAdminService platformConfigAdminService;

    @Autowired
    private BankAdminService bankAdminService;

    @Autowired
    private BenificialAdminService benificialAdminService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @GetMapping("/search")
    public ApiResponse searchMerchantListByMerchantName(String merchantName, HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        return ApiResponse.newSuccessInstance(this.merchantAdminService.searchMerchantListByMerchantName(merchantName, loginUser));
    }

    /**
     * 获取商户列表
     * @param pageNumber
     * @param pageSize
     * @param merchantName
     * @param merchantId
     * @param bindUserName
     * @return
     */
    @GetMapping("/list")
    public ApiResponse getMerchantInfoList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String merchantName, String merchantId, String bindUserName, HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        Boolean isAdmin = this.sysRoleService.isAdminByRole(loginUser.getUser());
        if (!isAdmin) {
            bindUserName = loginUser.getUsername();
        }
        return ApiResponse.newSuccessInstance(this.merchantAdminService.getMerchantInfoDTOList(pageNumber, pageSize, merchantName, merchantId, bindUserName));
    }


    /**
     * 查看商户详情
     * @param merchantId
     * @return
     */
    @GetMapping("/detail")
    public ApiResponse getMerchantInfoDetail(@RequestParam String merchantId) {
        return ApiResponse.newSuccessInstance(this.merchantAdminService.getMerchantInfoDetail(merchantId));
    }

    /**
     * 获取商户银行结算账户信息
     * @param merchantId
     * @return
     */
    @GetMapping("/bank/list")
    public ApiResponse getBankAccountList(@RequestParam String merchantId) {
        List<MerchantBankInfoVo> list = this.bankAdminService.getMerchantBankInfo(merchantId);
        return ApiResponse.newSuccessInstance(list);
    }

    /**
     * 保存商户银行结算账户信息
     * @param list
     * @return
     */
    @PostMapping("/bank/save")
    public ApiResponse saveBankAccount(@RequestBody List<MerchantBankInfo> list) {
        this.bankAdminService.saveMerchantBankInfoList(list);
        return ApiResponse.newSuccessInstance();
    }


}
