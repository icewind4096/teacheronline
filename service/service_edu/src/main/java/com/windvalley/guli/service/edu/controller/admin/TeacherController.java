package com.windvalley.guli.service.edu.controller.admin;


import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.service.ITeacherService;
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
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;

    @PostMapping("list")
    public List<Teacher> listAll(){
        return teacherService.list();
    }

    @PostMapping("remove/{id}")
    public boolean removeById(@PathVariable String id){
        return teacherService.removeById(id);
    }
}

