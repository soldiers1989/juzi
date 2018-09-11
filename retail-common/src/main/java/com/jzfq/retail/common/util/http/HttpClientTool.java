package com.jzfq.retail.common.util.http;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 带有token认证的http请求工具类
 * 
 * @Filename: HttpClientTool.java
 * @Version: 1.0
 * @Author: 杜林谦
 * @Email: dulinqian@juzifenqi.com
 *
 */
public class HttpClientTool extends HttpClientUtil {

	public static String sendPostForAuth(String url, Map<String, Object> paramMap, String token) {
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
			post.addHeader("source", "H5");// 2017.12.21核心组刘星&马乐确认传参【H5】而不是【2】
			post.addHeader("Version", "2.0");
			post.addHeader("Authorization", token);
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

	public static String sendPostForAuthXCX(String url, Map<String, Object> paramMap, String token) {
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
			post.addHeader("source", "xiaochengxu");// 2017.12.21核心组刘星&马乐确认传参【H5】而不是【2】
			post.addHeader("Version", "2.0");
			post.addHeader("Authorization", token);
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

	public static String sendPostForLinkAuth(String url, String jsonVal, String token) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", "application/json");
			post.addHeader("application", "shangcheng");
			post.addHeader("source", "H5");// 2018.3.26核心组刘星&志硕确认传参【H5】而不是【2】
			post.addHeader("Version", "2.0");
			post.addHeader("Authorization", token);
			StringEntity entity = new StringEntity(jsonVal, ContentType.create("application/json", "UTF-8"));
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

	public static String sendPostForLinkAuthXCX(String url, String jsonVal, String token) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", "application/json");
			post.addHeader("application", "shangcheng");
			post.addHeader("source", "xiaochengxu");// 2017.12.21核心组刘星&马乐确认传参【H5】而不是【2】
			post.addHeader("Version", "2.0");
			post.addHeader("Authorization", token);
			StringEntity entity = new StringEntity(jsonVal, ContentType.create("application/json", "UTF-8"));
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

	/**
	 * 包含验签的post请求
	 * 
	 * @param url
	 * @param content
	 * @param type
	 * @param token
	 * @param version
	 * @param imei
	 * @return
	 */
	public static String sendPostOfAuth(String url, String content, String type, String token, String version,
			String imei) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			System.out.println(content);
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", type);
			post.addHeader("Authorization", token);
			post.addHeader("Version", version);
			post.addHeader("imei", imei);
			post.addHeader("application", "shangcheng");
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 包含验签的post请求
	 * 
	 * @param url
	 * @param content
	 * @param type
	 * @param token
	 * @param version
	 * @param source
	 * @return
	 */
	public static String sendPostOfAuth1(String url, String content, String type, String token, String version,
			String source) {
		CloseableHttpClient client = null;

		CloseableHttpResponse resp = null;
		try {
			System.out.println(content);
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", type);
			post.addHeader("Authorization", token);
			post.addHeader("Version", version);
			post.addHeader("source", source);
			post.addHeader("application", "shangcheng");
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 包含验签的post请求
	 * 
	 * @param url
	 * @param content
	 * @param type
	 * @param token
	 * @param version
	 * @param source
	 * @return
	 */
	public static String sendPostOfAuthXCX(String url, String content, String type, String token, String version,
			String source) {
		CloseableHttpClient client = null;

		CloseableHttpResponse resp = null;
		try {
			System.out.println(content);
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", type);
			post.addHeader("Authorization", token);
			post.addHeader("Version", version);
			post.addHeader("source", source);
			post.addHeader("application", "xiaochengxu");
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 包含验签的get请求
	 * 
	 * @param url
	 * @param params
	 * @param type
	 * @param token
	 * @param version
	 * @param imei
	 * @return
	 */
	public static String sendGetOfAuth(String url, Map<String, Object> params, String type, String token,
			String version, String imei) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			client = HttpClients.createDefault();
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (String key : params.keySet()) {
					pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
			}
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-type", type);
			get.addHeader("Authorization", token);
			get.addHeader("Version", version);
			get.addHeader("imei", imei);
			get.addHeader("application", "shangcheng");
			System.out.println(url);
			resp = client.execute(get);
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				String str = EntityUtils.toString(resp.getEntity(), "utf-8");
				return str;
			}
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
     * POST请求，Map形式数据
     * 
     * @param url
     *            请求地址
     * @param param
     *            请求数据
     * @param timeOutSeconds
     *            编码方式
     */
    public static String sendPost(String url, Map<String, String> param,int timeOutSeconds) {

        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

            }
        }
        buffer.deleteCharAt(buffer.length() - 1);

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(timeOutSeconds*1000);  
            conn.setReadTimeout(timeOutSeconds*1000);  
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(buffer);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
	/**
	 * POST请求，Map形式数据
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 */
	public static String sendPost(String url, Map<String, String> param) {

		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

			}
		}
		buffer.deleteCharAt(buffer.length() - 1);

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(buffer);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPostOfAuthForApp(String url, String content, String type, String token, String version,
			String imei, String source) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			System.out.println(content);
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-type", type);
			post.addHeader("Authorization", token);
			post.addHeader("Version", version);
			post.addHeader("imei", imei);
			post.addHeader("application", "shangcheng");
			post.addHeader("source", source);
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
