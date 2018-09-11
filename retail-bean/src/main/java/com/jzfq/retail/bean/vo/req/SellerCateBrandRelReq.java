package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SellerCateBrandRelReq implements Serializable {
    @NotNull(message = "ID不能为空", groups = {SellerCateBrandRelReq.UpdateMethod.class})
    private Integer id;
    @NotNull(message = "店铺名称不能为空", groups = {SellerCateBrandRelReq.UpdateMethod.class, SellerCateBrandRelReq.CreateMethod.class})
    private Integer sellerId;
    @NotNull(message = "品牌不能为空", groups = {SellerCateBrandRelReq.UpdateMethod.class, SellerCateBrandRelReq.CreateMethod.class})
    private Integer brandId;
    @NotNull(message = "品类不能为空", groups = {SellerCateBrandRelReq.UpdateMethod.class, SellerCateBrandRelReq.CreateMethod.class})
    private Integer cateId;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}
