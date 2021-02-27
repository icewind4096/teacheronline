package com.windvalley.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.windvalley.guli.service.edu.entity.Chapter;
import com.windvalley.guli.service.edu.entity.Video;
import com.windvalley.guli.service.edu.entity.vo.ChapterVO;
import com.windvalley.guli.service.edu.entity.vo.VideoVO;
import com.windvalley.guli.service.edu.mapper.ChapterMapper;
import com.windvalley.guli.service.edu.mapper.VideoMapper;
import com.windvalley.guli.service.edu.service.IChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Service
public class ChapterService extends ServiceImpl<ChapterMapper, Chapter> implements IChapterService {
    @Autowired
    VideoMapper videoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String chapterId) {
        return removeVideoByChapterId(chapterId) & removeChapterByIdChaperId(chapterId);
    }

    private boolean removeChapterByIdChaperId(String chapterId) {
        baseMapper.deleteById(chapterId);

        return true;
    }

    private boolean removeVideoByChapterId(String chapterId) {
        QueryWrapper<Video> queryWrapper= new QueryWrapper<Video>();

        queryWrapper.eq("chapter_id", chapterId);

        videoMapper.delete(queryWrapper);

        return true;
    }

    @Override
    public List<ChapterVO> nestedList(String courseId) {
        List<ChapterVO> chapterVOList = new ArrayList<ChapterVO>();

        List<Chapter> chapterList = baseMapper.selectByCourseId(courseId);

        List<Video> videoList = videoMapper.selectByCourseId(courseId);

        for (Chapter chapter: chapterList) {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(chapter, chapterVO);
            chapterVOList.add(chapterVO);
        }

        for (Video video: videoList){
            int index = findChapterByChapterId(video.getChapterId(), chapterVOList);
            if (index >= 0) {
                VideoVO videoVO = new VideoVO();
                BeanUtils.copyProperties(video, videoVO);
                chapterVOList.get(index).getChildren().add(videoVO);
            }
        }

        return chapterVOList;
    }

    private int findChapterByChapterId(String videoChapterId, List<ChapterVO> chapterVOList) {
        for (int i = 0; i < chapterVOList.size(); i ++){
            if (chapterVOList.get(i).getId().equals(videoChapterId)){
                return i;
            }
        }
        return -1;
    }
}
