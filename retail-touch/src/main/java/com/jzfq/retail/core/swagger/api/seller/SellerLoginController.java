package com.jzfq.retail.core.swagger.api.seller;


import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.SellerLoginReq;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerLoginService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @Title: SellerLoginController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 18:25
 * @Description: 小程序商户端登录接口
 */
@Slf4j
@RestController
@RequestMapping("/")
public class SellerLoginController {

    private static final String SELLER_LOGIN = "/sellerLogin"; // 小程序商户端登录

    @Autowired
    private SellerLoginService sellerLoginService;

    @ApiOperation(value = "小程序商户端登录")
    @RequestMapping(value = SELLER_LOGIN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> login(@Valid @RequestBody SellerLoginReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            Map<String, Object> map = sellerLoginService.login(req);
            return TouchApiResponse.success(map, "登录成功");
        } catch (TouchCodeException te) {
            log.error("小程序商户端登录:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("小程序商户端登录:{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


}
