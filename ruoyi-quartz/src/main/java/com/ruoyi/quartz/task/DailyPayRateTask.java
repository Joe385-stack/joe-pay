package com.ruoyi.quartz.task;

import com.payment.admin.entity.BotMessage;
import com.payment.admin.entity.MerchantChannelConfig;
import com.payment.admin.enums.MessageTypeEnum;
import com.payment.admin.service.channel.MerchantChannelConfigService;
import com.payment.admin.service.message.BotMessageService;
import com.payment.admin.service.message.telegram.TelegramBotService;
import com.payment.admin.service.order.OrderService;
import com.payment.admin.vo.PayRateVo;
import com.ruoyi.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("dailyPayRateTask")
public class DailyPayRateTask {

    @Autowired
    BotMessageService botMessageService;

    @Autowired
    MerchantChannelConfigService merchantChannelConfigService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TelegramBotService telegramBotService;

    public void sendMessage(){
        Date now = Calendar.getInstance().getTime();
        Integer messageType = MessageTypeEnum.DAILY_SETTLE.getType();
        List<BotMessage> messages = botMessageService.getBotMessageByType(messageType);
        if(CollectionUtils.isEmpty(messages)){
            return;
        }
        List<Long> channelList = messages.stream().map(BotMessage::getChannelId).collect(Collectors.toList());
        List<MerchantChannelConfig> configList = merchantChannelConfigService.listByIds(channelList);
        List<String> merchantIdList = configList.stream().map(MerchantChannelConfig::getMerchantId).collect(Collectors.toList());
        Date beginDate = DateTimeUtils.getBeginTimeOfLastDate(now, -1);
        Date endDate = DateTimeUtils.getEndTimeOfLastDate(now, -1);
        Map<Long, MerchantChannelConfig> configMap = configList.stream().collect(Collectors.toMap(MerchantChannelConfig::getChannelId, s -> s));
        List<PayRateVo> payRateVos = orderService.calMerchantRate(merchantIdList, beginDate, endDate);
        Map<String, PayRateVo> payRateVoMap = payRateVos.stream().collect(Collectors.toMap(PayRateVo::getChannelId, s -> s));
        messages.stream().forEach(message -> {
            try {
                MerchantChannelConfig config = configMap.get(message.getChannelId());
                String bindChanelId = config == null ? "" : config.getMerchantId() + config.getPayChannel() + config.getPayScene();
                PayRateVo payRateVo = payRateVoMap.get(bindChanelId);
                sendMessage(message, payRateVo);
            }catch (Exception e){
                log.error("[发送支付成功率消息]失败:{}", e);
            }
        });
    }

    private void sendMessage(BotMessage message, PayRateVo payRateVo){
        String chatId = message.getChatId();
        String tokenId = message.getTokenId();
        String channelName = message.getChannelName();
        String merchantName = message.getMerchantName();
        String amount = "0.00";
        String total = "0";
        String success = "0";
        String rate = "0.00%";
        String text = "【日结算通知】商户【%s】在【%s】通道今日收款总额【%s】元,下单笔数【%s】笔,支付成功笔数【%s】笔,支付成功率【%s】";
        if(payRateVo != null){
            amount = new BigDecimal(payRateVo.getAmount()).divide(new BigDecimal(100))
                    .setScale(2, RoundingMode.FLOOR).toString();
            total = payRateVo.getTotal().toString();
            success = payRateVo.getSuccess().toString();
            BigDecimal result = BigDecimal.valueOf(payRateVo.getSuccess())
                    .divide(BigDecimal.valueOf(payRateVo.getTotal()), 4, BigDecimal.ROUND_HALF_UP);
            rate = result.multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        text = String.format(text, merchantName, channelName, amount, total, success, rate+"%");
        telegramBotService.sendMessage(tokenId, chatId, text);
    }
}
