package com.windvalley.guli.service.edu.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Course;
import com.windvalley.guli.service.edu.entity.vo.ChapterVO;
import com.windvalley.guli.service.edu.entity.vo.WebCourseQueryVO;
import com.windvalley.guli.service.edu.entity.vo.WebCourseVO;
import com.windvalley.guli.service.edu.service.IChapterService;
import com.windvalley.guli.service.edu.service.ICourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(description = "课程")
@RestController
@RequestMapping("/api/edu/course")
@Slf4j
public class APICourseController {
    @Autowired
    private ICourseService courseService;

    @Autowired
    private IChapterService chapterService;

    @ApiOperation("课程列表")
    @PostMapping("list")
    public R pageList(@ApiParam(value = "课程查询对象", required = true) WebCourseQueryVO webCourseQueryVO) {
        List<Course> courseList = courseService.webSelectList(webCourseQueryVO);
        return R.ok().data("courseList", courseList);
    }

    @ApiOperation("根据课程id，查询课程信息")
    @PostMapping("get/{courseId}")
    public R getById(@ApiParam(value = "课程ID", required = true) @PathVariable String courseId) {
        WebCourseVO webCourseVO = courseService.selectWebCourseVOById(courseId);

        List<ChapterVO> chapterVOList = chapterService.nestedList(courseId);

        return R.ok().data("course", webCourseVO)
                     .data("chapterVOList", chapterVOList);
    }
}
