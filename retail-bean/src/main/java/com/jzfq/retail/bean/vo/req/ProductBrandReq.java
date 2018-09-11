package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * @Title: ProductBrandSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 19:36
 * @Description: 商品品牌入参
 */
@Setter
@Getter
@ToString
public class ProductBrandReq implements Serializable {
    /**
     * 编号
     */
    @NotNull(message = "ID不能为空", groups = {ProductBrandReq.UpdateMethod.class})
    private Integer id;
    /**
     * 品牌名称
     */
    @NotBlank(message = "品牌名称不能为空", groups = { ProductBrandReq.CreateMethod.class})
    private String name;
    /**
     * 品牌首字母
     */
//    @Max(value = 2, message = "品牌首字母只能输入1位字符", groups = { ProductBrandReq.CreateMethod.class, ProductBrandReq.UpdateMethod.class})
    private String nameFirst;

    /**
     * 品牌英文名称
     */
    private String englishName;
    /**
     * 分类ID
     */
    @NotNull(message = "商品分类ID不能为空", groups = {ProductBrandReq.CreateMethod.class})
    private Integer productCateId;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}
