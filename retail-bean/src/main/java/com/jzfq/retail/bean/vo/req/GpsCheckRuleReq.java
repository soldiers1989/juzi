package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductBrandSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年08月06日 18:54
 * @Description: GPS校验规则入参
 */
@Setter
@Getter
@ToString
public class GpsCheckRuleReq implements Serializable {
    @NotNull(message = "ID不能为空", groups = {ProductBrandReq.UpdateMethod.class})
    private Integer id;
    @NotBlank(message = "环节标识不能为空", groups = { ProductBrandReq.CreateMethod.class})
    private String flag;
    @NotBlank(message = "描述不能为空", groups = { ProductBrandReq.CreateMethod.class})
    private String description;
    @NotNull(message = "是否开启不能为空", groups = { ProductBrandReq.CreateMethod.class})
    private Integer isOpen;
    @NotNull(message = "是否全局校验不能为空", groups = { ProductBrandReq.CreateMethod.class})
    private Integer isGlobal;
    @NotNull(message = "验证公里范围不能为空", groups = { ProductBrandReq.CreateMethod.class})
    private Integer range;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}
