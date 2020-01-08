package com.wxpay.paydemo.controller;

import com.wxpay.paydemo.model.Result;
import com.wxpay.paydemo.service.WxpayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhaoxiao 2020/1/8
 */
@Controller
public class PayController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WxpayService wxpayService;

    @ResponseBody
    @GetMapping(value="/sendMoney")
    public Result sendMoney(HttpServletRequest request) {
        try {
            return wxpayService.returnWechatMoney(request,"100","wx120938");
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        return new Result();
    }
}
