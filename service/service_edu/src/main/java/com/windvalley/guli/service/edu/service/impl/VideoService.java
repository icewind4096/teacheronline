package com.windvalley.guli.service.edu.service.impl;

import com.windvalley.guli.service.edu.entity.Video;
import com.windvalley.guli.service.edu.feign.IVodMediaService;
import com.windvalley.guli.service.edu.mapper.VideoMapper;
import com.windvalley.guli.service.edu.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Service
public class VideoService extends ServiceImpl<VideoMapper, Video> implements IVideoService {
    @Autowired
    private IVodMediaService vodMediaService;

    @Override
    public void removeMediaVideoById(String id) {
        Video video = baseMapper.selectById(id);
        if (video != null) {
             vodMediaService.remove(video.getVideoSourceId());
        }
    }

    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
        List<Video> videos = baseMapper.selectByChapterId(chapterId);

        vodMediaService.removeByIdList(getVideoIdListFromVideoList(videos));
    }

    @Override
    public void removeMediaVideoByCourseId(String courseId) {
        List<Video> videos = baseMapper.selectByCourseId(courseId);

        vodMediaService.removeByIdList(getVideoIdListFromVideoList(videos));
    }

    private List<String> getVideoIdListFromVideoList(List<Video> videos) {
        List<String> videoIdList = new ArrayList<>();
        for (Video video : videos){
            videoIdList.add(video.getVideoSourceId());
        }
        return videoIdList;
    }
}
