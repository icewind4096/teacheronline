package com.windvalley.guli.service.edu.service;

import com.windvalley.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.edu.entity.vo.ChapterVO;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
public interface IChapterService extends IService<Chapter> {

    boolean removeChapterById(String chapterId);

    List<ChapterVO> nestedList(String courseId);
}
