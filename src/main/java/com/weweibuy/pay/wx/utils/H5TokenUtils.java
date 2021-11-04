package com.weweibuy.pay.wx.utils;

import com.weweibuy.framework.common.codec.jwt.JwtUtils;
import com.weweibuy.pay.wx.model.vo.JwtTokenVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanMap;

import java.security.PrivateKey;
import java.util.Map;

/**
 * @author durenhao
 * @date 2021/11/4 22:14
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class H5TokenUtils {

    /**
     * 生成 JWT
     *
     * @param jwtTokenVo
     * @param effectiveTimeSecond
     * @param privateKey
     * @return
     */
    public static String generateJwtToken(JwtTokenVo jwtTokenVo, Long effectiveTimeSecond, PrivateKey privateKey) {
        return JwtUtils.encodeRS256(privateKey, (Map<String, Object>) BeanMap.create(jwtTokenVo), effectiveTimeSecond);
    }


}
