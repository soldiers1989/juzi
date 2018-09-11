package com.jzfq.retail.core.swagger.api.seller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.HttpUtil;
import com.google.common.collect.Lists;
import com.jzfq.retail.bean.domain.OrderImage;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.common.util.OSSUnitUtil;
import com.jzfq.retail.common.util.SymmetricEncoder;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.GoodsStockService;
import com.jzfq.retail.core.api.service.OSSService;
import com.jzfq.retail.core.api.service.OrderImageService;
import com.jzfq.retail.core.api.service.SellerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: ImageDemoController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月06日 10:10
 * @Description: 测试图片加解密
 */
@Slf4j
@RestController
@RequestMapping("/imageProcess")
public class ImageProcessController {

    @Autowired
    private OrderImageService orderImageService;

    @Autowired
    private OSSService ossService;

    @Autowired
    private SellerService sellerService;

    @ApiOperation(value = "图片解密")
    @RequestMapping(value = "/decrypt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public void pickUpGoods(@ApiParam(value = "订单编号") @RequestParam(value = "orderSn", required = true) String orderSn, HttpServletResponse response) {
        log.info("图片加密上传请求处理开始");
        try {
            // 根据订单编号查询图片URL
            OrderImage orderImage = orderImageService.getOrderImages(orderSn);
            byte[] enData = HttpClientUtil.sendGetByteArray(orderImage.getUrl());
            byte[] dnData = orderImageService.decryptImage(enData);
            response.getOutputStream().write(dnData);
        } catch (TouchCodeException te) {
            log.error("图片解密异常，异常描述：{}", te);
        } catch (Exception e) {
            log.error("图片解密异常，异常描述：{}", e);
        }
    }


    @ApiOperation(value = "上传单个图片")
    @RequestMapping(value = "/wx/upload/image", method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> wxUploadImage(MultipartFile file) {
        if (file == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0055);
        }
        try {
            String[] strings = file.getOriginalFilename().split("\\.");
            String suffix = "." + strings[strings.length - 1];
            String url = ossService.uploadImage(file.getBytes(), suffix, "images/wx/");
            return TouchApiResponse.success(url, "上传成功");
        } catch (IOException e) {
            return TouchApiResponse.failed(e.getMessage());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "批量上传图片")
    @RequestMapping(value = "/wx/upload/images", method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> wxUploadImages(List<MultipartFile> files) {
        if (files == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0055);
        }
        try {
            List<String> urls = Lists.newArrayList();
            for (MultipartFile file : files) {
                String[] strings = file.getOriginalFilename().split("\\.");
                String suffix = "." + strings[strings.length - 1];
                String url = ossService.uploadImage(file.getBytes(), suffix, "images/wx/");
                urls.add(url);
            }
            return TouchApiResponse.success(urls, "上传成功");
        } catch (IOException e) {
            return TouchApiResponse.failed(e.getMessage());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
