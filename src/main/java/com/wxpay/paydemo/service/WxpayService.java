package com.wxpay.paydemo.service;

import com.wxpay.paydemo.common.StatusCode;
import com.wxpay.paydemo.model.Result;
import com.wxpay.paydemo.model.WXPayConfig;
import com.wxpay.paydemo.util.CertHttpUtil;
import com.wxpay.paydemo.util.WxpayUtil;
import com.wxpay.paydemo.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoxiao 2020/1/8
 */
@Service
public class WxpayService {
    @Autowired
    private WXPayConfig wxPayConfig;

    //企业付款到零钱
    public Result returnWechatMoney(HttpServletRequest request, String money, String openId) throws Exception {
        //1.0 拼凑企业支付需要的参数
        //2.0 生成map集合
        Map<String, String> params = new HashMap<>();
        params.put("mch_appid", wxPayConfig.getAppid());//微信公众号的appid

        params.put("mchid", wxPayConfig.getMch_id());//商户号

        params.put("nonce_str", WxpayUtil.sessionGenerator(openId));//随机生成后数字，保证安全性

        params.put("partner_trade_no", WxpayUtil.getDateToString());//生成商户订单号

        params.put("openid", openId);// 支付给用户openid

        params.put("check_name", "NO_CHECK");//是否验证真实姓名呢

        params.put("re_user_name", "邻里用户");//收款用户姓名

        params.put("amount", money);//企业付款金额，单位为分

        params.put("desc", "退还篮子奖励金/收益提现");//企业付款操作说明信息。必填。

        params.put("spbill_create_ip", WxpayUtil.getIp());//调用接口的机器Ip地址
        //3.0 生成自己的签名
        try {
            String sign = WxpayUtil.generateSignature(params, wxPayConfig.getKey());//签名
            params.put("sign", sign);//封装退款对象
            String orderxml = XmlUtil.mapToXml(params);    //将数据转成XML格式
            String ru = CertHttpUtil.postData(wxPayConfig.getSend_url(),orderxml, wxPayConfig.getMch_id(), wxPayConfig.getCertificate_url());//退还零钱

            Map<String, String> returnMap = XmlUtil.xmlToMap(ru);//接口返回数据
            if (returnMap.containsKey("result_code") && returnMap.get("result_code").equals("SUCCESS")) {
                return new Result(true, StatusCode.OK, "企业付款到零钱成功。");
            } else if (returnMap.containsKey("err_code") && returnMap.get("err_code").equals("AMOUNT_LIMIT")) {//商户金额不足
                return new Result(false, StatusCode.AMOUNT_LIMIT, "商户余额不足，请联系相关人员处理。");
            } else if (returnMap.containsKey("err_code") && returnMap.get("err_code").equals("SENDNUM_LIMIT")) {//该用户今日付款次数超过限制
                return new Result(false, StatusCode.SENDNUM_LIMIT, "该用户今日付款次数超过限制,如有需要请登录微信支付商户平台更改API安全配置.");
            } else if (returnMap.containsKey("err_code") && returnMap.get("err_code").equals("OPENID_ERROR")) {
                return new Result(false, StatusCode.OPENID_ERROR, "openid与商户appid不匹配.");
            } else if (returnMap.containsKey("err_code") && returnMap.get("err_code").equals("MONEY_LIMIT")) {//已达到付款给此用户额度上限.
                return new Result(false, StatusCode.MONEY_LIMIT, "已达到付款给此用户额度上限.");
            } else {
                return new Result(false, StatusCode.ERROR, "企业付款到零钱出现未知错误。");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
