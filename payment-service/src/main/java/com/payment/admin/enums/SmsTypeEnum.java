package com.payment.admin.enums;

import lombok.AllArgsConstructor;

/**
 * @
 * @since 2019-12-12
 * 短信类型枚举
 */
@AllArgsConstructor
public enum SmsTypeEnum {

    VERIFY_CODE(1,"验证码"),
    ;
    public final int value;

    public final String desc;

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
