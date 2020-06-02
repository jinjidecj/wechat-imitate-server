package com.zcj.wxpro.controller;

import com.alibaba.fastjson.JSON;
import com.zcj.wxpro.body.BaseResponse;
import com.zcj.wxpro.body.CommonBody;
import com.zcj.wxpro.config.JuHeDataConfig;
import com.zcj.wxpro.service.IUserService;
import com.zcj.wxpro.util.Common;
import com.zcj.wxpro.util.HTTPsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class NewsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JuHeDataConfig juHeDataConfig;
    //GetMapping 不支持@RequestBody
    @GetMapping(value = "/newsList")
    public String getNewsList(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            return getNews();
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }
    @GetMapping(value = "/testList")
    private String getNews(){

        Map<String,String> params = new HashMap<String, String>();
        params.put("key", juHeDataConfig.getNewsAppKey());
        String result = null;
        try {
            result = HTTPsUtils.get(juHeDataConfig.getNewsUrl(),params,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
