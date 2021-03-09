package com.windvalley.guli.service.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.cms.entity.AdType;
import com.windvalley.guli.service.cms.entity.form.AdTypeInfoForm;
import com.windvalley.guli.service.cms.mapper.AdTypeMapper;
import com.windvalley.guli.service.cms.service.IAdTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推荐位 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
@Service
public class AdTypeService extends ServiceImpl<AdTypeMapper, AdType> implements IAdTypeService {

    @Override
    public IPage<AdType> selectPage(Page<AdType> pagePara) {
        return baseMapper.selectPage(pagePara, null);
    }

    @Override
    public String saveAdTypeInfo(AdTypeInfoForm adTypeInfoForm) {
        AdType adType = new AdType();
        BeanUtils.copyProperties(adTypeInfoForm, adType);

        baseMapper.insert(adType);

        return adType.getId();
    }

    @Override
    public boolean removeADTypeById(String id) {
        baseMapper.deleteById(id);

        return true;
    }

    @Override
    public void updateADTypeInfo(AdTypeInfoForm adTypeInfoForm) {
        AdType adType = new AdType();
        BeanUtils.copyProperties(adTypeInfoForm, adType);

        baseMapper.updateById(adType);
    }

    @Override
    public AdTypeInfoForm getInfoById(String id) {
        AdType adType = baseMapper.selectById(id);

        AdTypeInfoForm adTypeInfoForm = new AdTypeInfoForm();
        BeanUtils.copyProperties(adType, adTypeInfoForm);

        return adTypeInfoForm;
    }
}
