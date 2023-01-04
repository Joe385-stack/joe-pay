package com.payment.admin.shardingalgorithm;

import com.google.common.collect.Range;
import com.ruoyi.common.utils.DateTimeUtils;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * 范围分片算法类名称，用于BETWEEN，可选。该类需实现RangeShardingAlgorithm接口并提供无参数的构造器
 *
 * @Description
 * @Date 2019-07-20 00:45
 */
@Component
public class UnionShardingAlgorithm implements RangeShardingAlgorithm<Date> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        Collection<String> collect = new HashSet<>();


        return collect;
    }

}
