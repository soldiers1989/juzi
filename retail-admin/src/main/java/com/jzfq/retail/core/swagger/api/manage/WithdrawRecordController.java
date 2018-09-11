package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.req.OrdersSettleSearch;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.WithdrawRecordSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrdersSettleRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.WithdrawRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年06月29日 17:26
 * @Description: 商户结算
 */
@Slf4j
@RestController
public class WithdrawRecordController {


    @Autowired
    private WithdrawRecordService withdrawRecordService;


    @ApiOperation(value = "商户结算列表")
    @RequestMapping(value = "/api/withdrawRecode/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                   @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   WithdrawRecordSearchReq search) {
        PageReq<WithdrawRecordSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = withdrawRecordService.getList(pageReq.getPage(), pageReq.getPageSize(), search);
            return TouchApiResponse.success(result);
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("商户结算列表异常：{}", e.getMessage());
            return TouchApiResponse.failed("获取商户结算列表异常");
        }
    }
}
