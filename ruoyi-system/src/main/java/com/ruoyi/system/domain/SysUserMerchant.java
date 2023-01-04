package com.ruoyi.system.domain;

/**
 * Created by M. on 2022/12/17.
 */
public class SysUserMerchant {

    /** 记录 */
    private Long id;

    /** 用户id **/
    private Long userId;

    private String merchantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
