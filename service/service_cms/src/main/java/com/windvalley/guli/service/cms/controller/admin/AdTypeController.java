package com.windvalley.guli.service.cms.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.cms.entity.AdType;
import com.windvalley.guli.service.cms.entity.form.AdTypeInfoForm;
import com.windvalley.guli.service.cms.service.IAdTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/cms/ad-type")
@Api(description = "广告类别管理")
@Slf4j
public class AdTypeController {
    @Autowired
    private IAdTypeService adTypeService;

    @ApiOperation("广告类别列表")
    @PostMapping("list")
    public R list() {
        List<AdType> adTypeList = adTypeService.list();
        return R.ok().data("items", adTypeList);
    }

    @ApiOperation("广告类别分页列表")
    @PostMapping("pageList/{current}/{size}")
    public R pageList(@ApiParam(value = "当前页码", required = true) @PathVariable Long current
            , @ApiParam(value = "每页记录数", required = true) @PathVariable Long size) {
        Page<AdType> pagePara = new Page<>(current, size);

        IPage<AdType> pageModel = adTypeService.selectPage(pagePara);

        List<AdType> adTypeList = pageModel.getRecords();

        Long total = pageModel.getTotal();

        return R.ok().data("total", total).data("rows", adTypeList);
    }

    @ApiOperation("新增广告")
    @PostMapping("save")
    public R saveCourseInfo(@ApiParam(value = "广告基本信息", required = true) @RequestBody AdTypeInfoForm adTypeInfoForm){
        String adId = adTypeService.saveAdTypeInfo(adTypeInfoForm);
        return R.ok().data("adId", adId).message("保存成功");
    }

    @ApiOperation(value = "删除广告", notes = "根据广告ID,删除广告")
    @PostMapping("remove/{id}")
    public R removeById(@ApiParam("广告ID") @PathVariable String id){
        boolean result = adTypeService.removeADTypeById(id);
        if (result){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("更新广告")
    @PostMapping("update")
    public R updateInfo(@ApiParam(value = "课程基本信息", required = true) @RequestBody AdTypeInfoForm adTypeInfoForm){
        adTypeService.updateADTypeInfo(adTypeInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("根据广告ID,获得广告信息")
    @PostMapping("get/{id}")
    public R getById(@ApiParam(value = "广告ID", required = true) @PathVariable String id) {
        AdTypeInfoForm adTypeInfoForm = adTypeService.getInfoById(id);
        if (adTypeInfoForm != null){
            return R.ok().data("item", adTypeInfoForm);
        } else {
            return R.ok().message("数据不存在");
        }
    }
}