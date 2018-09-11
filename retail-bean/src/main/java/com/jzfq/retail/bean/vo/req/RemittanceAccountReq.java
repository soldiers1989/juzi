package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: RemittanceAccountReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年07月18日 14:51
 * @Description: 分类规则 入参对象
 */
@Setter
@Getter
@ToString
public class RemittanceAccountReq implements Serializable {

    @ApiModelProperty(value = "帐户ID")
    @NotNull(message = "ID不能为空", groups = {UpdateMethod.class})
    private Integer id;

    @ApiModelProperty(value = "打款账户")
    @NotNull(message = "打款账户不能为空", groups = {UpdateMethod.class, CreateMethod.class})
    private String accountName;

    @ApiModelProperty(value = "账户编号")
    @NotNull(message = "账户编号不能为空", groups = {UpdateMethod.class, CreateMethod.class})
    private String remark1;

    private String remark2;

    private String remark3;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}
