package com.jzfq.retail.common.util;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EnviromentUtil {

    protected static Logger log = org.slf4j.LoggerFactory.getLogger(EnviromentUtil.class);

    public static String    EJS_SOLR_URL;                                                 //solrURL
    public static String    EJS_SOLR_SERVER;                                              //solr 库

    public static String    EJS_END_POINT;
    public static String    EJS_OSS_URL;
    public static String    EJS_OSS_BUCKET;

    public static String    FMS_URL;
    public static String    SHARE_URL;
    public static String    ORDER_STATUS_URL;
    public static String    H5_URL;
    public static String    XCX_URL;
    public static String    ENV_TYPE;

    public static String getXCX_URL() {
        return XCX_URL;
    }

    public static void setXCX_URL(String xCX_URL) {
        XCX_URL = xCX_URL;
    }

    public static String getH5_URL() {
        return H5_URL;
    }

    public static void setH5_URL(String h5_URL) {
        H5_URL = h5_URL;
    }

    public static String getENV_TYPE() {
        return ENV_TYPE;
    }

    public static void setENV_TYPE(String eNV_TYPE) {
        ENV_TYPE = eNV_TYPE;
    }

    public static void setFMS_URL(String fMS_URL) {
        FMS_URL = fMS_URL;
    }

    public static void setSHARE_URL(String sHARE_URL) {
        SHARE_URL = sHARE_URL;
    }

    public static void setEJS_OSS_URL(String eJS_OSS_URL) {
        EJS_OSS_URL = eJS_OSS_URL;
    }

    public static void setEJS_OSS_BUCKET(String eJS_OSS_BUCKET) {
        EJS_OSS_BUCKET = eJS_OSS_BUCKET;
    }

    public static void setEJS_END_POINT(String eJS_END_POINT) {
        EJS_END_POINT = eJS_END_POINT;
    }

    public static String getEJS_SOLR_URL() {
        return EJS_SOLR_URL;
    }

    public static void setEJS_SOLR_URL(String eJS_SOLR_URL) {
        EJS_SOLR_URL = eJS_SOLR_URL;
    }

    public static String getEJS_SOLR_SERVER() {
        return EJS_SOLR_SERVER;
    }

    public static void setEJS_SOLR_SERVER(String eJS_SOLR_SERVER) {
        EJS_SOLR_SERVER = eJS_SOLR_SERVER;
    }

    public static String getORDER_STATUS_URL() {
        return ORDER_STATUS_URL;
    }

    public static void setORDER_STATUS_URL(String oRDER_STATUS_URL) {
        ORDER_STATUS_URL = oRDER_STATUS_URL;
    }


}
