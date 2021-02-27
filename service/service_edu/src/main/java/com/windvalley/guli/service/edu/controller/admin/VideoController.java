package com.windvalley.guli.service.edu.controller.admin;


import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Video;
import com.windvalley.guli.service.edu.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@CrossOrigin
@Api(description = "课时管理")
@RestController
@Slf4j
@RequestMapping("/admin/edu/video")
public class VideoController {
    @Autowired
    private IVideoService videoService;

    @ApiOperation("新增课时")
    @PostMapping("save")
    public R saveCourseInfo(@ApiParam(value = "课时信息", required = true) @RequestBody Video video){
        if (videoService.save(video)) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据课时ID,获得课时数据")
    @PostMapping("get/{id}")
    public R getById(@ApiParam(value = "课时ID", required = true) @PathVariable String id) {
        Video video = videoService.getById(id);
        if (video != null){
            return R.ok().data("item", video);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("修改课时数据")
    @PostMapping("update")
    public R updateById(@ApiParam(value = "课时对象", required = true) @RequestBody Video video) {
        boolean result = videoService.updateById(video);
        if (result){
            return R.ok().message("修改课时数据成功");
        } else {
            return R.error().message("修改课时数据失败");
        }
    }

    @ApiOperation(value = "删除课时", notes = "根据章节ID,删除讲师")
    @PostMapping("remove/{id}")
    public R removeById(@ApiParam("章节ID") @PathVariable String id){
        // TODO: 2021/2/22 删除视频

        if (videoService.removeById(id)){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

}
