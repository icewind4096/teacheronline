package com.windvalley.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseQueryVO implements Serializable {
    private String title;
    private String teacherId;
    private String subjectParentId;
    private String subjectId;
}
