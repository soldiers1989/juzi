package com.jzfq.retail.common.util.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liu wei
 * @time 2018-7-3 下午14:33:26
 * @description 基于Apache HttpClient封装的工具类
 */
@Slf4j
@Component
public class RetryHttpUtil {

    public static final String REQUEST_DATA_FORMAT_FORM="form";//表单类型
    public static final String REQUEST_DATA_FORMAT_JSON="json";//JSON类型
    public static final String REQUEST_DATA_FORMAT_XML="xml";//XML类型
    private static final int TIMEOUT = 100000; //超时时间
    private static PoolingHttpClientConnectionManager connectionPool;
    private static RequestConfig requestConfig;

    static {
        // 设置连接池
        connectionPool = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connectionPool.setMaxTotal(50);
        connectionPool.setDefaultMaxPerRoute(connectionPool.getMaxTotal());
        // 在提交请求之前 测试连接是否可用
        connectionPool.setValidateAfterInactivity(1000);

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(TIMEOUT);
        requestConfig = configBuilder.build();
    }

    /**
     * 异常重试五次
     * @return
     */
    private static HttpRequestRetryHandler retryHandler(){
        return (exception, executionCount, context) -> {

            log.info("try request: {}",executionCount);
            if (executionCount >= 5) {
                // Do not retry if over max retry count
                log.info("5次失败后需要入库，定时任务执行。入库在业务代码中");
                return false;
            }
//            if (exception instanceof InterruptedIOException) {
//                // IO operation problem
//                return false;
//            }
//            if (exception instanceof UnknownHostException) {
//                // Unknown host
//                return false;
//            }
//            if (exception instanceof SSLException) {
//                // SSL handshake exception
//                return false;
//            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                return true;
            }
            return true;
        };
    }

    /**
     * 不携带参数发送HTTP GET请求
     * @param url 请求url
     * @return
     */
    public static String get(String url) throws Exception {
        return get(url, null,null,false);
    }

    /**
     * 携带K-V形式参数发送HTTP GET请求
     * @param url 请求url
     * @param params 请求参数
     * @return
     */
    public static String get(String url, Map<String, String> params) throws Exception {
        return get(url, params, null,false);
    }


    /**
     * 不携带参数发送HTTPS GET请求
     * @param url 请求url
     * @return
     */
    public static String getBySSL(String url) throws Exception {
        return get(url, null,null,true);
    }

    /**
     * 携带K-V形式参数发送HTTPS GET请求
     * @param url 请求url
     * @param params 请求参数
     * @return
     */
    public static String getBySSL(String url, Map<String, String> params) throws Exception {
        return get(url, params, null,true);
    }

    /**
     * 发送K-V形式参数的GET请求
     * @param url 请求url
     * @param params 请求参数
     * @param headers 请求消息头
     * @param isSSL 是否启用SSL
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers,boolean isSSL) throws Exception {
        StringBuffer param = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
                i++;
            }
            url += param;
        }
        String result = null;
        CloseableHttpClient httpClient=null;
        if(isSSL){
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setSSLSocketFactory(createSSLConnSocketFactory()).build();
        }else{
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setConnectionManager(connectionPool).build();
        }
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            log.info("url==" + url);
            httpGet.setConfig(requestConfig);
            //设置消息头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (statusCode != 200) {
                log.error("HTTP错误响应状态码 : " + statusCode);
                log.error("HTTP错误响应描述：" + reasonPhrase);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.error("系统异常:{}", e);
                }
            }
        }
        return result;
    }

    /**
     * 不携带参数发送HTTP POST请求
     * @param url 请求url
     * @return
     */
    public static String post(String url) throws Exception {
        return post(url, null,null,false);
    }

    /**
     * 携带K-V参数发送HTTP POST请求
     * @param url 请求url
     * @param params 请求参数
     * @return
     */
    public static String post(String url, Map<String, String> params) throws Exception {
        return post(url, params, null,false);
    }


    /**
     * 不携带参数发送HTTPS POST请求
     * @param url 请求url
     * @return
     */
    public static String postBySSL(String url) throws Exception {
        return post(url, null,null,true);
    }

    /**
     * 携带K-V参数发送HTTPS POST请求
     * @param url 请求url
     * @param params 请求参数
     * @return
     */
    public static String postBySSL(String url, Map<String, String> params) throws Exception {
        return post(url, params, null,true);
    }

    /**
     * 发送K-V形式参数的POST请求
     * @param url 请求url
     * @param params 请求参数
     * @param headers 请求消息头
     * @param isSSL 是否启用SSL
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headers,boolean isSSL) throws Exception {
        String result = null;
        CloseableHttpClient httpClient=null;
        if(isSSL){
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setSSLSocketFactory(createSSLConnSocketFactory()).build();
        }else{
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setConnectionManager(connectionPool).build();
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //设置消息头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            if(params !=null && !params.isEmpty()){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                            .getValue());
                    pairList.add(pair);
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (statusCode != 200) {
                log.error("HTTP错误响应状态码 : " + statusCode);
                log.error("HTTP错误响应描述：" + reasonPhrase);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.error("系统异常:{}", e);
                }
            }
        }
        return result;
    }

    /**
     * 发送普通字符串的POST请求
     * @param url 请求url
     * @param str 请求参数字符串
     * @param dataFmt 数据格式：json、xml、form
     * @param isSSL 是否启用SSL
     * @return
     * @throws Exception
     */
    public static String postStr(String url, String str,String dataFmt,boolean isSSL) throws Exception {
//        log.info("url:"+url+",params:"+str);
        String result = null;
        CloseableHttpClient httpClient=null;
        if(isSSL){
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setSSLSocketFactory(createSSLConnSocketFactory()).build();
        }else{
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setConnectionManager(connectionPool).build();
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if(REQUEST_DATA_FORMAT_FORM.equals(dataFmt)){
                StringEntity stringEntity = new StringEntity(str, "UTF-8");//构造字符串数据
                stringEntity.setContentType("application/x-www-form-urlencoded"); //设置FORM表单类型
                httpPost.setEntity(stringEntity);
            }else if(REQUEST_DATA_FORMAT_JSON.equals(dataFmt)){
                StringEntity stringEntity = new StringEntity(str, "UTF-8");//构造字符串数据
                stringEntity.setContentType("application/json;charset=UTF-8");//设置JSON数据
                httpPost.setEntity(stringEntity);
            }else if(REQUEST_DATA_FORMAT_XML.equals(dataFmt)){
                StringEntity stringEntity = new StringEntity(str, "UTF-8");//构造字符串数据
                stringEntity.setContentType("application/xml;charset=UTF-8");//设置XML数据
                httpPost.setEntity(stringEntity);
            }else{
                log.error("指定的数据传输格式错误");
                return null;
            }
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (statusCode != 200) {
                log.error("HTTP错误响应状态码 : " + statusCode);
                log.error("HTTP错误响应描述：" + reasonPhrase);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.error("系统异常:{}", e);
                }
            }
        }
        return result;
    }

    /**
     * 携带JSON格式参数发送HTTP POST请求
     * @param url 请求url
     * @param str 请求参数
     * @return
     */
    public static String postStrWithJSON(String url, String str) throws Exception {
        return postStr(url,str,REQUEST_DATA_FORMAT_JSON,false);
    }

    /**
     * 携带XML格式参数发送HTTP POST请求
     * @param url 请求url
     * @param str 请求参数
     * @return
     */
    public static String postStrWithXml(String url, String str) throws Exception {
        return postStr(url,str,REQUEST_DATA_FORMAT_XML,false);
    }

    /**
     * 携带JSON格式参数发送HTTPS POST请求
     * @param url 请求url
     * @param str 请求参数
     * @return
     */
    public static String postStrBySSLWithJSON(String url, String str) throws Exception {
        return postStr(url,str,REQUEST_DATA_FORMAT_JSON,true);
    }


    /**
     * 携带XML格式参数发送HTTPS POST请求
     * @param url 请求url
     * @param str 请求参数
     * @return
     */
    public static String postBySSLWithXML(String url, String str) throws Exception {
        return postStr(url,str,REQUEST_DATA_FORMAT_XML,true);
    }

    /**
     * 发送PUT请求
     * @param url 请求url
     * @param str 请求参数字符串
     * @param dataFmt 数据格式：json、xml、form
     * @param isSSL 是否启用SSL
     * @return
     * @throws Exception
     */
    public static String putStr(String url, String str,String dataFmt,boolean isSSL) throws Exception {
        log.info("url:"+url+",params:"+str);
        String result = null;
        CloseableHttpClient httpClient=null;
        if(isSSL){
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setSSLSocketFactory(createSSLConnSocketFactory()).build();
        }else{
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setConnectionManager(connectionPool).build();
        }
        CloseableHttpResponse response = null;
        try {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setConfig(requestConfig);
            if(REQUEST_DATA_FORMAT_FORM.equals(dataFmt)){
                StringEntity stringEntity = new StringEntity(str, "UTF-8");//构造字符串数据
                stringEntity.setContentType("application/x-www-form-urlencoded"); //设置FORM表单类型
                httpPut.setEntity(stringEntity);
            }else if(REQUEST_DATA_FORMAT_JSON.equals(dataFmt)){
                StringEntity stringEntity = new StringEntity(str, "UTF-8");//构造字符串数据
                stringEntity.setContentType("application/json;charset=UTF-8");//设置JSON数据
                httpPut.setEntity(stringEntity);
            }else if(REQUEST_DATA_FORMAT_XML.equals(dataFmt)){
                StringEntity stringEntity = new StringEntity(str, "UTF-8");//构造字符串数据
                stringEntity.setContentType("application/xml;charset=UTF-8");//设置XML数据
                httpPut.setEntity(stringEntity);
            }else{
                log.error("指定的数据传输格式错误");
                return null;
            }
            response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (statusCode != 200) {
                log.error("HTTP错误响应状态码 : " + statusCode);
                log.error("HTTP错误响应描述：" + reasonPhrase);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.error("系统异常:{}", e);
                }
            }
        }
        return result;
    }


    /**
     * 携带JSON格式数据发送HTTP PUT请求
     * @param url 请求url
     * @param str 请求参数
     * @return
     * @throws Exception
     */
    public static String putStrWithJSON(String url, String str) throws Exception{
        return putStr(url,str,REQUEST_DATA_FORMAT_JSON,false);
    }

    /**
     * 创建SSL安全连接
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }


    /**
     * 发送 POST Multipart 数据
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String postMultipart(String url,Map<String,String> headers,Map<String, String> params,Map<String,List<File>> files,boolean isSSL) throws Exception {
        String result=null;
        CloseableHttpClient httpClient=null;
        if(isSSL){
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setSSLSocketFactory(createSSLConnSocketFactory()).build();
        }else{
            httpClient = HttpClients.custom().setRetryHandler(retryHandler()).setConnectionManager(connectionPool).build();
        }
        CloseableHttpResponse response=null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //设置消息头
            if(MapUtils.isNotEmpty(headers)){
                for(Map.Entry<String, String> entry : headers.entrySet()){
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
            if(MapUtils.isNotEmpty(params)){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    meBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create("text/plain", "utf-8")));
                }
            }
            if(MapUtils.isNotEmpty(files)){
                for(Map.Entry<String, List<File>> entry : files.entrySet()) {
                    for(File file:entry.getValue()){
                        FileBody fileBody = new FileBody(file);
                        meBuilder.addPart(entry.getKey(),fileBody);
                    }
                }
            }
            HttpEntity reqEntity = meBuilder.build();
            httpPost.setEntity(reqEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase=response.getStatusLine().getReasonPhrase();
            log.info("响应状态码 : "+statusCode);
            log.info("响应描述："+reasonPhrase);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                log.info("响应内容："+result);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.error("系统异常:{}", e);
                }
            }
        }
        return result;
    }


    public static void main(String args[]) throws Exception {
        String url = "https://172.24.133.33:8443/foreign/order/BJPHYB2017051100013/audit/1/1";

        // 设置请求参数
        JSONObject paramJson = new JSONObject();

        //审核人名称
        paramJson.put("empName", "1");
        //审核人员工号
        paramJson.put("empNo", "1");
        //审核时间
        paramJson.put("operatingTime", new Date());
        //驳回原因
        paramJson.put("reason", "1");
        //状态
        paramJson.put("status", 1);

        // 请求老运营接口
        String jsonStr = paramJson.toJSONString();
        //String s = HttpClientUtil.postStrBySSLWithJSON(url, jsonStr);
//        url = "http://10.0.129.100:8081/sseapi/outsideSys/hosting/checkResultNotice";
//        jsonStr = "{\"checkStatus\":1,\"checkTime\":1494521996586,\"assetNo\":\"BJPHYB2017051100013\",\"checkResult\":\"审核成功\"}";
        String s = RetryHttpUtil.postStrBySSLWithJSON(url, jsonStr);
        System.out.println(s);
//        String result = HttpUtil.httpsGet(url, "");

    }






}
