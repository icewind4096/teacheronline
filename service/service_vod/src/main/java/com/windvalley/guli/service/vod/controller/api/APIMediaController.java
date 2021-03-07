package com.windvalley.guli.service.vod.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.ExceptionUtils;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.vod.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "阿里云VOD点播")
@CrossOrigin
@RestController
@RequestMapping("/api/vod/media")
@Slf4j
public class APIMediaController {
    @Autowired
    private IVideoService videoService;

    @ApiOperation(value = "得到需要播放的视频凭证", notes = "得到需要播放的视频凭证")
    @PostMapping("getPlayAuthup/{videoSourceId}")
    public R getPlayAuth(@ApiParam(value = "需要播放的视频文件的ID", required = true) @PathVariable("videoSourceId") String videoSourceId){
        try {
            String playAuth = videoService.getPlayAuthByVideoSourceId(videoSourceId);
            return R.ok().message("获得播放凭证成功").data("playAuth", playAuth);
        } catch (Exception e) {
            log.error("获取播放凭证失败 {}" + ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }
}
