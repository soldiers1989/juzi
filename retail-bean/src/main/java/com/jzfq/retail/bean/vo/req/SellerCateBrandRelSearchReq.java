package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SellerCateBrandRelSearchReq implements Serializable {
    private String sellerTitle;
    private Integer sellerStyle;
    private String cateName;
    private String brandName;
}
