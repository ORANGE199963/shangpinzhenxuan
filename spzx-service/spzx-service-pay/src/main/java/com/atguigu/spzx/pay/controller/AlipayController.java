package com.atguigu.spzx.pay.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.pay.properties.AlipayProperties;
import com.atguigu.spzx.pay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/order/alipay")
public class AlipayController {

    @Autowired
    AlipayService alipayService;

    @Autowired
    AlipayProperties properties;

    //该回调接口用于异步接收支付状态，在接口中需要对支付宝传递的数据进行安全性校验（签名校验）
    //该接口不需要我们自己来进行调用，支付宝负责对该接口进行调用
    @PostMapping("/callback/notify")
    public String callback( @RequestParam Map<String, String> paramsMap){
        //1、签名校验
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap,properties.getAlipay_public_key() ,AlipayProperties.charset , AlipayProperties.sign_type); //调用SDK验证签名
//            boolean signVerified = true;
            if(signVerified){

                //判断是否支付成功
                String trade_status = paramsMap.get("trade_status");
                if(trade_status.equals("TRADE_SUCCESS")){
                    //支付成功！！！
                    //1、修改订单  2、修改支付记录  3、修改商品库存和销量
                    alipayService.afterPaySuccess(paramsMap);

                }
                //验签成功
                return "success";
            }else{
                //验签失败
                return "failure";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 确认支付
     * @param orderNo
     * @return
     */
    //确认支付返回form表单
    @GetMapping("submitAlipay/{orderNo}")
    public Result submitAlipay(@PathVariable String orderNo){
        String form = alipayService.alipay(orderNo);//1、创建支付记录  2、调用支付宝接口
        return Result.build(form, ResultCodeEnum.SUCCESS);
    }
}
