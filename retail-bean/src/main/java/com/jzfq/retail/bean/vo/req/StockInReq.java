package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class StockInReq implements Serializable {

    private Integer id;

    private String collectCode;

    private Integer purchaseOrdersId;

    private Integer collectStatus;

    private Date collectTimeBegin;

    private Date collectTimeEnd;

    private String collectPerson;

    private String productName;

    private String intoCode;

    private Date intoTime;

    private Date source;

    private String remark;

}