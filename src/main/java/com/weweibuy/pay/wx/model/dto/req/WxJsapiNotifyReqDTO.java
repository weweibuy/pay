package com.weweibuy.pay.wx.model.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.weweibuy.pay.wx.config.WxFeignSnakeCaseEncoderAndDecoder;
import com.weweibuy.pay.wx.model.constant.WxApiConstant;
import com.weweibuy.pay.wx.model.dto.common.JsapiOrderInfoDTO;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2021/11/2 22:16
 **/
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WxJsapiNotifyReqDTO {

    /**
     * 通知的唯一ID
     * 示例值：EV-2018022511223320873
     */
    @NotBlank(message = "通知ID不能为空")
    private String id;

    /**
     * 通知创建的时间
     */
    @JsonFormat(pattern = WxFeignSnakeCaseEncoderAndDecoder.DATA_FORMAT, timezone = "GMT+8")
    @NotNull(message = "通知创建时间不能为空")
    private LocalDateTime createTime;

    /**
     * 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS
     * {@link WxApiConstant#WX_NOTIFY_EVENT_TYPE_SUCCESS}
     */
    @NotBlank(message = "通知类型不能为空")
    private String eventType;

    /**
     * 通知数据类型
     * 支付成功通知为encrypt-resource
     * {@link WxApiConstant#WX_NOTIFY_RESOURCE_TYPE_SUCCESS}
     */
    @NotBlank(message = "通知数据类型不能为空")
    private String resourceType;

    /**
     * 回调摘要
     * 示例值：支付成功
     */
    private String summary;

    /**
     * 通知数据(里面包含加密的订单数据)
     * {@link JsapiOrderInfoDTO}
     */
    @NotNull(message = "通知数据不能为空")
    @Valid
    private WxEncryptDataDTO resource;


}
