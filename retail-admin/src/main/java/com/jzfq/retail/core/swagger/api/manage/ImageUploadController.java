package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.OSSService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Title: ImageUploadController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月26日 17:28
 * @Description: 图片上传工具
 */
@Slf4j
@RestController
@RequestMapping("/images")
public class ImageUploadController {

    @Autowired
    private OSSService ossService;

    @ApiOperation("图片上传")
    @PostMapping(value = "/upload")
    public ResponseEntity<TouchResponseModel> upload(MultipartFile file) {
        if (file == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0055);
        }
        try {
            String[] strings = file.getOriginalFilename().split("\\.");
            String suffix = "." + strings[strings.length - 1];
            String url = ossService.uploadImage(file.getBytes(), suffix);
            return TouchApiResponse.success(url.split("\\?")[0],"上传成功");
        } catch (IOException e) {
            return TouchApiResponse.failed(e.getMessage());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
