package com.wxpay.paydemo.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaoxiao 2020/1/8
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx-pay")
public class WXPayConfig {

    private String appid;

    private String mch_id;

    private String key;

    private String send_url;

    private String certificate_url;


}
