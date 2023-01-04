package com.ruoyi.web.controller.merchant;

import com.payment.admin.service.channel.AgentPaymentRecordService;
import com.payment.admin.vo.AgentRecordVo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.entity.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/agent")
public class AgentPaymentRecordController {

    @Autowired
    private AgentPaymentRecordService agentPaymentRecordService;

    /**
     * 商户查询代付记录
     * @param pageNumber
     * @param pageSize
     * @param merchantId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResponse getAgentRecordList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          String merchantId, String receiverName, String status, String startTime, String endTime){
        return ApiResponse.newSuccessInstance(this.agentPaymentRecordService.getAgentRecordList(merchantId, status,
                receiverName, startTime, endTime, pageNumber, pageSize));
    }

    @GetMapping(value = "/status/options")
    public ApiResponse getAgentStatusOptions() {
        return ApiResponse.newSuccessInstance(this.agentPaymentRecordService.getAgentStatusOptionsList());
    }

}
