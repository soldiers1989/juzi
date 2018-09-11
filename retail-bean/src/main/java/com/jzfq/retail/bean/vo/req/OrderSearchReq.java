package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: OrderSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月10日 17:01
 * @Description: 订单查询入参
 */
@Getter
@Setter
@ToString
public class OrderSearchReq implements Serializable {

    private Integer[] status;

}
