package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class SellerSingleCreditReq implements Serializable {
    private Integer id;

    @NotNull(message = "商户ID不能为空", groups = {UpdateMethod.class,  CreateMethod.class})
    private Integer sellerId;

    @NotNull(message = "是否开启不能为空", groups = {UpdateMethod.class,  CreateMethod.class})
    private Integer isOpen;


    private Long creditLimit;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}