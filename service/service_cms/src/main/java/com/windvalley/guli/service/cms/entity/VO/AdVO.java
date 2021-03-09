package com.windvalley.guli.service.cms.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="Ad对象", description="广告推荐")
public class AdVO {
    @ApiModelProperty(value = "推荐ID")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "广告类别")
    private String type;
/*
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "背景颜色")
    private String color;

    @ApiModelProperty(value = "链接地址")
    private String linkUrl;
 */
}
