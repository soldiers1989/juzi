package com.jzfq.retail.common.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OSS 工具类
 *
 * @author sjtang
 * @Description: TODO
 * @date:2017年5月18日 下午6:29:58
 */
@Component
@Slf4j
public class OSSUnitUtil {

    //阿里云API的内或外网域名
    @Value("${alibaba.oss.endPoint}")
    private String ENDPOINT;

    //阿里云API的密钥Access Key ID
    @Value("${alibaba.oss.accessKeyId}")
    private String ACCESS_KEY_ID;

    //阿里云API的密钥Access Key Secret
    @Value("${alibaba.oss.accessKeySecret}")
    private String ACCESS_KEY_SECRET;

    private static OSSClient client;

    /**
     * 获取阿里云OSS客户端对象
     */
    public OSSClient getOSSClient() {
        if (client == null) {
            client = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        }
        return client;
    }

    /**
     * 新建Bucket  --Bucket权限:私有
     *
     * @param bucketName bucket名称
     * @return true 新建Bucket成功
     */
    public boolean createBucket(String bucketName) {
        Bucket bucket = getOSSClient().createBucket(bucketName);
        return bucketName.equals(bucket.getName());
    }

    /**
     * 删除Bucket
     *
     * @param bucketName bucket名称
     */
    public void deleteBucket(String bucketName) {
        getOSSClient().deleteBucket(bucketName);
        log.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代
     *
     * @param file       上传文件
     * @param bucketName bucket名称
     * @param diskName   上传文件的目录  --bucket下文件的路径
     * @return String 唯一MD5数字签名
     */
    public String uploadObject2OSSMultipartFile(MultipartFile file, String fileName, String bucketName, String diskName) {
        //OSSClient client=getOSSClient();
        String resultStr = null;
        try {
            InputStream is = file.getInputStream();
            //          String fileName = file.getOriginalFilename(); 
            //          Long fileSize = file.getSize();
            //创建上传Object的Metadata  
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //          metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");  
            //上传文件   
            PutObjectResult putResult = getOSSClient().putObject(bucketName, diskName + fileName, is,
                    metadata);
            //解析结果  
            resultStr = putResult.getETag();
        } catch (Exception e) {
            System.out.println(e);
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代
     *
     * @param file       上传文件
     * @param bucketName bucket名称
     * @param diskName   上传文件的目录  --bucket下文件的路径
     * @return String 唯一MD5数字签名
     */
    public String uploadObject2OSS(File file, String bucketName, String diskName) {
        String resultStr = null;
        try {
            InputStream is = new FileInputStream(file);
            String fileName = file.getName();
            Long fileSize = file.length();
            //创建上传Object的Metadata  
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //          metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");  
            //上传文件   
            PutObjectResult putResult = getOSSClient().putObject(bucketName, diskName + fileName, is,
                    metadata);
            //解析结果  
            resultStr = putResult.getETag();
        } catch (Exception e) {
            System.out.println(e);
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 上传图片
     *
     * @param file
     * @param bucketName
     * @param fileName
     * @param diskName
     * @return
     */
    public String uploadObject2OSS2(File file, String bucketName, String fileName, String diskName) {
        String resultStr = null;
        try {
            InputStream is = new FileInputStream(file);
            //            String fileName = file.getName();
            Long fileSize = file.length();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //          metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件
            PutObjectResult putResult = getOSSClient().putObject(bucketName, diskName + fileName, is,
                    metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            System.out.println(e);
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 上传图片
     *
     * @param is
     * @param bucketName
     * @param fileName
     * @param diskName
     * @return
     */
    public String uploadObject2OSS3(InputStream is, String bucketName, String fileName, String diskName) {
        String resultStr = null;
        try {
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = getOSSClient().putObject(bucketName, diskName + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            System.out.println(e);
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 根据key获取OSS服务器上的文件输入流
     *
     * @param bucketName bucket名称
     * @param diskName   文件路径
     * @param key        Bucket下的文件的路径名+文件名
     */
    public InputStream getOSS2InputStream(String bucketName, String diskName, String key) {
        OSSObject ossObj = getOSSClient().getObject(bucketName, diskName + key);
        return ossObj.getObjectContent();
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param bucketName bucket名称
     * @param diskName   文件路径
     * @param key        Bucket下的文件的路径名+文件名
     */
    public void deleteFile(String bucketName, String diskName, String key) {
        getOSSClient().deleteObject(bucketName, diskName + key);
        log.info("删除" + bucketName + "下的文件" + diskName + key + "成功");
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)
                || "png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if ("html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        return "text/html";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key, String bucketName) {

        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = getOSSClient().generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    public static void main(String[] args) {
        //	OSSClient client = OSSUnitUtil.getOSSClient(); 
        //上传文件  
        String flilePathName = "/Users/sjtang/Desktop/3b376fd0-3856-3a9b-a171-0261b0206859.png";
        File file = new File(flilePathName);
        String diskName = "";
        //    String md5key = OSSUnitUtil.uploadObject2OSS(client, file, "dev-usercenter", diskName); 
        //    System.out.println("=================="+md5key);
    }

    /**
     * 暂时不用的方法
     *
     * @return
     */
    public String getBucketNameByDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        String format2 = format.format(new Date());
        return format2;
    }

    /**
     * 通过时间创建文件夹
     *
     * @return
     */
    public String getFileDirNameByDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        String format2 = format.format(new Date());
        return format2;
    }

    /**
     * 添加获取文件后缀名方法
     *
     * @param fileName
     * @return
     */
    public String getFileLastName(String fileName) {
        String substring = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return substring;
    }

    /**
     * 获取oss endpoint
     *
     * @return
     */
    public String getOssEndPoint() {
        return EnviromentUtil.EJS_END_POINT;
    }

    /**
     * 获取bucket+endpoint拼接成的服务器url
     *
     * @return
     */
    public String getOssUrl() {
        return EnviromentUtil.EJS_OSS_URL;
    }

    /**
     * 获取商城图片上传统一域名
     *
     * @return
     */
    public String getBucketNameJavaShop() {
        return EnviromentUtil.EJS_OSS_BUCKET;
    }
}