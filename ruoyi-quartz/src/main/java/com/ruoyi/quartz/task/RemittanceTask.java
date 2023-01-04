package com.ruoyi.quartz.task;

import com.payment.admin.entity.MerchantFlowStatistic;
import com.payment.admin.entity.VirtualAccount;
import com.payment.admin.service.merchant.VirtualAccountService;
import com.payment.admin.service.order.MerchantFlowStatisticService;
import com.ruoyi.common.utils.DateTimeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/***
 * @
 * @date 2021-10-25
 * @description 根据流水增加账户可使用余额
 */
@Component("remittanceTask")
public class RemittanceTask {

    @Autowired
    private MerchantFlowStatisticService merchantFlowStatisticService;

    @Autowired
    private VirtualAccountService virtualAccountService;

    public void remittance(){
        //获取今天星期几(1是星期天)
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Date now = Calendar.getInstance().getTime();
        //获取两天前的结算记录
        Date beginDate = DateTimeUtils.getBeginTimeOfLastDate(now,-2);
        Date endDate = DateTimeUtils.getEndTimeOfLastDate(now,-1);
        List<MerchantFlowStatistic> statisticList = merchantFlowStatisticService.getStatisticByTime(beginDate, endDate);
        if(CollectionUtils.isNotEmpty(statisticList)){
            statisticList.forEach(statistic -> {
                //如果是T1并且是周末，则不结算
                if(statistic.getSettlementRules() == SettlementsEnum.T_1.getSettlementRulesId()
                        && (day == 0 || day == 7)){
                    return;
                }
                virtualAccountService.doSettlement(statistic);
            });
        }
    }
}
