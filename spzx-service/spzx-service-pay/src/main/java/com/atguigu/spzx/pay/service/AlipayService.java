package com.atguigu.spzx.pay.service;

import java.util.Map;

public interface AlipayService {
    String alipay(String orderNo);

    void afterPaySuccess(Map<String, String> paramsMap);

}
