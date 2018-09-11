package com.jzfq.retail.bean.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
public class SellerTermEx extends SellerTerm {
    /**
     * 是否免息
     */
    private Boolean isInterestFree;

    /**
     * 分期试算
     */
    private Object ob;
}