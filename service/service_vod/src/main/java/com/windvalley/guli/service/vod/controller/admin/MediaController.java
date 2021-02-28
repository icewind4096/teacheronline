package com.windvalley.guli.service.vod.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.ExceptionUtils;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.vod.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(description = "阿里云VOD管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class MediaController {
    @Autowired
    private IVideoService videoService;

    @ApiOperation(value = "上传视频文件", notes = "上传视频文件到阿里云")
    @PostMapping("upload")
    public R upload(@ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file)  {
        try {
            String originalFileName = file.getOriginalFilename();
            String videoId = videoService.upload(file.getInputStream(), originalFileName);
            return R.ok().message("视频文件上传成功").data("videoId", videoId);
        } catch (IOException e) {
            log.error("上传失败 {}" + ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @ApiOperation(value = "删除视频文件", notes = "删除阿里云视频文件")
    @PostMapping("remove/{id}")
    public R remove(@ApiParam(name = "id", value = "视频文件ID", required = true) @PathVariable String id)  {
        try {
            videoService.removeById(id);
            return R.ok().message("删除视频文件成功");
        } catch (ClientException e) {
            log.error("删除视频文件失败 {}" + ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @ApiOperation(value = "删除视频文件", notes = "删除阿里云视频文件")
    @PostMapping("remove")
    public R removeByIdList(@ApiParam(name = "ids", value = "视频文件ID列表", required = true) @RequestBody List<String> ids)  {
        try {
            videoService.removeByIdList(ids);
            return R.ok().message("删除视频文件成功");
        } catch (ClientException e) {
            log.error("删除视频文件失败 {}" + ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

}
