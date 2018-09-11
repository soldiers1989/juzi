package com.jzfq.retail.bean.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: DictionarySearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年07月15日 12:18
 * @Description: 字典管理筛选 入参
 */
@Setter
@Getter
@ToString
public class DictionarySearchReq implements Serializable {
    @ApiModelProperty(value = "编码")
    private String dictCode;
    @ApiModelProperty(value = "类型")
    private String dictType;
    @ApiModelProperty(value = "名称")
    private String dictName;
    @ApiModelProperty(value = "创建人")
    private String createUser;
}