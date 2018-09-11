package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: ProductCateBrandAreasSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 14:51
 * @Description: 分类规则 入参对象
 */
@Setter
@Getter
@ToString
public class ProductCateBrandAreasReq implements Serializable {
    /**
     * 主健
     */
    @NotNull(message = "ID不能为空", groups = {UpdateMethod.class})
    private Integer id;
    @NotNull(message = "分类ID不能为空", groups = {CreateMethod.class})
    private Integer cateId;
    @NotNull(message = "品牌ID不能为空", groups = {CreateMethod.class})
    private Integer brandId;
    @NotBlank(message = "城市ID不能为空", groups = {CreateMethod.class})
    private String areaId;
    @NotNull(message = "区间价最小值不能为空", groups = {CreateMethod.class})
    private BigDecimal intervalPriceMin;
    @NotNull(message = "区间价最大值不能为空", groups = {CreateMethod.class})
    private BigDecimal intervalPriceMax;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}
