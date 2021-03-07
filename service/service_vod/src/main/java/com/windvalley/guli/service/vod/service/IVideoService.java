package com.windvalley.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

public interface IVideoService {
    /**
     *
     * @param inputStream
     * @param originalFileName
     * @return video id
     */
    String upload(InputStream inputStream, String originalFileName);
    void removeById(String id) throws ClientException;
    void removeByIdList(List<String> ids) throws ClientException;
    String getPlayAuthByVideoSourceId(String videoSourceId) throws Exception;
}
