package com.windvalley.guli.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.open")
//@ConfigurationProperties 告诉springboot，将  properties/yml 配置文件中的对应属性的值，映射到这个组件类中，与类中的属性进行一一绑定
//prefix = "wx.open"  表示将配置文件中 前缀为wx.open的属性来与这个类中进行映射
public class UCenterProperties {
    private String appid;
    private String appSecret;
    private String redirectURI;
}
