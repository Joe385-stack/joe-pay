package com.ruoyi.quartz.task;

import com.payment.admin.channel.jyt.enums.BatchAgentStatusEnum;
import com.payment.admin.entity.AgentBatchRecord;
import com.payment.admin.entity.AgentPaymentRecord;
import com.payment.admin.service.channel.AgentBatchRecordService;
import com.payment.admin.service.channel.AgentPaymentRecordService;
import com.payment.admin.service.merchant.VirtualAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Component("agentRecordTask")
public class AgentRecordTask {

    @Autowired
    private VirtualAccountService virtualAccountService;

    @Autowired
    private AgentPaymentRecordService agentPaymentRecordService;

    @Autowired
    private AgentBatchRecordService agentBatchRecordService;

    public void doAgent(){
        List<AgentBatchRecord> batchRecords = agentBatchRecordService.getUntreatedRecord();
        if(CollectionUtils.isEmpty(batchRecords)){
            return;
        }
        for(int i =0; i< batchRecords.size(); i++){
            AgentBatchRecord batchRecord = batchRecords.get(i);
            handleBatchRecord(batchRecord);
            batchRecord.setStatus(BatchAgentStatusEnum.SUCCESS.getCode());
            batchRecord.setUpdateTime(Calendar.getInstance().getTime());
            agentBatchRecordService.updateById(batchRecord);
        };
    }

    private void handleBatchRecord(AgentBatchRecord batchRecord){
        List<AgentPaymentRecord> paymentRecords = agentPaymentRecordService.getRecordByBatchNo(batchRecord.getBatchNo());
        if(CollectionUtils.isEmpty(paymentRecords)){
            log.error("[批量代付]批量数据为空, 批次号:{}", batchRecord.getBatchNo());
            return;
        }
        for(int i =0; i< paymentRecords.size(); i++){
            AgentPaymentRecord paymentRecord = paymentRecords.get(i);
            try{
                virtualAccountService.doAgent(batchRecord, paymentRecord);
            }catch (Exception e){
                log.error("代付支付, 批次号:{}, 错误信息:{}", batchRecord.getBatchNo(), e.getMessage());
            }
        }
    }
}
