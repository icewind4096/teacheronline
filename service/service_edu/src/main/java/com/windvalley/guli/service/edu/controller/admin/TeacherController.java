package com.windvalley.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.feign.IOssFileService;
import com.windvalley.guli.service.edu.service.ITeacherService;
import com.windvalley.guli.service.edu.entity.vo.TeacherQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@CrossOrigin
@Api(description = "讲师管理系统")
@RestController
@RequestMapping("/admin/edu/teacher")
@Slf4j
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IOssFileService ossFileService;

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
            teacherService.deleteAvatarById(id);

            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "批量删除讲师", notes = "根据讲师ID列表,删除讲师")
    @PostMapping("batremove")
    public R batRemoveById(@ApiParam("讲师Id列表") @RequestBody List<String> ids){
        boolean result = teacherService.removeByIds(ids);
        if (result){
            return R.ok().message("批量删除成功");
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
        if (teacherService.save(teacher)){
            return R.ok().message("保存讲师数据成功");
        } else {
            return R.error().message("保存讲师数据失败");
        }
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
    public R getById(@ApiParam(value = "讲师ID", required = true) @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        if (teacher != null){
            return R.ok().data("item", teacher);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据讲师姓名,获得讲师姓名列表")
    @PostMapping("list/name/{key}")
    public R getNameListByKey(@ApiParam(value = "查询讲师姓名条件", required = true) @PathVariable String key) {
        List<Map<String, Object>> nameList = teacherService.getNameListByKey(key);
        return R.ok().data("nameList", nameList);
    }

    @ApiOperation("测试OSS服务调用")
    @PostMapping("test")
    public R test(){
        ossFileService.test();
        return R.ok();
    }

    @ApiOperation("测试并发服务")
    @PostMapping("test_concurrent")
    public R testConcurrent(){
        log.info("test concurrent");
        return R.ok();
    }

    @PostMapping("message1")
    public String message1(){
        return "message1";
    }

    @PostMapping("message2")
    public String message2(){
        return "message2";
    }
}

