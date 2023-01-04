package com.ruoyi.web.controller.order;

import com.payment.admin.domain.RefundRequestBody;
import com.payment.admin.domain.RefundResponse;
import com.payment.admin.entity.MerchantChannelConfig;
import com.payment.admin.entity.RefundRecord;
import com.payment.admin.entity.VirtualAccount;
import com.payment.admin.enums.AccountTypeEnum;
import com.payment.admin.enums.RefundStatusEnum;
import com.payment.admin.service.channel.MerchantChannelAdminService;
import com.payment.admin.service.merchant.MerchantVirtualAccountService;
import com.payment.admin.service.merchant.VirtualAccountService;
import com.payment.admin.service.order.RefundRecordService;
import com.payment.admin.service.order.RefundService;
import com.ruoyi.common.utils.DateTimeUtils;
import com.ruoyi.web.constant.BusinessCode;
import com.ruoyi.web.entity.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @
 * @date 2021-09-21
 * @description 退款处理
 */
@Slf4j
@RestController
@RequestMapping("/admin/refund")
public class RefundOrderController {

    @Autowired
    private RefundService refundService;

    @Autowired
    private RefundRecordService refundRecordService;

    @Autowired
    private MerchantChannelAdminService merchantChannelAdminService;

    @Autowired
    private MerchantVirtualAccountService merchantVirtualAccountService;

    @Autowired
    private VirtualAccountService virtualAccountService;

    @PostMapping("/doRefund")
    @PreAuthorize("@ss.hasPermi('merchant:refund')")
    public ApiResponse refundOrder(@RequestBody RefundRequestBody refundVo){
        RefundResponse response =  refundService.refund(refundVo);
        if(response.getRefundCode().equals(RefundStatusEnum.REFUND_SUCCESS.getCode())){
            return ApiResponse.newSuccessInstance();
        }else {
            return new ApiResponse(BusinessCode.BUSINESS_ERROR, response.getRefundMsg());
        }
    }

    @PostMapping("/refund/correct")
    public void refundCorrect(String merchantId, Integer payChannel, Integer payScene,
                                     String beginStr, String endStr){
        Date beginDate = DateTimeUtils.convertStrToDate(beginStr, DateTimeUtils.DEFAULT_DATETIME_PATTERN);
        Date endDate = DateTimeUtils.convertStrToDate(endStr, DateTimeUtils.DEFAULT_DATETIME_PATTERN);
        MerchantChannelConfig channel = merchantChannelAdminService.getMerchantChannelConfig(merchantId, payChannel, payScene);
        List<RefundRecord> refundRecords = refundRecordService.getRefundedRecord(merchantId,
                Integer.valueOf(payChannel), Integer.valueOf(payScene), beginDate, endDate);
        String rate = StringUtils.isEmpty(channel.getRate()) ? "0.006" : channel.getRate();
        String channelRate = StringUtils.isEmpty(channel.getChannelRate()) ? "0.006" : channel.getChannelRate();
        Long totalMerchantFee = 0L;
        Long totalPlatformFee = 0L;
        for (RefundRecord refundRecord : refundRecords){
            Long totalAmount = refundRecord.getRefundAmount();
            BigDecimal amount = BigDecimal.valueOf(totalAmount);
            Long totalFee = amount.multiply(new BigDecimal(rate)).setScale(1, RoundingMode.HALF_UP).longValue();
            Long channelFee = amount.multiply(new BigDecimal(channelRate)).setScale(1, RoundingMode.HALF_UP).longValue();
            Long merchantFee = totalAmount-totalFee;
            Long platformFee = totalFee - channelFee;
            totalMerchantFee += merchantFee;
            totalPlatformFee += platformFee;
        }
        log.info("totalMerchantFee={}, totalPlatformFee={}", totalMerchantFee, totalPlatformFee);
        List<VirtualAccount> accounts = merchantVirtualAccountService.getVirtualAccount(merchantId, payChannel, payScene);
        for(VirtualAccount account : accounts) {
            //如果是分润账户，增加费率
            Integer accountType = account.getAccountType();
            long amount = AccountTypeEnum.MERCHANT.getCode() == accountType ?
                    totalMerchantFee.longValue() : totalPlatformFee.longValue();
            virtualAccountService.subAmount(account.getAccountNo(), amount);
        }
    }
}
