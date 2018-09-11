package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotIn;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductCateReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 18:03
 * @Description: 商品分类 入参
 */
@Setter
@Getter
@ToString
public class ProductCateReq implements Serializable {
    /**
     * 编号
     */
    @NotNull(message = "ID不能为空", groups = {ProductCateReq.UpdateMethod.class})
    private Integer id;
    /**
     * 名称
     */
    @NotBlank(message = "分类名称不能为空", groups = {ProductCateReq.UpdateMethod.class, ProductCateReq.CreateMethod.class})
    private String name;
    /**
     * 是否显示 1 显示 2不显示
     */
    @NotNull(message = "是否显示不能为空", groups = {ProductCateReq.UpdateMethod.class, ProductCateReq.CreateMethod.class})
    @NotIn(params = "1,2", message = "是否显示参数有误，正确参数：1显示 2不显示", groups = {ProductCateReq.UpdateMethod.class, ProductCateReq.CreateMethod.class})
    private Integer visible;

    public interface CreateMethod{}
    public interface UpdateMethod{}

}
