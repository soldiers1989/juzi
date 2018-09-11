package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class TakeCodeReq implements Serializable {

    @NotBlank(message = "takeCode不能为空")
    private String takeCode;

    @NotNull(message = "sellerId不能为空")
    private Integer sellerId;
}