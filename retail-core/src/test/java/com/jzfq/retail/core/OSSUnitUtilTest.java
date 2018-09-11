package com.jzfq.retail.core;

import com.aliyun.oss.common.utils.IOUtils;
import com.jzfq.retail.common.util.CommonUtil;
import com.jzfq.retail.common.util.FileUtil;
import com.jzfq.retail.common.util.OSSUnitUtil;
import com.jzfq.retail.common.util.SymmetricEncoder;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.domain.OrderCheck;
import com.jzfq.retail.core.call.domain.RiskReceive;
import com.jzfq.retail.core.call.service.RiskForeignService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: RiskForeignServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 21:03
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class OSSUnitUtilTest {

    @Autowired
    private OSSUnitUtil oSSUnitUtil;

    @Test
    public void upFile() throws Exception{
        byte[] con = IOUtils.readStreamAsByteArray(new FileInputStream("C:\\Users\\juzi\\Desktop\\微信截图_20180803180602.png"));
        // 获取加密字节数组
        byte[] encodeByte = SymmetricEncoder.AESEncode("AAAA",con);
        InputStream is = new ByteArrayInputStream(encodeByte);
        Integer randomInt = (int) ((Math.random() * 99) + 10);
        String key = "image_" + String.format("%04d", randomInt) + "_" + System.currentTimeMillis() + ".png";
        String diskName = "images/upload/";
        String ossBucket = "juzifenqi-xinlingshou-test";
        oSSUnitUtil.uploadObject2OSS3(is, ossBucket, key, diskName);
        String url = oSSUnitUtil.getUrl(diskName + key, ossBucket);
        System.out.println(url);
    }
}
