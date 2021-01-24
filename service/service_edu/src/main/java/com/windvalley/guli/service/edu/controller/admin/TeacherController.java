package com.windvalley.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.service.ITeacherService;
import com.windvalley.guli.service.edu.vo.TeacherQueryVO;
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
    public R listAll(){
        List<Teacher> teacherList = teacherService.list();
        return R.ok().data("items", teacherList);
    }

    @ApiOperation(value = "删除讲师", notes = "根据讲师ID,删除讲师")
    @PostMapping("remove/{id}")
    public R removeById(@ApiParam("讲师ID") @PathVariable String id){
        boolean result = teacherService.removeById(id);
        if (result){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("讲师分页列表")
    @PostMapping("list/{current}/{size}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long current
                     , @ApiParam(value = "每页记录数", required = true) @PathVariable Long size
                     , @ApiParam("查询条件对象") TeacherQueryVO teacherQueryVO) {
        Page<Teacher> pagePara = new Page<>(current, size);

        IPage<Teacher> pageModel = teacherService.selectPage(pagePara, teacherQueryVO);

        List<Teacher> teachers = pageModel.getRecords();

        Long total = pageModel.getTotal();

        return R.ok().data("total", total).data("rows", teachers);
    }

    @ApiOperation("保存讲师数据")
    @PostMapping("save")
    public R save(@ApiParam(value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return R.ok().message("保存讲师数据成功");
    }

    @ApiOperation("修改讲师数据")
    @PostMapping("update")
    public R updateById(@ApiParam(value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        boolean result = teacherService.updateById(teacher);
        if (result){
            return R.ok().message("修改讲师数据成功");
        } else {
            return R.error().message("修改讲师数据失败");
        }
    }

    @ApiOperation("根据讲师ID,获得讲师数据")
    @PostMapping("get/{id}")
    public R delete(@ApiParam(value = "讲师ID", required = true) @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        if (teacher != null){
            return R.ok().data("item", teacher);
        } else {
            return R.error().message("数据不存在");
        }
    }
}

