package com.windvalley.guli.service.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.cms.entity.Ad;
import com.windvalley.guli.service.cms.entity.VO.AdVO;
import com.windvalley.guli.service.cms.entity.form.AdInfoForm;
import com.windvalley.guli.service.cms.feign.IOssFileService;
import com.windvalley.guli.service.cms.mapper.AdMapper;
import com.windvalley.guli.service.cms.service.IAdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
@Service
public class AdService extends ServiceImpl<AdMapper, Ad> implements IAdService {
    @Autowired
    IOssFileService ossFileService;

    @Override
    public String saveAdInfo(AdInfoForm adInfoForm) {
        Ad ad = new Ad();
        BeanUtils.copyProperties(adInfoForm, ad);

        baseMapper.insert(ad);

        return ad.getId();
    }

    @Override
    public void updateAdInfo(AdInfoForm adInfoForm) {
        Ad ad = new Ad();
        BeanUtils.copyProperties(adInfoForm, ad);

        baseMapper.updateById(ad);
    }

    @Override
    public AdInfoForm getInfoById(String id) {
        Ad ad = baseMapper.selectById(id);

        AdInfoForm adInfoForm = new AdInfoForm();
        BeanUtils.copyProperties(ad, adInfoForm);

        return adInfoForm;
    }

    @Override
    public IPage<AdVO> selectPage(Page<AdVO> pagePara) {
        QueryWrapper<AdVO> queryWrapper = new QueryWrapper();

        queryWrapper.orderByAsc("a.type_id", "a.sort");

        return baseMapper.selectPageByWrapper(pagePara, queryWrapper);
    }

    @Override
    public boolean removeADById(String id) {
        baseMapper.deleteById(id);

        return true;
    }

    @Override
    public boolean removeImageById(String id) {
        Ad ad = baseMapper.selectById(id);

        if (ad != null){
            if (StringUtils.isEmpty(ad.getImageUrl()) == false){
                R r = ossFileService.deleteFile(ad.getImageUrl());
                return r.getSuccess();
            }
        }

        return false;
    }

    @Override
    public List<Ad> listByAdTypeId(String adTypeId) {
        QueryWrapper<Ad> queryWrapper = new QueryWrapper();

        queryWrapper.orderByAsc("a.type_id", "a.sort");
        queryWrapper.eq("a.type_id", adTypeId);

        return baseMapper.selectListByAdTypeId(queryWrapper);
    }
}
