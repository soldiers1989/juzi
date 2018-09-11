package com.jzfq.retail.core.messaging.util;

import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.core.messaging.pojo.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lagon
 * @time 2016-4-4 下午10:12:52
 * @description API开发工具类
 */
public class ApiDevUtils {

	private final static Logger log= LoggerFactory.getLogger(ApiDevUtils.class);

	//输出接口的请求日志
    public static void outputRequestLog(Logger log, HttpServletRequest request){
    	log.info("###################请求信息##################");
        log.info("请求时间:{}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.info("请求IP:{}",getIpAddr(request));
    	log.info("请求路径:{}",getRequestURI(request));
    	log.info("请求消息头：{}",getRequestHeadersMap(request));
    	log.info("请求参数：{}",getRequestParametersMap(request));
    	log.info("###########################################");
    }

	//输出JSON字符串，并打印日志
    public static void outputJsonMessage(Logger log, HttpServletRequest request, HttpServletResponse response, Map<String,Object> jsonMap){
    	log.info("###################响应信息##################");
    	log.info("请求路径：{}",getRequestURI(request));
    	log.info("响应时间：{}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		PrintWriter out=null;
		response.setContentType("application/json; charset=UTF-8");
		JSONObject jsonObject=new JSONObject();
		for(String key:jsonMap.keySet()){
			jsonObject.put(key,jsonMap.get(key));
		}
		try {
			out = response.getWriter();
			out.write(jsonObject.toString());
			log.info("响应JSON结果：{}",jsonObject.toString());
		} catch (IOException e) {
			log.error("流输出异常：{}",e);
		}finally{
			out.close();
		}
    	log.info("###########################################");

    }

	//将Message转换成JSON字符串并输出
	public static void outputJsonMessage(HttpServletResponse response, Message message){
		PrintWriter out=null;
		response.setContentType("application/json; charset=UTF-8");
		try {
			out = response.getWriter();
			String msg=message.toJSONString();
			out.write(msg);
			log.info("响应JSON结果：{}",msg);
		} catch (IOException e) {
			log.error("流输出异常：{}",e);
		}finally{
			out.close();
		}

	}

    //将Map转成JSON字符串
    public static String toJSONString(Map<String,Object> jsonMap){

		JSONObject jsonObject=new JSONObject();
		Set<String> keys=jsonMap.keySet();
		for(String key:keys){
			jsonObject.put(key,jsonMap.get(key));
		}
        return jsonObject.toJSONString();
    }

	//获取ip地址
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取一定长度的随机数字
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomNumberByLength(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @Description:把数组转换为一个用逗号分隔的字符串
     */
    public static String converToString(String[] ig) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + ",";
            }
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * @Description:把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

	public static String getTimeStamp(){
		return String.valueOf(System.currentTimeMillis());
	}

	public static TreeMap<String,String> getRequestParametersMap(HttpServletRequest request){
    	TreeMap<String,String> treeMap=new TreeMap<String,String>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = null;
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length >= 1) {
                paramValue = paramValues[0];
            }else{
            	paramValue=null;
            }
            treeMap.put(paramName, StringUtils.trim(paramValue));
        }
        return treeMap;
	}

    public static Map<String,String> getRequestHeadersMap(HttpServletRequest request){
		Map<String,String> headersMap=new LinkedHashMap<String,String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headersMap.put(headerName, headerValue);
        }
        return headersMap;
	}

    public static String getRequestURI(HttpServletRequest request){
		String param = request.getQueryString();
		if (param == null) {
			param = "";
		} else {
			param = "?" + param;
		}
		return request.getRequestURI() + param;
	}

    public static String getRequestURL(HttpServletRequest request){
		String queryString = request.getQueryString();
		if (queryString == null) {
			queryString = "";
		} else {
			queryString = "?" + queryString;
		}
		return request.getRequestURL().toString()+queryString;
	}

    //判断是否是ajax请求
	public static Boolean isAjaxRequest(HttpServletRequest request) {
		String reqType=request.getParameter("reqType");
		if("1".equals(reqType)){
			return true;
		}else if("2".equals(reqType)){
			return false;
		}else{
			return null;
		}
	}

    public static long convert2long(String date, String format) {
		try {
			if (StringUtils.isNotBlank(date)) {
				if (StringUtils.isBlank(format)) {
					format = "yyyy-MM-dd HH:mm:ss";
				}
				SimpleDateFormat sf = new SimpleDateFormat(format);
				return sf.parse(date).getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	}

    //返回异常的堆栈信息
	public static String getExceptionTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

    //格式化long类型的时间为yyyy-MM-dd HH:mm:ss格式时间字符串
    public static String formatLongTime(long longTime) {
        return DateFormatUtils.format(longTime, "yyyy-MM-dd HH:mm:ss");
    }

}
