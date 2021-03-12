package com.windvalley.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.edu.entity.form.CourseInfoForm;
import com.windvalley.guli.service.edu.entity.vo.*;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
public interface ICourseService extends IService<Course> {
    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getInfoById(String courseId);

    void updateCourseInfo(CourseInfoForm courseInfoForm);

    IPage<CourseVO> selectPage(Page<CourseVO> pagePara, CourseQueryVO courseQueryVO);

    boolean removeCursorById(String id);

    boolean removeCoverById(String id);

    CoursePublishVO getCoursePublishVOById(String id);

    boolean publishCourseById(String id);

    List<Course> webSelectList(WebCourseQueryVO webCourseQueryVO);

    WebCourseVO selectWebCourseVOById(String courseId);

    List<CourseVO> selectHotCouse();
}
