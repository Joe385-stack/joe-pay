package com.ruoyi.web.controller.notify;

import com.payment.admin.service.channel.INotifyPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/notify")
public class NotifyAdminController {

    @Autowired
    private INotifyPaymentService notifyPaymentService;

    @PostMapping("/{channel}/agent")
    public String agentNotify(HttpServletRequest request, @PathVariable("channel") String channel){
        return notifyPaymentService.handleNotify(request, channel);
    }
}
