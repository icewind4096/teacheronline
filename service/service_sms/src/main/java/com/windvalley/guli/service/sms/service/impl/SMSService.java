package com.windvalley.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.sms.service.ISMSService;
import com.windvalley.guli.service.sms.util.SMSProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class SMSService implements ISMSService {
    @Autowired
    SMSProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getKeyid(), smsProperties.getKeysecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        //==内容不可以改动，和发送短信服务器有关
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        //==内容不可以改动，和发送短信模板有关
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        //==只需要修改2个参数，电话号码和内容的变量值
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("TemplateParam",String.format("{\"code\":\"%s\"}", checkCode));

        CommonResponse response = client.getCommonResponse(request);

        Gson gSon = new Gson();
        HashMap<String, String> data = gSon.fromJson(response.getData(), HashMap.class);

        if (!"OK".equals(data.get("Code"))){
            log.error("短信发送失败：code = {}, message={}", data.get("Code"), data.get("Message"));
            if ("isv.BUSINESS_LIMIT_CONTROL".equals(data.get("Code"))){
                throw new WindvalleyException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL.getMessage(), ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL.getCode());
            }
            throw new WindvalleyException(data.get("Message"), ResultCodeEnum.SMS_SEND_ERROR.getCode());
        }
    }
}
