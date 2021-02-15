package com.windvalley.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.edu.vo.TeacherQueryVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
public interface ITeacherService extends IService<Teacher> {
    IPage<Teacher> selectPage(Page<Teacher> pagePara, TeacherQueryVO teacherQueryVO);

    List<Map<String, Object>> getNameListByKey(String key);

    Boolean deleteAvatarById(String id);
}
