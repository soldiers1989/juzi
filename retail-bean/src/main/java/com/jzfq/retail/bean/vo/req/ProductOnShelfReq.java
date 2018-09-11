package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author MaoLixia
 * @Date 2018/8/28 14:41
 * 商品图片
 */
@Setter
@Getter
@ToString
public class ProductOnShelfReq implements Serializable{
    @NotNull(message = "sellerId can not be null")
    private Integer sellerId;

    @NotNull(message = "productId can not be null")
    private Integer productId;

    @NotBlank(message = "proLable can not be blank")
    private String proLabel;

    @NotBlank(message = "attribute can not be blank")
    private String attrParams;

    @NotEmpty(message = "imageList can not be empty")
    private List<ProductImageList> imageList;

    @NotBlank(message = "upTime can not be blank")
    private String upTime;

    @NotBlank(message = "offTime can not be blank")
    private String offTime;

    @NotNull(message = "operationType can not be null")
    private Integer operationType;

    private String remark;

    @Setter
    @Getter
    @ToString
    public class ProductImageList implements Serializable{
        private String url;
        private String remark;
        private Integer sort;
    }

}


