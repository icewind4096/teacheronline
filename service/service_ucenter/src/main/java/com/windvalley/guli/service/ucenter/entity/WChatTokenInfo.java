package com.windvalley.guli.service.ucenter.entity;

import lombok.Data;

@Data
public class WChatTokenInfo {
    private String accessToken;
    private String openId;

    @Override
    public String toString() {
        return "WChatTokenInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
