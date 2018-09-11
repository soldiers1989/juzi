package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class OptionsSearchReq implements Serializable {
    private Integer id;
    private String name;
}
