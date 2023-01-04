package com.ruoyi.web.entity;

import com.ruoyi.web.constant.BusinessCode;

import java.io.Serializable;

/**
 * API 接口请求返回消息体
 *
 * @author M.
 * @date 2021-03-31
 */
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ApiResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static ApiResponse newSuccessInstance() {
        return new ApiResponse(BusinessCode.SUCCESS, BusinessCode.SUCCESS_MSG);
    }

    public static ApiResponse newSuccessInstance(Object data) {
        return new ApiResponse(BusinessCode.SUCCESS, BusinessCode.SUCCESS_MSG, data);
    }

    /**
     * 返回业务码
     */
    private Integer code;

    /**
     * 返回内容
     */
    private String message;

    /**
     * 服务器响应时间
     */
    private Long timestamp;

    /**
     * 返回数据
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
