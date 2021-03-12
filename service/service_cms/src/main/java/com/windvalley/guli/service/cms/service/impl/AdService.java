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
import org.springframework.cache.annotation.Cacheable;
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

    /**
     * @Cacheable(value = "index", key = "'listByAdTypeId'") 的意思就是，如果Redis缓存中的index组下面含有一个key等于listByAdTypeId的数据，就直接取出来
     * 如果没有就去数据库里面把数据取出来，同时放到index组下面，key值叫listByAdTypeId
     * key的值，必须保证在系统里为一个唯一值，要不会在redis里面被覆盖
     * @param adTypeId
     * @return
     */
    @Cacheable(value = "index", key = "'listByAdTypeId'")
    @Override
    public List<Ad> selectByAdTypeId(String adTypeId) {
        QueryWrapper<Ad> queryWrapper = new QueryWrapper();

        queryWrapper.orderByAsc("a.type_id", "a.sort");
        queryWrapper.eq("a.type_id", adTypeId);

        return baseMapper.selectListByAdTypeId(queryWrapper);
    }
}
