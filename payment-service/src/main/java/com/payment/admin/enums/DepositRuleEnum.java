package com.payment.admin.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DepositRuleEnum {

    RATE(1, "比例"),
    AMOUNT(2, "金额"),
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
