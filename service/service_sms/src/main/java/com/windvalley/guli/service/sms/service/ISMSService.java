package com.windvalley.guli.service.sms.service;

import com.aliyuncs.exceptions.ClientException;

public interface ISMSService {
    void send(String mobile, String checkCode) throws ClientException;
}
