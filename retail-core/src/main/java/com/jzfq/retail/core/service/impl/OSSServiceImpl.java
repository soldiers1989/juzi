package com.jzfq.retail.core.service.impl;

import com.google.zxing.common.detector.MathUtils;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.OSSUnitUtil;
import com.jzfq.retail.common.util.QRCodeUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.OSSService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Title: OSSServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月26日 16:45
 * @Description:
 */
@Slf4j
@Service("ossService")
public class OSSServiceImpl implements OSSService {


    @Value("${alibaba.oss.bucket}")
    private String ossBucket;

    @Autowired
    private OSSUnitUtil ossUnitUtil;

    @Override
    public String uploadImage(byte[] bytes, String suffix) {
        InputStream is = new ByteArrayInputStream(bytes);
        String diskName = "images/upload/";
        return this.upload(is, suffix, diskName);
    }


    @Override
    public String uploadImage(byte[] bytes, String suffix, String diskName) {
        InputStream is = new ByteArrayInputStream(bytes);
        return this.upload(is, suffix, diskName);
    }

    private String upload(InputStream is, String suffix, String diskName) {
        String url = null;
        Integer randomInt = (int) ((Math.random() * 99) + 10);
        String key = "image_" + String.format("%04d", randomInt) + "_" + System.currentTimeMillis() + suffix;
        if (StringUtils.isNotBlank(ossUnitUtil.uploadObject2OSS3(is, ossBucket, key, diskName))) {
            url = ossUnitUtil.getUrl(diskName + key, ossBucket);
        }
        if (StringUtils.isBlank(url)) {
            log.error("oss上传图片失败，返回地址为空");
            throw new RuntimeException("上传图片失败，返回地址为空");
        }
        log.info("oss上传图片地址返回：{}", url);
        url = url.split("\\?")[0];
        return url;
    }
}
