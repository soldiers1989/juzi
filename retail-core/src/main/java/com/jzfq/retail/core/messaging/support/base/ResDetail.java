package com.jzfq.retail.core.messaging.support.base;

import lombok.*;

/**
 * @author lagon
 * @time 2017/1/14 15:00
 * @description 响应实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResDetail {
    private boolean isSuccess;//是否成功
    private String remark;//响应描述
}
