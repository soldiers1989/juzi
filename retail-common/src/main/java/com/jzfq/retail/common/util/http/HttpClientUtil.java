package com.jzfq.retail.common.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/**
 * http请求工具类
 *                       
 * @Filename: HttpClientUtil.java
 * @Version: 1.0
 * @Author: 陈万海
 * @Email: chenwanhai@sina.com
 *
 */
public class HttpClientUtil {

    private static final Logger log                      = LoggerFactory
        .getLogger(HttpClientUtil.class);

    public static final String  CONTENT_APPLICATION_JSON = "application/json; charset=UTF-8";
    public static final String  CONTENT_APPLICATION_FORM = "application/x-www-form-urlencoded; charset=UTF-8";

    public static String sendGet(String url) {
        HttpGet get = null;
        CloseableHttpResponse resp = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            get = new HttpGet(url);
            resp = client.execute(get);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = resp.getEntity();
                String content = EntityUtils.toString(entity, "utf-8");
                return content;
            }
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (resp != null) {
                    resp.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * post请求json
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public static String sendJsonPost(String url, String json) throws Exception {
        return sendPost(url, json, "application/x-www-form-urlencoded");
    }

    /**
     * 
     * @param url
     * @param content
     * @param type
     * @param timeout 毫秒
     * @return
     */
    public static String sendPost(String url, String content, String type, int timeout) {
        CloseableHttpClient client = null;
        CloseableHttpResponse resp = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.addHeader("Content-type", type);
            StringEntity entity = new StringEntity(content, ContentType.create(type, "UTF-8"));
            post.setEntity(entity);
            resp = client.execute(post);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                String str = EntityUtils.toString(resp.getEntity(), "utf-8");
                return str;
            }
        } catch (UnsupportedCharsetException e) {
            log.error(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (resp != null) {
                    resp.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static String sendPost(String url, String content, String type) {
        CloseableHttpClient client = null;
        CloseableHttpResponse resp = null;
        try {
            System.out.println(content);
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-type", type);
            StringEntity entity = new StringEntity(content, ContentType.create(type, "UTF-8"));
            // StringEntity entity = new StringEntity(content);
            post.setEntity(entity);
            resp = client.execute(post);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                String str = EntityUtils.toString(resp.getEntity(), "utf-8");
                return str;
            }
        } catch (UnsupportedCharsetException e) {
            log.error(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (resp != null) {
                    resp.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static String getServiceResponseAsString(String url, Map<String, String> params) {
        return getServiceResponseAsString(url, params, null, 60 * 1000);
    }

    public static String getServiceResponseAsString(String url, Map<String, String> params,
                                                    Map<String, String> heads, Integer timeOut) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uri = new URIBuilder();
            uri.setPath(url);
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uri.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpGet httpGet = new HttpGet(uri.build());
            if (heads != null) {
                for (Map.Entry<String, String> entry : heads.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut).build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    return EntityUtils.toString(entity);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static String sendPostMap(String url, Map<String, Object> paramMap) {
        CloseableHttpClient client = null;
        CloseableHttpResponse resp = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (paramMap != null && paramMap.size() > 0) {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-type", "application/x-www-form-urlencoded");
            post.addHeader("application", "shangcheng");
            StringEntity entity = new StringEntity(
                    stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1),
                    ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
            post.setEntity(entity);
            resp = client.execute(post);
            return EntityUtils.toString(resp.getEntity(), "utf-8");
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (resp != null) {
                    resp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static byte[] sendGetByteArray(String url) {
        HttpGet get = null;
        CloseableHttpResponse resp = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            get = new HttpGet(url);
            resp = client.execute(get);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                byte[] buffer = new byte[1024 * 4];
                InputStream is = resp.getEntity().getContent();
                int len=0;
                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                while((len=is.read(buffer))!=-1){
                    bos.write(buffer,0,len);
                }
                bos.flush();
                return bos.toByteArray();
            }
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (resp != null) {
                    resp.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
