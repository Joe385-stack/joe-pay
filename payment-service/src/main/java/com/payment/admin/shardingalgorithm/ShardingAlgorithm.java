package com.payment.admin.shardingalgorithm;

import com.ruoyi.common.utils.DateTimeUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Component
public class ShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {

        SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtils.DATE_FORMAT_YEAR_MONTH);
        String suffixTime = formatter.format(preciseShardingValue.getValue());
        for (String each : collection) {
            if (each.endsWith(suffixTime)) {
                return each;
            }
        }
        throw new IllegalArgumentException("can not find tables end with " + suffixTime + "!");
    }
}
