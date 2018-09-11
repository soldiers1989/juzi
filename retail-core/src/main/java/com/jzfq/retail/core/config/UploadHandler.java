package com.jzfq.retail.core.config;

import com.google.common.collect.Sets;
import com.jzfq.retail.common.exception.BadRequestException;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Set;


/**
 * Description: 后台用户查询参数
 *
 * @author liuxueliang
 * @version V1.0
 * 2018年7月10日上午
 */
@Component
@Slf4j
public class UploadHandler {

    @Value("${uploadHandler.suffixes}")
    public String SUFFIXES;

    @Value("${uploadHandler.thumbnailWidth}")
    protected String THUMBNAIL_WIDTH;

    @Value("${uploadHandler.thumbnailHeight}")
    protected String THUMBNAIL_HEIGHT;

    @Value("${uploadHandler.thumbnailPath}")
    protected String THUMBNAIL_PATH;

    @Value("${uploadHandler.filePath}")
    protected String FILE_PATH;

    /**
     * 验证
     *
     * @param multipartFile
     * @return
     */
    private void validate(MultipartFile multipartFile) {
        // 验证文件类型
        if (!validSuffix(getSuffix(multipartFile))) {
            throw new BadRequestException("只能上传" + SUFFIXES + "类型的文件");
        }
    }

    /**
     * 如果包含,或者返回为空 则返回true，如果不包含 则返回false
     *
     * @param suffix
     * @return
     */
    private boolean validSuffix(String suffix) {
        if (StringUtils.isNotBlank(SUFFIXES)) {
            Set<String> suffixes = Sets.newHashSet(SUFFIXES.toUpperCase().split(","));
            return suffixes.contains(suffix.toUpperCase());
        }
        return true;
    }

    /**
     * 校验指定文件尾缀
     *
     * @param suffix
     * @param uploadSuffixes
     * @return
     */
    private boolean validSuffix(String suffix, String uploadSuffixes) {
        if (StringUtils.isBlank(uploadSuffixes)) {
            return validSuffix(suffix.toUpperCase());
        }
        Set<String> suffixes = Sets.newHashSet(uploadSuffixes.toUpperCase().split(","));
        return suffixes.contains(suffix.toUpperCase());
    }

    /**
     * 获取文件Size
     *
     * @param multipartFile
     * @return
     */
    public long getFileSize(MultipartFile multipartFile) {
        Assert.notNull(multipartFile);
        return multipartFile.getSize();
    }

    /**
     * 获取文件oldName
     *
     * @param multipartFile
     * @return
     */
    public String getOldName(MultipartFile multipartFile) {
        Assert.notNull(multipartFile);
        return multipartFile.getOriginalFilename();
    }

    /**
     * 获取文件尾缀
     *
     * @param multipartFile
     * @return
     */
    public String getSuffix(MultipartFile multipartFile) {
        String fullName = multipartFile.getOriginalFilename();
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }

    /**
     * 获取文件MD5加密串
     *
     * @param multipartFile
     * @return
     */
    public String getMd5(MultipartFile multipartFile) {
        String md5 = null;
        InputStream is = null;
        try {
            is = multipartFile.getInputStream();
            md5 = DigestUtils.md5Hex(IOUtils.toByteArray(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(is);
        return md5;
    }


    /**
     * 删除文件
     *
     * @return
     * @throws Exception
     */
    public boolean deleteFile(String fullName) {
        File file = new File(fullName);
        boolean flag = false;
        if (file.isFile()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 图片制作缩略图
     *
     * @param file
     * @return
     */
    public File createThumbnail(MultipartFile file) {
        String suffix = getSuffix(file);
        if (StringUtils.isBlank(suffix)) {
            throw new BadRequestException("上传文件异常");
        }
        if (validSuffix(suffix, SUFFIXES)) {
            return createThumbnailImage(file);
        }
        throw new BadRequestException("制作缩略图异常");
    }

    /**
     * 图片制作缩略图
     *
     * @param file
     * @return
     */
    public File createThumbnailImage(MultipartFile file) {
        String fileMd5 = getMd5(file);
        String fileSuffix = getSuffix(file);
        String dirPath = FILE_PATH + THUMBNAIL_PATH;
        hasDirectory(dirPath);
        File thumbnailFile = new File(dirPath + fileMd5 + "." + fileSuffix);
        try {
            Thumbnails.of(file.getInputStream()).size(Integer.parseInt(THUMBNAIL_WIDTH), Integer.parseInt(THUMBNAIL_HEIGHT)).toFile(thumbnailFile);
            return thumbnailFile;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("制作缩略图异常:{}", e.getMessage());
            throw new BusinessException("制作缩略图异常");
        }
    }

    /**
     * 图片制作缩略图
     *
     * @param file
     * @param fileSuffix
     * @return
     */
    public File createThumbnailImage(byte[] file, String fileSuffix) {
        String fileMd5 = MD5Util.getMD5String(file);
        String dirPath = FILE_PATH + THUMBNAIL_PATH;
        hasDirectory(dirPath);
        File thumbnailFile = new File(dirPath + fileMd5 + "." + fileSuffix);
        try {
            Thumbnails.of(new ByteArrayInputStream(file)).size(Integer.parseInt(THUMBNAIL_WIDTH), Integer.parseInt(THUMBNAIL_HEIGHT)).toFile(thumbnailFile);
            return thumbnailFile;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("制作缩略图异常:{}", e.getMessage());
            throw new BusinessException("制作缩略图异常");
        }
    }

    /**
     * 缩略图MD5
     *
     * @param thumbnailFile
     * @return
     */
    public String getThumbnailMd5(File thumbnailFile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(thumbnailFile);
            return DigestUtils.md5Hex(IOUtils.toByteArray(fileInputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();log.error("缩略图文件名异常: {}", e);
            throw new BusinessException("缩略图文件名异常");
        } catch (IOException e) {
            e.printStackTrace();log.error("缩略图文件名异常: {}", e);
            throw new BusinessException("缩略图文件名异常");
        }
    }

    /**
     * 缩略图后缀
     *
     * @param thumbnailFile
     * @return
     */
    public String getThumbnailSuffix(File thumbnailFile) {
        if (StringUtils.isBlank(thumbnailFile.getName()) || thumbnailFile.getName().split("[.]").length != 2) {
            throw new BusinessException("缩略图文件名异常");
        }
        return thumbnailFile.getName().split("[.]")[1].toLowerCase();
    }

    /**
     * 目录不存在则创建
     *
     * @param path
     */
    public static boolean hasDirectory(String path) {
        File dir = new File(path);
        return dir.mkdirs();
    }
}
