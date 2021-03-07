package com.windvalley.guli.service.edu.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.feign.IOssFileService;
import com.windvalley.guli.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Api(description = "讲师管理系统")
@RestController
@RequestMapping("/api/edu/teacher")
@Slf4j
public class APITeacherController {
    @Autowired
    private ITeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @PostMapping("list")
    public R listAll(){
        List<Teacher> teacherList = teacherService.list();
        return R.ok().data("items", teacherList);
    }

    @ApiOperation("得到讲师信息")
    @PostMapping("get/{id}")
    public R get(@ApiParam("讲师ID") @PathVariable String id){
        Map<String, Object> teacherInfo = teacherService.selectTeacherInfoById(id);
        return R.ok().data("items", teacherInfo);
    }
}
