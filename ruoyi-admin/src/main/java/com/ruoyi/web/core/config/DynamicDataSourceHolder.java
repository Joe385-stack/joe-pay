package com.ruoyi.web.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @
 * @data 2021-03-28
 * @动态数据源配置
 */
@Slf4j
@Component
public class DynamicDataSourceHolder {

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";

    public static String getDbType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_MASTER;
        }
        return db;
    }

    public static void setDBType(String str) {
        log.info("数据源为" + str);
        contextHolder.set(str);
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
