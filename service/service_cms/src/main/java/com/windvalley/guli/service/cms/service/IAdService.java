package com.windvalley.guli.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.cms.entity.VO.AdVO;
import com.windvalley.guli.service.cms.entity.form.AdInfoForm;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
public interface IAdService extends IService<Ad> {
    String saveAdInfo(AdInfoForm adInfoForm);

    void updateAdInfo(AdInfoForm adInfoForm);

    AdInfoForm getInfoById(String id);

    IPage<AdVO> selectPage(Page<AdVO> pagePara);

    boolean removeADById(String id);

    boolean removeImageById(String id);

    List<Ad> selectByAdTypeId(String adTypeId);
}
