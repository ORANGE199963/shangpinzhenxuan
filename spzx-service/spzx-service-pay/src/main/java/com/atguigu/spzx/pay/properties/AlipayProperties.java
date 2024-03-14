package com.atguigu.spzx.pay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spzx.alipay")
public class AlipayProperties {

    private String alipay_url;
    private String app_id;
    private String app_private_key;//应用的私钥
    private String alipay_public_key;//支付宝的公钥
    private String return_payment_url;//支付成功的页面
    private String notify_payment_url;//支付成功or失败之后，会自动调用的接口


    public final static String format="json";
    public final static String charset="utf-8";
    public final static String sign_type="RSA2";

}
