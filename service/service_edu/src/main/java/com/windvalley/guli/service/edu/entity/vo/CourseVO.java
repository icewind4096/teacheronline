package com.windvalley.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseVO implements Serializable {
    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;

    @ApiModelProperty(value = "课程专业名称")
    private String subjectTitle;

    @ApiModelProperty(value = "课程专业父级名称")
    private String subjectParentTitle;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private String price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "购买次数")
    private Integer buyCount;

    @ApiModelProperty(value = "观看次数")
    private Integer viewCount;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "建立时间")
    private String gmtCreate;
}
