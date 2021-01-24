package com.windvalley.guli.service.edu.service.impl;

import com.windvalley.guli.service.edu.entity.Video;
import com.windvalley.guli.service.edu.mapper.VideoMapper;
import com.windvalley.guli.service.edu.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
