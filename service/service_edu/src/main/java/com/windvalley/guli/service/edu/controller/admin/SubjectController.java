package com.windvalley.guli.service.edu.controller.admin;


import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.ExceptionUtils;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.edu.entity.vo.SubjectVO;
import com.windvalley.guli.service.edu.service.ISubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@CrossOrigin
@Api(description = "课程分类管理")
@RestController
@Slf4j
@RequestMapping("/admin/edu/subject")
public class SubjectController {
    @Autowired
    private ISubjectService subjectService;

    @ApiOperation(value = "课程分类数据Excel批量导入", notes = "根据Excel文件数据，批量导入课程分类")
    @PostMapping("import")
    public R batchImport(@ApiParam("上传的excel文件") @RequestParam("file")MultipartFile file) {
        try {
            subjectService.batchImport(file.getInputStream());
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation(value = "课程分类数据列表，数形态", notes = "课程分类数据列表，数形态")
    @PostMapping("nested-list")
    public R nestedList() {
        List<SubjectVO> subjectVOList = subjectService.nestedList();
        return R.ok().data("items", subjectVOList);
    }
}

