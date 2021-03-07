package com.windvalley.guli.service.edu.entity.vo;

import lombok.Data;

@Data
public class WebCourseQueryVO {
    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;
    private String orderBy;
}
