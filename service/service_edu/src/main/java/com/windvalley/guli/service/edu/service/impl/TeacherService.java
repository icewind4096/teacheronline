package com.windvalley.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.feign.IOssFileService;
import com.windvalley.guli.service.edu.mapper.TeacherMapper;
import com.windvalley.guli.service.edu.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.windvalley.guli.service.edu.entity.vo.TeacherQueryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    @Autowired
    IOssFileService ossFileService;

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

    @Override
    public List<Map<String, Object>> getNameListByKey(String key) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();

        queryWrapper.select("name");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.likeRight("name", key);
        }

        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public Boolean deleteAvatarById(String id) {
        Teacher teacher = baseMapper.selectById(id);

        if (teacher != null){
            if (StringUtils.isEmpty(teacher.getAvatar()) == false){
                R r = ossFileService.deleteFile(teacher.getAvatar());
                return r.getSuccess();
            }
        }

        return false;
    }
}
