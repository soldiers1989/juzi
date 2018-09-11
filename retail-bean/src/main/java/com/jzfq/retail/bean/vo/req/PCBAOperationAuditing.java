package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: PCBAOperationAuditing
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 16:18
 * @Description: 分类规则审核 入参
 */
@Setter
@Getter
@ToString
public class PCBAOperationAuditing implements Serializable {
    /**
     * 编号
     */
    @NotNull(message = "ID不能为空")
    private Integer id;
    /**
     * opt参数->success：审核通过，fail：审核失败
     */
    @NotBlank(message = "opt参数不能为空")
    private String opt;

}
