package com.ruoyi.quartz.task;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.payment.admin.channel.ori.utils.ClientUtils;
import com.payment.admin.entity.RefundRecord;
import com.payment.admin.enums.RefundStatusEnum;
import com.payment.admin.service.order.RefundRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Component("AlipayRefundQueryTask")
public class AlipayRefundQueryTask {

    @Autowired
    private RefundRecordService refundRecordService;

    public void doTask(){
        Integer channel = 9;
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
            AlipayClient alipayClient = ClientUtils.initClient(merchantId);
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", record.getOrderNo());
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
