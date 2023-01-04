package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.payment.admin.channel.ori.utils.ClientUtils;
import com.payment.admin.entity.MerchantCert;
import com.payment.admin.entity.Order;
import com.payment.admin.entity.RefundRecord;
import com.payment.admin.enums.RefundStatusEnum;
import com.payment.admin.service.order.OrderService;
import com.payment.admin.service.order.RefundRecordService;
import com.payment.admin.utils.CertInitUtils;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Component("syZftRefundQueryTask")
public class SyZftRefundQueryTask {

    @Autowired
    private RefundRecordService refundRecordService;

    @Autowired
    private OrderService orderService;

    public void doTask(){
        Integer channel = 23;
        List<RefundRecord> refundRecords = refundRecordService.getPendingRecords(channel);
        if(CollectionUtils.isEmpty(refundRecords)){
            return;
        }
        refundRecords.stream().forEach(refundRecord -> {
            updateRefundStatus(refundRecord);
        });
    }

    public void updateRefundStatus(RefundRecord record){
        try {
            String merchantId = record.getMerchantId();
            Date date = record.getCreateTime();
            //只查询30天以内的订单
            Date beginDate = DateTimeUtils.getLastDate(date, -30);
            Order order = orderService.getOrderById(record.getOrderNo(), beginDate, date);
            if(order == null){
                throw new BaseException("退款订单不存在");
            }
            MerchantCert cert = CertInitUtils.getMerchantCert(merchantId);
            if(cert == null){
                throw new BaseException("密钥配置不存在");
            }
            AlipayClient alipayClient = ClientUtils.initClient(cert.getAppId());
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order.getThirdOrderId());
            bizContent.put("out_request_no", record.getRefundNo());
            request.setBizContent(bizContent.toString());
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.certificateExecute(request);
            if (response.isSuccess() && "REFUND_SUCCESS".equals(response.getRefundStatus())) {
                record.setRefundStatus(RefundStatusEnum.REFUND_SUCCESS.getCode());
            } else {
                record.setRefundStatus(RefundStatusEnum.REFUND_FAILED.getCode());
            }
        }catch (Exception e){
            record.setRefundStatus(RefundStatusEnum.REFUND_ERROR.getCode());
        } finally {
            record.setUpdateTime(new Date());
            refundRecordService.saveOrUpdate(record);
        }
    }
}
