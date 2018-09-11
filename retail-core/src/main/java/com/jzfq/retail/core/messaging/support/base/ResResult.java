package com.jzfq.retail.core.messaging.support.base;

import lombok.*;

/**
 * @author lagon
 * @time 2017/7/7 12:53
 * @description 响应结果实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResResult {

    private String code;//响应标识
    private String remark;//响应描述

}
