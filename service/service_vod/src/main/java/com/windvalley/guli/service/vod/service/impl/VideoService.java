package com.windvalley.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.vod.service.IVideoService;
import com.windvalley.guli.service.vod.utils.VODProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VideoService implements IVideoService {
    @Autowired
    VODProperties vodProperties;

    @Override
    public String upload(InputStream inputStream, String originalFileName) {
        String title = getFileNameWithoutExt(originalFileName);

        UploadStreamRequest request = new UploadStreamRequest(vodProperties.getKeyid(), vodProperties.getKeysecret(), title, originalFileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = response.getVideoId();
        if (StringUtils.isEmpty(videoId) == true){
            log.error("上传失败 ErrorCode:{} -> {}", response.getCode(), response.getMessage());
            throw new WindvalleyException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return videoId;
    }

    private String getFileNameWithoutExt(String originalFileName) {
        if (originalFileName.contains(".")){
            return originalFileName.substring(0, originalFileName.lastIndexOf("."));
        } else {
            return originalFileName;
        }
    }

    @Override
    public void removeById(String id) throws ClientException {
        DefaultAcsClient client = initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        request.setVideoIds(id);

        client.getAcsResponse(request);
    }

    @Override
    public void removeByIdList(List<String> ids) throws ClientException {
        DefaultAcsClient client = initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        //支持传入多个视频ID，多个用逗号分隔, 最多20个
        while (ids.size() > 0){
            request.setVideoIds(getRemoveIds(ids));
            client.getAcsResponse(request);
        }
    }

    private String getRemoveIds(List<String> idList) {
        int count = 0;

        StringBuffer ids = new StringBuffer();

        while (count < 20 && idList.size() > 0){
            ids.append(idList.get(0) + ",");
            idList.remove(0);
            count = count + 1;
        }

        if (StringUtils.isEmpty(ids) == false){
            ids.deleteCharAt(ids.lastIndexOf(","));
        }

        return ids.toString();
    }

    private DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";  // 点播服务接入区域 中国大陆全部选上海
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 获取视频信息
     * @param client 发送请求客户端
     * @return GetVideoInfoResponse 获取视频信息响应数据
     * @throws Exception
     */
    public static GetVideoInfoResponse getVideoInfo(DefaultAcsClient client, String videoSourceId) throws Exception {
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId(videoSourceId);
        return client.getAcsResponse(request);
    }

    @Override
    public String getPlayAuthByVideoSourceId(String videoSourceId) throws ClientException {
        DefaultAcsClient client = initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);
        request.setAuthInfoTimeout(200L);  //200秒内必须播放，否则凭证无效

        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        return response.getPlayAuth();
    }
}
