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
 * @Date 2018年08月23日 18:21
 * @Description: 通过normName查询SKU接口的入参
 */
@Setter
@Getter
@ToString
public class GetSkuByNormNameSearchReq implements Serializable {
   @NotNull(message="产品ID不能为空")
   private Integer productId;
   @NotBlank(message="编码不能为空")
   private String normName;
}
