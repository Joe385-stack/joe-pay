package com.ruoyi.quartz.task;

import com.payment.admin.constant.RedisKey;
import com.payment.admin.service.merchant.ISubMerchantConfigService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateTimeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 定时任务，24小时后重置子商户连续未支付笔数，
 * 拉出限制队列
 */
@Slf4j
@Component("subMerchantTask")
public class SubMerchantTask {

    @Autowired
    private ISubMerchantConfigService subMerchantConfigService;

    public void resumeMerchant(){
        Date now = new Date();
        String cacheObj = SpringUtils.getBean(RedisCache.class).getCacheObject(RedisKey.merchant_pool_init_count.getKey());
        String initCount = StringUtils.isBlank(cacheObj) ? "10" : cacheObj;
        List<SubMerchantConfig> list = subMerchantConfigService.getBlockMerchant(initCount);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.stream().forEach(config -> {
            Date updateTime = config.getUpdateTime();
            if(DateTimeUtils.getDiffHour(now, updateTime) >= 24){
                config.setContinueCount(0);
                config.setUpdateTime(now);
            }
        });
        subMerchantConfigService.saveOrUpdateBatch(list);
    }
}
