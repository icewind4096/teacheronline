package com.windvalley.guli.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.windvalley.guli.service.edu.entity.Video;
import com.windvalley.guli.service.edu.entity.vo.CoursePublishVO;
import com.windvalley.guli.service.edu.entity.vo.CourseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {

    IPage<CourseVO> selectPageByCourseQuery(Page<CourseVO> pagePara, @Param(Constants.WRAPPER) QueryWrapper<CourseVO> queryWrapper);

    Video selectVideoByCourseId(@Param(Constants.WRAPPER) QueryWrapper<Video> queryWrapper);

    CoursePublishVO selectCoursePublishVOById(String id);
}
