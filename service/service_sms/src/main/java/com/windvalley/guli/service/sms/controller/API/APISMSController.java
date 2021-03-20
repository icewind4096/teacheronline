package com.windvalley.guli.service.sms.controller.API;

import com.aliyuncs.exceptions.ClientException;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.FormUtils;
import com.windvalley.guli.common.base.util.RandomUtils;
import com.windvalley.guli.service.sms.service.ISMSService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@CrossOrigin
@Api(description = "短信管理")
@RestController
@RequestMapping("/api/sms")
@Slf4j
public class APISMSController {
    @Autowired
    ISMSService SMSService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {
        //产生验证码
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){
            log.error(ResultCodeEnum.LOGIN_PHONE_ERROR.getMessage());
            return R.error().code(ResultCodeEnum.LOGIN_PHONE_ERROR.getCode()).message(ResultCodeEnum.LOGIN_PHONE_ERROR.getMessage());
        }

        String checkCode = RandomUtils.getSixBitRandom();

        //发送验证码
        //SMSService.send(mobile, checkCode);

        //存储验证码到Redis
        redisTemplate.opsForValue().set(mobile, checkCode, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功").data("code", checkCode);
    }
}
