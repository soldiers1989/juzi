package com.jzfq.retail.core.api.service;

import java.io.InputStream;

/**
 * @Title: OSSService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月26日 15:56
 * @Description: 阿里云上传接口
 */
public interface OSSService {


    /**
     * 上传图片
     *
     * @return
     */
    String uploadImage(byte[] bytes, String suffix);

    /**
     * 上传图片
     * @param bytes
     * @param suffix
     * @param diskName
     * @return
     */
    String uploadImage(byte[] bytes,String suffix,String diskName);
}
