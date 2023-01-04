package com.ruoyi.web.controller.channel;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.web.entity.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by M. on 2021/12/23.
 * 网关支付（后管内部使用）
 */
@RestController
@RequestMapping("/admin/gateway")
public class GatewayPaymentAdminController {


}
