package com.payment.admin.enums;

import com.ruoyi.common.exception.BaseException;

import java.util.Arrays;
import java.util.Optional;

public enum SubjectTypeEnum {
//    个体户，企业，事业单位，民办非企业
    SUBJECT_TYPE_INDIVIDUAL("1", "个体工商户"),
    SUBJECT_TYPE_ENTERPRISE("2", "企业"),
    SUBJECT_TYPE_INSTITUTIONS("3", "党政、机关及事业单位"),
    SUBJECT_TYPE_OTHERS("4", "其他组织")
    ;


    private String id;
    private String subjectType;

    public String getId() {
        return id;
    }

    public String getSubjectType() {
        return subjectType;
    }

    SubjectTypeEnum(String id, String subjectType) {
        this.id = id;
        this.subjectType = subjectType;
    }

    public static SubjectTypeEnum find(String id) {
        Optional<SubjectTypeEnum> result = Arrays.stream(SubjectTypeEnum.values()).filter(subjectTypeEnum -> subjectTypeEnum.getId().equals(id)).findAny();
        if (result.isPresent()) {
            return result.get();
        }
        throw new BaseException("查不到该企业性质枚举类");
    }
}
