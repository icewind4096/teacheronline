package com.windvalley.guli.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.windvalley.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.entity.vo.SubjectVO;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
public interface SubjectMapper extends BaseMapper<Subject> {
    List<SubjectVO> selectNestedListByParentId(String parentId);
}
