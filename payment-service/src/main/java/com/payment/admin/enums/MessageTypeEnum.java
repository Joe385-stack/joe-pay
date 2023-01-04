package com.payment.admin.enums;

import com.ruoyi.common.exception.BaseException;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum MessageTypeEnum {

    PAY_RATE(1, "支付成功率消息"),
    DAILY_SETTLE(2, "日结算消息"),
    ;

    private final Integer type;

    private final String desc;

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageTypeEnum find(Integer type) {
        Optional<MessageTypeEnum> result =
                Arrays.stream(MessageTypeEnum.values()).filter(messageTypeEnum -> messageTypeEnum.getType().equals(type)).findAny();
        if (result.isPresent()) {
            return result.get();
        }
        throw new BaseException("查询不到改类型的消息");
    }
}
