package com.windvalley.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.mapper.TeacherMapper;
import com.windvalley.guli.service.edu.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.windvalley.guli.service.edu.vo.TeacherQueryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Service
public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Override
    public IPage<Teacher> selectPage(Page<Teacher> pagePara, TeacherQueryVO teacherQueryVO) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort");

        if (teacherQueryVO == null){
            return baseMapper.selectPage(pagePara, queryWrapper);
        }

        if (!StringUtils.isEmpty(teacherQueryVO.getName())){
            queryWrapper.likeRight("name", teacherQueryVO.getName());
        }

        if (teacherQueryVO.getLevel() != null){
            queryWrapper.eq("level", teacherQueryVO.getLevel());
        }

        if (!StringUtils.isEmpty(teacherQueryVO.getJoinDateBegin())){
            queryWrapper.ge("join_date", teacherQueryVO.getJoinDateBegin());
        }

        if (!StringUtils.isEmpty(teacherQueryVO.getJoinDateEnd())){
            queryWrapper.le("join_date", teacherQueryVO.getJoinDateBegin());
        }

        return baseMapper.selectPage(pagePara, queryWrapper);
    }
}
