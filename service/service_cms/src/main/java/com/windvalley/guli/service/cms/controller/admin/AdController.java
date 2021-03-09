package com.windvalley.guli.service.cms.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.cms.entity.AdType;
import com.windvalley.guli.service.cms.entity.VO.AdVO;
import com.windvalley.guli.service.cms.entity.form.AdInfoForm;
import com.windvalley.guli.service.cms.entity.form.AdTypeInfoForm;
import com.windvalley.guli.service.cms.service.IAdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/cms/ad")
@Api(description = "广告推荐管理")
@Slf4j
public class AdController {
    @Autowired
    private IAdService adService;

    @ApiOperation("新增推荐")
    @PostMapping("save")
    public R save(@ApiParam(value = "广告基本信息", required = true) @RequestBody AdInfoForm adInfoForm){
        String adId = adService.saveAdInfo(adInfoForm);
        return R.ok().data("adId", adId).message("保存成功");
    }

    @ApiOperation("更新推荐")
    @PostMapping("update")
    public R updateInfo(@ApiParam(value = "推荐基本信息", required = true) @RequestBody AdInfoForm adInfoForm){
        adService.updateAdInfo(adInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("根据推荐ID,获得推荐信息")
    @PostMapping("get/{id}")
    public R getById(@ApiParam(value = "推荐ID", required = true) @PathVariable String id) {
        AdInfoForm adInfoForm = adService.getInfoById(id);
        if (adInfoForm != null){
            return R.ok().data("item", adInfoForm);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("推荐分页列表")
    @PostMapping("pageList/{current}/{size}")
    public R pageList(@ApiParam(value = "当前页码", required = true) @PathVariable Long current
            , @ApiParam(value = "每页记录数", required = true) @PathVariable Long size) {
        Page<AdVO> pagePara = new Page<>(current, size);

        IPage<AdVO> pageModel = adService.selectPage(pagePara);

        List<AdVO> adTypeList = pageModel.getRecords();

        Long total = pageModel.getTotal();

        return R.ok().data("total", total).data("rows", adTypeList);
    }

    @ApiOperation(value = "删除推荐", notes = "根据推荐ID,删除推荐")
    @PostMapping("remove/{id}")
    public R removeById(@ApiParam("推荐ID") @PathVariable String id){
        //删除图片
        adService.removeImageById(id);

        boolean result = adService.removeADById(id);
        return R.ok().message("删除成功");
    }
}
