package com.payment.admin.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface PaymentChannel {
    String channel();
    String scene();
}
