package com.windvalley.guli.service.edu.mapper;

import com.windvalley.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Repository
public interface ChapterMapper extends BaseMapper<Chapter> {
    List<Chapter> selectByCourseId(String courseId);
}
