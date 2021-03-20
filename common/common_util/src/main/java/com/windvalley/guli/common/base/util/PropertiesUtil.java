package com.windvalley.guli.common.base.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {
    private static Properties props;

    static {
        String fileName = "normal.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }

    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        } else {
            char[] chars = obj.toString().toCharArray();
            int length = chars.length;
            if (length < 1) {
                return false;
            } else {
                int i = 0;
                if (length > 1 && chars[0] == '-') {
                    i = 1;
                }

                while(i < length) {
                    if (!Character.isDigit(chars[i])) {
                        return false;
                    }

                    ++i;
                }

                return true;
            }
        }
    }

    public static Integer getProperty(String key, Integer defaultValue){
        String value = props.getProperty(key.trim()).trim();
        if(isNumeric(value)){
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public static Boolean getProperty(String key, boolean defaultValue) {
        String value = props.getProperty(key.trim()).trim();
        if (!StringUtils.isBlank(value)){
            return "true".equals(value.toLowerCase());
        }
        return defaultValue;
    }
}
