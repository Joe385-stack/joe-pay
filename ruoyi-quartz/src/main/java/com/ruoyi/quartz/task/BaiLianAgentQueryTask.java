package com.ruoyi.quartz.task;

import com.payment.admin.entity.AgentPaymentRecord;
import com.payment.admin.service.channel.AgentPaymentRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 查询百联优力代付结果
 */
@Slf4j
@Component("baiLianAgentQueryTask")
public class BaiLianAgentQueryTask {

    @Autowired
    private AgentPaymentRecordService agentPaymentRecordService;

    public void query(Integer channel){
        List<AgentPaymentRecord> agentPaymentRecords = agentPaymentRecordService.getRecordByChannel(channel);
        if(CollectionUtils.isEmpty(agentPaymentRecords)){
            log.info("[待处理的代付订单不存在]");
            return;
        }
        agentPaymentRecords.stream().forEach(record -> {
            agentPaymentRecordService.updateAgentRecordStatus(record);
        });
    }
}
