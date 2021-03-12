package com.windvalley.guli.service.edu.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.entity.vo.CourseVO;
import com.windvalley.guli.service.edu.service.ICourseService;
import com.windvalley.guli.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@Api(description = "首页")
@RestController
@RequestMapping("/api/edu")
@Slf4j
public class APIIndexController {
    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private ICourseService courseService;

    @ApiOperation("课程与讲师额首页数据")
    @PostMapping("index")
    public R index(){
        List<CourseVO> courses = courseService.selectHotCouse();

        List<Teacher> teachers = teacherService.selectHotTeacher();

        return R.ok().data("courses", courses).data("teachers", teachers);
    }
}
