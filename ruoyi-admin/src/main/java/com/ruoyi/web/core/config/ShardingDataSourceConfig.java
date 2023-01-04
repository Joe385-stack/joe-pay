package com.ruoyi.web.core.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.collect.Lists;
import com.payment.admin.shardingalgorithm.ShardingAlgorithm;
import com.payment.admin.shardingalgorithm.UnionShardingAlgorithm;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by M. on 2021/4/20.
 */

@Configuration
@MapperScan(value = "com.payment.admin.shardingmapper", sqlSessionFactoryRef = "shardingSqlSessionFactory")
@EnableTransactionManagement
public class ShardingDataSourceConfig {
    private String mapperLocations = "classpath*:shardingmapper/*.xml";

    @Autowired
    private ShardingAlgorithm shardingAlgorithm;
    @Autowired
    private UnionShardingAlgorithm unionShardingAlgorithm;


    @Bean("shardingDataSource")
    public DataSource shardingDataSource(@Qualifier("master") DataSource master,
                                         @Qualifier("slave") DataSource slave) throws SQLException {
        ShardingRuleConfiguration ruleConfiguration = new ShardingRuleConfiguration();
        //属性配置
        Properties props = new Properties();
        //是否打印执行的sql语句
        props.setProperty("sql.show", "true");
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(master, slave), ruleConfiguration, props);
    }

    /**
     * 加载创建dataSource：
     *
     * @return
     */
    private Map<String, DataSource> createDataSourceMap(@Qualifier("master") DataSource master,
                                                        @Qualifier("slave") DataSource slave) {
        HashMap<String, DataSource> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put("master_0", master);
        dataSourceMap.put("slave_0", slave);
        return dataSourceMap;
    }

    /**
     * 读写分离配置
     * @return
     */
    private List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfiguration(){
        return Lists.newArrayList(new MasterSlaveRuleConfiguration("ds_0", "master_0",
                Arrays.asList("slave_0")));
    }

    /**
     * 按日期分表分库
     * @return
     */
    private ShardingStrategyConfiguration getDateShardingStrategy() {
        String column = "create_time";
        return new StandardShardingStrategyConfiguration(column, shardingAlgorithm, unionShardingAlgorithm);
    }

    @Bean("shardingSqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("shardingDataSource") DataSource shardingDataSource, MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(shardingDataSource);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{mybatisPlusInterceptor});
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return sqlSessionFactoryBean;
    }
}
