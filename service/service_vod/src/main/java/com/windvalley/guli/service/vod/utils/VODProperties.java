package com.windvalley.guli.service.vod.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
//自动读取配置文件application.yml中的aliyun.vod下的配置，并配置到对应的类变量中去
@ConfigurationProperties(prefix = "aliyun.vod")
public class VODProperties {
    private String keyid;
    private String keysecret;
    private String templateGroupId;
    private String workflowId;
}
