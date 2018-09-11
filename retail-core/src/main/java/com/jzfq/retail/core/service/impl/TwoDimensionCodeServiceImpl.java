package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.Product;
import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.bean.domain.ProductCateQuery;
import com.jzfq.retail.bean.domain.ProductQuery;
import com.jzfq.retail.bean.vo.res.TwoDimensionRes;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.OSSUnitUtil;
import com.jzfq.retail.common.util.QRCodeUtil;
import com.jzfq.retail.common.util.UUIDGenerator;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.TwoDimensionCodeService;
import com.jzfq.retail.core.call.domain.RiskReceive;
import com.jzfq.retail.core.dao.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月07日 16:29
 * @Description: 订单影像信息接口实现
 */
@Slf4j
@Service
public class TwoDimensionCodeServiceImpl implements TwoDimensionCodeService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OSSUnitUtil ossUnitUtil;

    @Value("${qrcode.logo}")
    private String logoPath;

    @Value("${alibaba.oss.bucket}")
    private String ossBucket;

    @Value("${two.dimension.code.content}")
    private String twoDimCon;


    @Override
    public List<TwoDimensionRes> getExportData() {
        // 返回数据
        List<TwoDimensionRes> productMaps = new ArrayList<TwoDimensionRes>();

        ProductQuery productQuery = new ProductQuery();
        productQuery.or().andIdentificationEqualTo(1);//二期店中商品
        int count = productMapper.countByExample(productQuery);
        if(count == 0) {
            throw new BusinessException("没有商品记录！");
        }
        if(count > 3000) {
            throw new BusinessException(MessageFormat.format("数据量过大，禁止导出，当前数据量：{0}",count));
        }
        List<Product> products = productMapper.selectByExample(productQuery);
        for(Product product : products) {
            TwoDimensionRes res = new TwoDimensionRes();
            // 生成二维码，上传订单二维码
            //如果为空或失效则新增一条二维码
            String content = twoDimCon;
            content = content.replace("SELLERID",product.getSellerId() + "").replace("PRODUCTID",product.getId() + "");

            String diskName = "images/QRCode/";
            String url = null;
            String key = UUIDGenerator.getUUID() + ".jpg";
            try {
                if (StringUtils.isNotBlank(ossUnitUtil.uploadObject2OSS3(QRCodeUtil.encodeFile(content, logoPath, true), ossBucket, key, diskName))) {
                    url = ossUnitUtil.getUrl(diskName + key, ossBucket);
                }
            } catch (Exception e) {
                log.error("生成二维码或OSS服务异常，异常描述：{}",e);
                throw new BusinessException(e.getMessage());
            }
            if (StringUtils.isBlank(url)) {
                log.error("oss上传二维码图片失败，返回地址未开为空");
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0031);
            }
            log.info("oss上传二维码图片地址返回：{}", url);
            //新增二维码对象
            url = url.split("\\?")[0];

            res.setProductName(product.getName1());
            res.setProductDesc(product.getKeyword());
            res.setUrl(url);
            productMaps.add(res);
        }

        return productMaps;
    }
}
