package com.payment.admin.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccountTypeEnum {

    MERCHANT(1, "商户账户"),
    PLATFORM(2, "平台分润账户"),
    ;

    private final Integer code;

    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
