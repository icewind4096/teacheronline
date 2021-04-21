package com.windvalley.guli.service.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

    //这个配置不是必须的,如果不写，也可以使用redishttpsession
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        //设置spring session默认的cook值由SESSIONID 替换为JESSIONID
        defaultCookieSerializer.setCookieName("JSESSIONID");
        //CookiePath设置为根路径
        defaultCookieSerializer.setCookiePath("/");
        //把所有的cookie写到父域下面
        defaultCookieSerializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return defaultCookieSerializer;
    }
}
