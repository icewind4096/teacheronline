package com.windvalley.guli.service.cms.controller.api;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.cms.entity.Ad;
import com.windvalley.guli.service.cms.entity.form.AdInfoForm;
import com.windvalley.guli.service.cms.service.IAdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation("根据广告类别ID,获得对应广告列表")
    @PostMapping("list/{adTypeId}")
    public R getById(@ApiParam(value = "广告类别ID", required = true) @PathVariable String adTypeId) {
        List<Ad> adList = adService.listByAdTypeId(adTypeId);
        return R.ok().data("items", adList);
    }
}
