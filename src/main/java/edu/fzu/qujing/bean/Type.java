package edu.fzu.qujing.bean;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(description = "类型")
@AllArgsConstructor
@NoArgsConstructor
public class Type implements Serializable {

    @ApiModelProperty("id")
    public Integer id;

    @ApiModelProperty("类型的名称")
    public String name;

}
