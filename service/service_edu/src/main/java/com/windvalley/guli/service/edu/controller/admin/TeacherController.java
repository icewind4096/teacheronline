package com.windvalley.guli.service.edu.controller.admin;


import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Api(description = "讲师管理系统")
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @PostMapping("list")
    public List<Teacher> listAll(){
        return teacherService.list();
    }

    @ApiOperation(value = "删除讲师", notes = "根据讲师ID,删除讲师")
    @PostMapping("remove/{id}")
    public boolean removeById(@ApiParam("讲师ID") @PathVariable String id){
        return teacherService.removeById(id);
    }
}

