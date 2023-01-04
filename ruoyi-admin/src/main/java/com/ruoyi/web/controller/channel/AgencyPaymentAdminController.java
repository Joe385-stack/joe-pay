package com.ruoyi.web.controller.channel;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.web.constant.BusinessCode;
import com.ruoyi.web.entity.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @
 * @description 代付业务
 * @date 2021-08-12
 */
@Slf4j
@RestController
@RequestMapping("/admin/agent/")
public class AgencyPaymentAdminController {
}
