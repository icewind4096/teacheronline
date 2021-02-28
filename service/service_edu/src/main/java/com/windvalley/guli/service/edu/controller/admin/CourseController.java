package com.windvalley.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.entity.form.CourseInfoForm;
import com.windvalley.guli.service.edu.entity.vo.CoursePublishVO;
import com.windvalley.guli.service.edu.entity.vo.CourseQueryVO;
import com.windvalley.guli.service.edu.entity.vo.CourseVO;
import com.windvalley.guli.service.edu.entity.vo.TeacherQueryVO;
import com.windvalley.guli.service.edu.service.ICourseService;
import com.windvalley.guli.service.edu.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@CrossOrigin
@Api(description = "课程管理")
@RestController
@Slf4j
@RequestMapping("/admin/edu/course")
public class CourseController {
    @Autowired
    ICourseService courseService;

    @Autowired
    IVideoService videoService;

    @ApiOperation("新增课程")
    @PostMapping("save")
    public R saveCourseInfo(@ApiParam(value = "课程基本信息", required = true) @RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId).message("保存成功");
    }

    @ApiOperation("更新课程")
    @PostMapping("update")
    public R updateCourseInfo(@ApiParam(value = "课程基本信息", required = true) @RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseInfo(courseInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("根据课程ID,获得课程信息")
    @PostMapping("info/{id}")
    public R getById(@ApiParam(value = "课程ID", required = true) @PathVariable String id) {
        CourseInfoForm courseInfoForm   = courseService.getInfoById(id);
        if (courseInfoForm != null){
            return R.ok().data("item", courseInfoForm);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("课程分页列表")
    @PostMapping("list/{current}/{size}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long current
            , @ApiParam(value = "每页记录数", required = true) @PathVariable Long size
            , @ApiParam("查询条件对象") CourseQueryVO courseQueryVO) {
        Page<CourseVO> pagePara = new Page<>(current, size);

        IPage<CourseVO> pageModel = courseService.selectPage(pagePara, courseQueryVO);

        List<CourseVO> courseVOS = pageModel.getRecords();

        Long total = pageModel.getTotal();

        return R.ok().data("total", total).data("rows", courseVOS);
    }

    @ApiOperation(value = "删除课程", notes = "根据课程ID,删除课程")
    @PostMapping("remove/{id}")
    public R removeById(@ApiParam("课程ID") @PathVariable String id){
        courseService.removeCoverById(id);

        videoService.removeMediaVideoByCourseId(id);

        boolean result = courseService.removeCursorById(id);
        if (result){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据ID获取发布课程信息", notes = "根据ID获取发布课程信息")
    @PostMapping("publish/info/{id}")
    public R getPublishCourseVOById(@ApiParam("课程ID") @PathVariable String id){
        CoursePublishVO coursePublishVO = courseService.getCoursePublishVOById(id);
        if (coursePublishVO != null){
            return R.ok().data("item", coursePublishVO);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据ID发布课程信息", notes = "根据ID发布课程信息")
    @PostMapping("publish/{id}")
    public R publishCourseById(@ApiParam("课程ID") @PathVariable String id){
        if (courseService.publishCourseById(id) == true){
            return R.ok().message("课程发布成功");
        } else {
            return R.ok().message("数据不存在");
        }
    }
}
