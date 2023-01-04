package com.ruoyi.web.controller.order;

import com.github.pagehelper.PageInfo;
import com.payment.admin.service.merchant.PlatformConfigAdminService;
import com.payment.admin.service.order.OrderService;
import com.payment.admin.vo.OrderVo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.web.entity.ApiResponse;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by M. on 2021/4/22.
 */

@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TokenService tokenService;



    @GetMapping("/list/merchant")
    public ApiResponse list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            Integer status,
                            Integer payChannel,
                            Integer payScene,
                            String merchantOrderId,
                            String originOrderId,
                            String thirdOrderId,
                            String orderId,
                            String subject,
                            @RequestParam(value = "startTime") String startTime,
                            @RequestParam(value = "endTime") String endTime,
                            HttpServletRequest request) {
        LoginUser loginUser = this.tokenService.getLoginUser(request);
        PageInfo<OrderVo> page = this.orderService.getListByMerchant(loginUser, status, pageNumber, pageSize, payChannel, payScene, merchantOrderId, originOrderId, orderId, thirdOrderId, subject, startTime, endTime);
        return ApiResponse.newSuccessInstance(page);
    }

    @GetMapping("/export/merchant")
    public AjaxResult export(Integer status,
                             Integer payChannel,
                             Integer payScene,
                             String merchantOrderId,
                             String originOrderId,
                             String orderId,
                             String thirdOrderId,
                             String subject,
                             @RequestParam(value = "startTime") String startTime,
                             @RequestParam(value = "endTime") String endTime,
                             HttpServletRequest request) {
        LoginUser loginUser = this.tokenService.getLoginUser(request);
        List<OrderVo> list = this.orderService.getExportListByMerchant(loginUser, status, payChannel, payScene, merchantOrderId, originOrderId, orderId, thirdOrderId, subject, startTime, endTime);
        ExcelUtil<OrderVo> util = new ExcelUtil<>(OrderVo.class);
        return util.exportExcel(list, "商户账单");
    }

    @GetMapping("/statistics/merchant")
    public ApiResponse statisticsOrderByMerchant(Integer payChannel,
                                                 Integer payScene,
                                                 String merchantName,
                                                 @RequestParam(value = "startTime") String startTime,
                                                 @RequestParam(value = "endTime") String endTime, HttpServletRequest request) {
        LoginUser loginUser = this.tokenService.getLoginUser(request);
        Map<String, BigDecimal> map = this.orderService.statisticsOrderByMerchant(loginUser, merchantName, payChannel, payScene, startTime, endTime);
        return ApiResponse.newSuccessInstance(map);
    }

    @GetMapping("/status/options/list")
    public ApiResponse getOrderStatusOptionsList() {
        Map<String, List<Map<String, Object>>> map = this.orderService.getOrderStatusOptionsList();
        return ApiResponse.newSuccessInstance(map);
    }

    @PostMapping("/notify/send")
    public ApiResponse SendNotify(String orderId, @RequestParam(value = "createTime") String createTime){
        orderService.sendNotifyMsg(orderId, createTime);
        return ApiResponse.newSuccessInstance();
    }
}
