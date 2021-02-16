package com.windvalley.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.windvalley.guli.service.edu.entity.Subject;
import com.windvalley.guli.service.edu.entity.excel.ExcelSubjectData;
import com.windvalley.guli.service.edu.entity.vo.SubjectVO;
import com.windvalley.guli.service.edu.listener.ExcelSubjectDataListener;
import com.windvalley.guli.service.edu.mapper.SubjectMapper;
import com.windvalley.guli.service.edu.service.ISubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Service
public class SubjectService extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {
    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectDataListener(baseMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet()
                .doRead();
    }

    @Override
    public List<SubjectVO> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }
}
