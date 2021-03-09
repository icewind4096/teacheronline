package com.windvalley.guli.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.cms.entity.AdType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.cms.entity.form.AdTypeInfoForm;

/**
 * <p>
 * 推荐位 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
public interface IAdTypeService extends IService<AdType> {
    IPage<AdType> selectPage(Page<AdType> pagePara);

    String saveAdTypeInfo(AdTypeInfoForm adTypeInfoForm);

    boolean removeADTypeById(String id);

    void updateADTypeInfo(AdTypeInfoForm adTypeInfoForm);

    AdTypeInfoForm getInfoById(String id);
}
