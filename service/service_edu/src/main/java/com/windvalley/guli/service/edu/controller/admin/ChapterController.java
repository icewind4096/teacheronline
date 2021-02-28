package com.windvalley.guli.service.edu.controller.admin;


import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Chapter;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.entity.form.CourseInfoForm;
import com.windvalley.guli.service.edu.entity.vo.ChapterVO;
import com.windvalley.guli.service.edu.entity.vo.SubjectVO;
import com.windvalley.guli.service.edu.service.IVideoService;
import com.windvalley.guli.service.edu.service.impl.ChapterService;
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
@Api(description = "章节管理")
@RestController
@Slf4j
@RequestMapping("/admin/edu/chapter")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;

    @Autowired
    private IVideoService videoService;

    @ApiOperation("新增章节")
    @PostMapping("save")
    public R saveCourseInfo(@ApiParam(value = "章节信息", required = true) @RequestBody Chapter chapter){
        if (chapterService.save(chapter)) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据章节ID,获得章节数据")
    @PostMapping("get/{id}")
    public R getById(@ApiParam(value = "章节ID", required = true) @PathVariable String id) {
        Chapter chapter = chapterService.getById(id);
        if (chapter != null){
            return R.ok().data("item", chapter);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("修改章节数据")
    @PostMapping("update")
    public R updateById(@ApiParam(value = "章节对象", required = true) @RequestBody Chapter chapter) {
        boolean result = chapterService.updateById(chapter);
        if (result){
            return R.ok().message("修改章节数据成功");
        } else {
            return R.error().message("修改章节数据失败");
        }
    }

    @ApiOperation(value = "删除章节", notes = "根据章节ID,删除章节")
    @PostMapping("remove/{id}")
    public R removeById(@ApiParam("章节ID") @PathVariable String id){
        videoService.removeMediaVideoByChapterId(id);

        if (chapterService.removeChapterById(id)){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "章节数据列表，树形态", notes = "章节数据列表，树形态")
    @PostMapping("nestedlist/{courseId}")
    public R nestedListByCourseId(@ApiParam("课程ID") @PathVariable String courseId) {
        List<ChapterVO> chapterVOList = chapterService.nestedList(courseId);
        return R.ok().data("items", chapterVOList);
    }
}

