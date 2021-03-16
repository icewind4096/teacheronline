package com.windvalley.guli.service.sms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
//自动读取配置文件application.yml中的aliyun.sms下的配置，并配置到对应的类变量中去
@ConfigurationProperties(prefix = "aliyun.sms")
public class SMSProperties {
    private String regionId;
    private String keyid;
    private String keysecret;
    private String templateCode;
    private String signName;

}
