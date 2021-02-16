package com.windvalley.guli.service.edu.service;

import com.windvalley.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.edu.entity.vo.SubjectVO;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
public interface ISubjectService extends IService<Subject> {
    void batchImport(InputStream inputStream);

    List<SubjectVO> nestedList();
}
