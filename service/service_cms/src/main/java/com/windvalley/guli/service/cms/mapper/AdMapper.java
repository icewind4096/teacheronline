package com.windvalley.guli.service.cms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.windvalley.guli.service.cms.entity.VO.AdVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 广告推荐 Mapper 接口
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
public interface AdMapper extends BaseMapper<Ad> {
    IPage<AdVO> selectPageByWrapper(Page<AdVO> pagePara, @Param(Constants.WRAPPER) QueryWrapper<AdVO> queryWrapper);

    List<Ad> selectListByAdTypeId(@Param(Constants.WRAPPER) QueryWrapper<Ad> queryWrapper);
}
