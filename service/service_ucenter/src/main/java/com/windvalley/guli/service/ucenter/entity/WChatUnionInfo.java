package com.windvalley.guli.service.ucenter.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class WChatUnionInfo {
    private String openid;                  // 普通用户的标识，对当前开发者帐号唯一
    private String nickname;                // 普通用户昵称
    private Integer sex; 	                // 普通用户性别，1为男性，2为女性
    private String province;                // 普通用户个人资料填写的省份
    private String city; 	                // 普通用户个人资料填写的城市
    private String country; 	            // 国家，如中国为CN
    private String headimgurl; 	            // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private ArrayList<String> privilege;    // 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
    private String unionid;                 // 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。

    public WChatUnionInfo() {
        privilege = new ArrayList<>();
    }

}
