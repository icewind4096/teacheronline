package com.windvalley.guli.service.cms.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.cms.entity.Ad;
import com.windvalley.guli.service.cms.service.IAdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/cms/ad")
@Api(description = "广告推荐")
@Slf4j
public class ApiADController {
    @Autowired
    private IAdService adService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据广告类别ID,获得对应广告列表")
    @PostMapping("list/{adTypeId}")
    public R getById(@ApiParam(value = "广告类别ID", required = true) @PathVariable String adTypeId) {
        List<Ad> adList = adService.selectByAdTypeId(adTypeId);
        return R.ok().data("items", adList);
    }

    @ApiOperation("把广告条中的广告信息，存储到redis中去")
    @PostMapping("save-test")
    @ResponseBody
    public R saveAd(@ApiParam(value = "广告信息", required = true) @RequestBody Ad ad){
        redisTemplate.opsForValue().set("ad", ad);
        return R.ok();
    }

    @ApiOperation("把redis中存储的数据获取出来")
    @PostMapping("get-test/{key}")
    @ResponseBody
    public R getAd(@ApiParam(value = "redis的键", required = true) @PathVariable String key){
        Ad ad = (Ad) redisTemplate.opsForValue().get(key);
        return R.ok().data("item", ad);
    }

    @ApiOperation("删除redis中存储的数据")
    @PostMapping("delete-test/{key}")
    @ResponseBody
    public R delAd(@ApiParam(value = "redis的键", required = true) @PathVariable String key){
        Boolean result = redisTemplate.delete(key);
        if (result == true){
            return R.ok();
        } else{
            return R.error();
        }
    }
}
