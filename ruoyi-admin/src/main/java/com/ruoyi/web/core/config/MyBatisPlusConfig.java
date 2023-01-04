package com.ruoyi.web.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ruoyi.web.interceptor.DynamicDataSourceInterceptor;
import com.ruoyi.framework.datasource.DynamicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by M. on 2021/3/15.
 */

@Configuration
@MapperScan(value = {"com.payment.admin.mapper", "com.ruoyi.**.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisPlusConfig {

    private String mapperLocations = "classpath*:mapper/**/*Mapper.xml,classpath*:mapper/*.xml";
    private String typeAliasesPackage = "com.ruoyi.**.domain";

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }

    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        return paginationInnerInterceptor;
    }

    @Bean
    public DynamicDataSourceInterceptor dynamicDataSourceInterceptor(){
        return new DynamicDataSourceInterceptor();
    }


    @Primary
    @Bean("sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DynamicDataSource dataSource) throws Exception{
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        //懒加载
        LazyConnectionDataSourceProxy proxy =new LazyConnectionDataSourceProxy();
        proxy.setTargetDataSource(dataSource);
        sqlSessionFactoryBean.setDataSource(proxy);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{mybatisPlusInterceptor(), dynamicDataSourceInterceptor()});
        String[] locations = mapperLocations.split(",");
        sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations(locations));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        return sqlSessionFactoryBean;
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations){
        return Stream.of(Optional.ofNullable(mapperLocations).orElse(new String[0]))
                .flatMap(location -> {
                    try {
                        return Stream.of(new PathMatchingResourcePatternResolver().getResources(location));
                    }catch (IOException e){
                        return Stream.of(new Resource[0]);
                    }
                }).toArray(Resource[]::new);
    }
}
