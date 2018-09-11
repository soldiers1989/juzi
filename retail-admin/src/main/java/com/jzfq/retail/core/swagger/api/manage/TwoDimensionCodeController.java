package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.res.TwoDimensionRes;
import com.jzfq.retail.common.util.ExcelUtil;
import com.jzfq.retail.core.api.service.TwoDimensionCodeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/foreign/twoDimensionCode")
public class TwoDimensionCodeController {

    @Autowired
    private TwoDimensionCodeService twoDimensionCodeService;

    @ApiOperation(value = "生成商品二维码并导出商品二维码URL")
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(HttpServletResponse response) {
        List<TwoDimensionRes> list = twoDimensionCodeService.getExportData();
        ExcelUtil.export("商品二维码",new String[]{"商品名称","商品描述","商品二维码地址"},list,response);
    }
}
