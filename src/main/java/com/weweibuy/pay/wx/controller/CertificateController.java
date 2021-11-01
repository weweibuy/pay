package com.weweibuy.pay.wx.controller;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.pay.wx.manager.PlatformCertificateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2021/11/1 17:41
 **/
@RestController
@RequestMapping("/wx/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final PlatformCertificateManager platformCertificateManager;

    @GetMapping("/platform/refresh")
    public CommonCodeResponse refreshPlatformCertificate() {
        platformCertificateManager.reloadPlatformCertificate();
        return CommonCodeResponse.success();
    }


}
