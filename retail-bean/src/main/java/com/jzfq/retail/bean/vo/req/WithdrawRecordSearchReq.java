package com.jzfq.retail.bean.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class WithdrawRecordSearchReq implements Serializable {
    private String sellerCode;
    private String sellerName;
    private String withdrawId;
    private String createTimeBegin;
    private String createTimeEnd;
    private String finishTimeBegin;
    private String finishTimeEnd;
    private Integer state;
    private String createUserName;
}
