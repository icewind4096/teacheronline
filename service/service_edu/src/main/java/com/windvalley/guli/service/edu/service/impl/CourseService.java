package com.windvalley.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.*;
import com.windvalley.guli.service.edu.entity.form.CourseInfoForm;
import com.windvalley.guli.service.edu.entity.vo.*;
import com.windvalley.guli.service.edu.feign.IOssFileService;
import com.windvalley.guli.service.edu.mapper.*;
import com.windvalley.guli.service.edu.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
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
public class CourseService extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    IOssFileService ossFileService;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CourseCollectMapper courseCollectMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存 cursor
        Course course = saveCourseInfoByFormInfo(courseInfoForm);

        //保存 cursor description
        saveCourseDescByFormInfo(course.getId(), courseInfoForm);

        return course.getId();
    }

    @Override
    public CourseInfoForm getInfoById(String courseId) {
        Course course = baseMapper.selectById(courseId);

        if (course == null) return null;

        CourseDescription courseDescription = courseDescriptionMapper.selectById(courseId);

        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        //  修改 cursor
        Course course = updateCourseInfoByFormInfo(courseInfoForm);

        //保存 cursor description
        updateCourseDescByFormInfo(course.getId(), courseInfoForm);
    }

    @Override
    public IPage<CourseVO> selectPage(Page<CourseVO> pagePara, CourseQueryVO courseQueryVO) {
        QueryWrapper<CourseVO> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("a.gmt_create");

        if (courseQueryVO == null){
            return baseMapper.selectPageByCourseQuery(pagePara, queryWrapper);
        }

        if (!StringUtils.isEmpty(courseQueryVO.getTitle())){
            queryWrapper.like("a.title", courseQueryVO.getTitle());
        }

        if (!StringUtils.isEmpty(courseQueryVO.getTeacherId() )){
            queryWrapper.eq("a.teacher_id", courseQueryVO.getTeacherId());
        }

        if (!StringUtils.isEmpty(courseQueryVO.getSubjectId())){
            queryWrapper.eq("a.subject_id", courseQueryVO.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQueryVO.getSubjectParentId())){
            queryWrapper.eq("a.subject_parent_id", courseQueryVO.getSubjectParentId());
        }

        return baseMapper.selectPageByCourseQuery(pagePara, queryWrapper);
    }

    private void updateCourseDescByFormInfo(String courseId, CourseInfoForm courseInfoForm) {
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(courseId);
        courseDescription.setDescription(courseInfoForm.getDescription());

        courseDescriptionMapper.updateById(courseDescription);
    }

    private Course updateCourseInfoByFormInfo(CourseInfoForm courseInfoForm) {
        Course course = new Course();

        BeanUtils.copyProperties(courseInfoForm, course);

        baseMapper.updateById(course);

        return course;
    }

    private void saveCourseDescByFormInfo(String courseId, CourseInfoForm courseInfoForm) {
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(courseId);
        courseDescription.setDescription(courseInfoForm.getDescription());

        courseDescriptionMapper.insert(courseDescription);
    }

    private Course saveCourseInfoByFormInfo(CourseInfoForm courseInfoForm) {
        Course course = new Course();

        BeanUtils.copyProperties(courseInfoForm, course);

        course.setStatus(Course.COURSE_DRAFT);

        baseMapper.insert(course);

        return course;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCursorById(String id) {
        return removeVideoByCourseId(id) & removeChapterByCourseId(id) & removeCommentByCourseId(id)
             & removeCollectByCourseId(id) & removeDescriptByCursorId(id) & removeCursorInfoById(id);
    }

    private boolean removeCollectByCourseId(String id) {
        QueryWrapper<CourseCollect> queryWrapper= new QueryWrapper<CourseCollect>();

        queryWrapper.eq("course_id", id);

        courseCollectMapper.delete(queryWrapper);

        return true;
    }

    private boolean removeCommentByCourseId(String id) {
        QueryWrapper<Comment> queryWrapper= new QueryWrapper<Comment>();

        queryWrapper.eq("course_id", id);

        commentMapper.delete(queryWrapper);

        return true;
    }

    private boolean removeChapterByCourseId(String id) {
        QueryWrapper<Chapter> queryWrapper= new QueryWrapper<Chapter>();

        queryWrapper.eq("course_id", id);

        chapterMapper.delete(queryWrapper);

        return true;
    }

    private boolean removeVideoByCourseId(String id) {
        QueryWrapper<Video> queryWrapper= new QueryWrapper<Video>();

        queryWrapper.eq("course_id", id);

        videoMapper.delete(queryWrapper);

        return true;
    }

    private boolean removeDescriptByCursorId(String id) {
        courseDescriptionMapper.deleteById(id);

        return true;
    }

    private boolean removeCursorInfoById(String id) {
        baseMapper.deleteById(id);

        return true;
    }

    @Override
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);

        if (course != null){
            if (StringUtils.isEmpty(course.getCover()) == false){
                R r = ossFileService.deleteFile(course.getCover());
                return r.getSuccess();
            }
        }

        return false;
    }

    @Override
    public CoursePublishVO getCoursePublishVOById(String id) {
        return baseMapper.selectCoursePublishVOById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course = new Course();

        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);

        return baseMapper.updateById(course) > 0;
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVO webCourseQueryVO) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper();

        queryWrapper.eq("status", Course.COURSE_NORMAL);

        if (!StringUtils.isEmpty(webCourseQueryVO.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id", webCourseQueryVO.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getSubjectId())){
            queryWrapper.eq("subject_id", webCourseQueryVO.getSubjectId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getBuyCountSort())){
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getGmtCreateSort())){
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getPriceSort())){
            if (webCourseQueryVO.getOrderBy() == null || webCourseQueryVO.getOrderBy().equals("1")){
                queryWrapper.orderByDesc("price");
            } else {
                queryWrapper.orderByAsc("price");
            }
        }

        if (!StringUtils.isEmpty(webCourseQueryVO.getBuyCountSort())){
            queryWrapper.orderByDesc("buy_count");
        }

        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVO selectWebCourseVOById(String courseId) {
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);

        baseMapper.updateById(course);

        return baseMapper.selectWebCourseVOById(courseId);
    }

    @Cacheable(value = "index", key = "'selectHotCouse'")
    @Override
    public List<CourseVO> selectHotCouse() {
        QueryWrapper<CourseVO> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.eq("status", Course.COURSE_NORMAL);
        queryWrapper.last("limit 8");

        return baseMapper.selectHotCourseList(queryWrapper);
    }
}
