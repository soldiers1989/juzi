package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: SelectOperationLogReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月07日 18:09
 * @Description:
 */
@Getter
@Setter
@ToString
public class SelectOperationLogReq implements Serializable {

    @NotBlank(message = "macId不可以为空")
    private String macId;

    private String serviceType;
}
