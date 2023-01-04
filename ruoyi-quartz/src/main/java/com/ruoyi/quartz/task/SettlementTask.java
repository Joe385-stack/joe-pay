package com.ruoyi.quartz.task;

import com.payment.admin.constant.Constants;
import com.payment.admin.entity.MerchantChannelConfig;
import com.payment.admin.entity.MerchantFlowStatistic;
import com.payment.admin.service.channel.MerchantChannelConfigService;
import com.payment.admin.service.order.MerchantFlowStatisticService;
import com.payment.admin.service.order.OrderService;
import com.ruoyi.common.utils.DateTimeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @
 * @date 2021-10-25
 * @description 流水结算(D1和T1)
 */
@Component("settlementTask")
public class SettlementTask {

    @Autowired
    private MerchantChannelConfigService merchantChannelConfigService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MerchantFlowStatisticService merchantFlowStatisticService;

    public void settlement(){
        //获取所有D1和T1的商户(不包括已经冻结的商户)
        List<MerchantChannelConfig> list = merchantChannelConfigService.getD1OrT1Merchant();
        List<String> merchantIds = list.stream().map(MerchantChannelConfig::getMerchantId).collect(Collectors.toList());
        Map<String, Integer> settlement = list.stream().collect(Collectors.toMap(k1 ->
                k1.getMerchantId() + k1.getPayChannel() + k1.getPayScene() , MerchantChannelConfig::getSettlementRules, (s, a) -> s));
        Date now = Calendar.getInstance().getTime();
        //统计的开始时间和截止时间
        Date beginTime = DateTimeUtils.getBeginTimeOfLastDate(now, -1);
        Date endTime = DateTimeUtils.getEndTimeOfLastDate(now, -1);
        //按商户、渠道和场景统计昨日流水
        List<MerchantFlowStatistic> statistics = orderService.getMerchantFlowStatistic(merchantIds, beginTime, endTime);
        //没有流水的商户过滤掉
        statistics = statistics.stream().filter(statistic -> statistic.getTotalAmount() > 0).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(statistics)){
           statistics.forEach(statistic -> {
               statistic.setStatus(Constants.DealStatus.PENDING);
               statistic.setSettlementRules(settlement.get(statistic.getMerchantId()+statistic.getPayChannel()+statistic.getPayScene()));
               statistic.setStatisticDay(beginTime);
               statistic.setCreateTime(now);
               statistic.setUpdateTime(now);
           });
        }
        merchantFlowStatisticService.saveBatch(statistics);
    }
}
