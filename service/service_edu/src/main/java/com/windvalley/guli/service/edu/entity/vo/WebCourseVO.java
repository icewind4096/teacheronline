package com.windvalley.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WebCourseVO implements Serializable {
    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private String price;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "购买次数")
    private Integer buyCount;

    @ApiModelProperty(value = "观看次数")
    private Integer viewCount;

    @ApiModelProperty(value = "课时数")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;

    @ApiModelProperty(value = "讲师简介")
    private String intro;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

    @ApiModelProperty(value = "课程一级分类ID")
    private String subjectLevelOneId;

    @ApiModelProperty(value = "课程一级分类")
    private String subjectLevelOne;

    @ApiModelProperty(value = "课程二级分类ID")
    private String subjectLevelTwoId;

    @ApiModelProperty(value = "课程二级分类")
    private String subjectLevelTwo;
}
