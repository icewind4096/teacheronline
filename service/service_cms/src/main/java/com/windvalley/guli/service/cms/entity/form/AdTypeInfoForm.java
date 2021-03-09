package com.windvalley.guli.service.cms.entity.form;

import com.baomidou.mybatisplus.annotation.TableName;
import com.windvalley.guli.service.base.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 推荐位
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-07
 */
@Data
@ApiModel(value="AdTypeForm对象", description="推荐位")
public class AdTypeInfoForm extends BaseEntity {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "标题")
    private String title;
}
