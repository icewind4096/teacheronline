package com.windvalley.guli.service.edu.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.vo.SubjectVO;
import com.windvalley.guli.service.edu.service.ISubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@Api(description = "课程分类")
@RestController
@RequestMapping("/api/edu/course")
@Slf4j
public class APISubjectController {
    @Autowired
    private ISubjectService subjectService;

    @ApiOperation(value = "课程分类数据列表，数形态", notes = "课程分类数据列表，数形态")
    @PostMapping("nested-list")
    public R nestedList() {
        List<SubjectVO> subjectVOList = subjectService.nestedList();
        return R.ok().data("items", subjectVOList);
    }
}
