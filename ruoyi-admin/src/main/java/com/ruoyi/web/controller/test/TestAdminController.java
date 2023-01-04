package com.ruoyi.web.controller.test;

import com.payment.admin.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by M. on 2021/3/15.
 */
@RestController
@RequestMapping("/test")
public class TestAdminController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public String test() {
        return this.testService.test();
    }
}
