package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductBrandSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 19:36
 * @Description: 商品品牌筛选入参
 */
@Setter
@Getter
@ToString
public class SellerSettlePointReq implements Serializable {
    @NotNull(message="ID不能为空", groups = {DictionaryReq.UpdateMethod.class})
    private Integer id;
    @NotNull(message="商户不能为空", groups = {DictionaryReq.CreateMethod.class})
    private Integer sellerId;
    @NotNull(message="扣点不能为空", groups = {DictionaryReq.CreateMethod.class})
    private Double settlePoint;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}
