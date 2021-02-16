package com.windvalley.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.windvalley.guli.service.edu.entity.Subject;
import com.windvalley.guli.service.edu.entity.Teacher;
import com.windvalley.guli.service.edu.entity.excel.ExcelSubjectData;
import com.windvalley.guli.service.edu.mapper.SubjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {
    private SubjectMapper subjectMapper;

    public ExcelSubjectDataListener(){

    }

    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        log.info("行数据:{}", excelSubjectData);

        //处理excel行数据, 存入数据库

        String parentId;
        //判断一级类别是否存在
        Subject subjectOne = getByLevelOneTitle(excelSubjectData.getLevelOneTitle());
        if (subjectOne == null){
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(excelSubjectData.getLevelOneTitle());
            subjectMapper.insert(subject);
            parentId = subject.getId();
        } else {
            parentId = subjectOne.getId();
        }

        //判断二级类别是否存在
        if (getByLevelTwoTitle(parentId, excelSubjectData.getLevelTwoTitle()) == null){
            Subject subjectTwo = new Subject();
            subjectTwo.setParentId(parentId);
            subjectTwo.setTitle(excelSubjectData.getLevelTwoTitle());
            subjectMapper.insert(subjectTwo);
        }
    }

    private Subject getByLevelTwoTitle(String parentId, String levelTwoTitle) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.eq("title", levelTwoTitle);
        return subjectMapper.selectOne(queryWrapper);
    }

    private Subject getByLevelOneTitle(String levelOneTitle) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper();
        queryWrapper.eq("title", levelOneTitle);
        queryWrapper.eq("parent_id", "0");
        return subjectMapper.selectOne(queryWrapper);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("全部数据解析完成");
    }
}
