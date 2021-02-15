package com.windvalley.guli.service.oss.controller.admin;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.ExceptionUtils;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.oss.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Api(description = "阿里云文件管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {
    @Autowired
    private IFileService fileService;

    @ApiOperation(value = "上传文件", notes = "上传文件到阿里云")
    @PostMapping("upload")
    public R upload(@ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file,
                    @ApiParam(value = "目标目录", required = true) @RequestParam("destDir") String destDir)  {
        try {
            String originalFileName = file.getOriginalFilename();

            String uploadFileURL = null;

            uploadFileURL = fileService.upload(file.getInputStream(), destDir, originalFileName);

            return R.ok().message("文件上传成功").data("url", uploadFileURL);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "删除文件", notes = "删除阿里云中的文件")
    @PostMapping("delete")
    public R deleteFile(@ApiParam(value = "文件URL", required = true) @RequestBody String url){
        fileService.delete(url);

        return R.ok().message("删除文件成功").data("url", url);
    }

    @ApiOperation(value = "测试微服务调用")
    @PostMapping("test")
    public R test(){
        log.info("oss test 被调用");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok();
    }


}
