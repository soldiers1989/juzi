package com.jzfq.retail.bean.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class ProductCateResponse implements Serializable {

    private Integer id;
    private Integer productTypeId;
    private Integer pid;
    private String name;
    private String path;
    private BigDecimal scaling;
    private Integer createId;
    private Integer updateId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer sort;
    private String image;
    private Integer visible;
    private Integer type;
    private String createUserName;
}