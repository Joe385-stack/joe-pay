package com.payment.admin.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RefundStatusEnum {

    PENDING(1, "退款处理中"),
    REFUND_SUCCESS( 2, "退款成功"),
    REFUND_FAILED(3, "退款失败"),
    REFUND_ERROR(4, "退款错误"),
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
