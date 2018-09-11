package com.jzfq.retail.bean.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class OrderSnCount implements Serializable {

    private Integer id;

    private Integer sellerId;

    private Integer currentVal;
    
    private String dateStr;
}