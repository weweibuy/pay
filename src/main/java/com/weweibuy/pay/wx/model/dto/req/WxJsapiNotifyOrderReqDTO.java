package com.weweibuy.pay.wx.model.dto.req;

import com.weweibuy.pay.wx.model.dto.common.JsapiOrderInfoDTO;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import lombok.Data;

/**
 * 微信Jsapi 支付通知, 解密后的内容
 * <p>
 * 获取 {@link WxJsapiNotifyReqDTO#resource}  -->
 * 解密:{@link WxEncryptDataDTO#ciphertext}
 *
 * @author durenhao
 * @date 2021/11/2 22:33
 **/
@Data
public class WxJsapiNotifyOrderReqDTO extends JsapiOrderInfoDTO {


}
