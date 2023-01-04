package com.payment.admin.service.test;

import org.springframework.stereotype.Service;

/**
 * Created by M. on 2021/3/15.
 */

@Service
public class TestService {

    public String test() {
        System.out.println("尝试成功");
        return "Hello World";
    }
}
