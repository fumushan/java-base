package com.base.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.exception.ServiceException;
import com.base.util.MD5Util;
import com.base.util.TokenUtil;

import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	private static final OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(10, TimeUnit.SECONDS)
			.connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build(); // 设置各种超时时间;

	/**
	 * 发送HTTP请求
	 * 
	 * @param url
	 * @param formBody
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, RequestBody formBody) throws IOException {
		Request request = requestBuilder(url).post(formBody).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			logger.error("url :{} , 请求失败 : {}", url, response);
			throw new IOException("Unexpected code " + response);
		}
		return response.body().string();
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param url
	 * @param formBody
	 * @return
	 * @throws IOException
	 */
	public static final String TAG = "MainActivity";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static String postJson(String url, String json) throws IOException {
		RequestBody requestBody = RequestBody.create(JSON, json);
		Request request = requestBuilder(url).post(requestBody).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			logger.error("url :{} , 请求失败 : {}", url, response);
			throw new IOException("Unexpected code " + response);
		}
		return response.body().string();
	}

	/**
	 * 发送HTTP请求（自动加签名）
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String postWithSign(String url, Map<String, String> params) throws IOException {
		Map<String, String> paramsMap = params;
		RequestBody formBody = createSignFormBody(paramsMap);
		paramsMap = null;
		params = null;
		Request request = requestBuilder(url).post(formBody).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			logger.error("url :{} , 请求失败 : {}", url, response);
			throw new IOException("Unexpected code " + response);
		}
		return response.body().string();
	}

	public static String post(String url, Map<String, String> params) {

		Request.Builder builder = requestBuilder(url);
		return postForm(params, builder);
	}

	public static String post(String url, Map<String, String> params, Map<String, String> headers) {

		Request.Builder builder = requestBuilder(url);
		headers.forEach((key, value) -> builder.addHeader(key, value));
		return postForm(params, builder);
	}

	private static String postForm(Map<String, String> params, Request.Builder builder) {

		Request request = builder.post(formBody(params)).build();
		Response response;
		try {
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				logger.error("request fail !! {}", response);
				throw new IOException("Unexpected code " + response);
			}
			return response.body().string();
		} catch (IOException e) {
			logger.error("request fail !! {}", e);
			throw new ServiceException("请求失败");
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param paramsMap
	 * @return
	 */
	private static RequestBody createSignFormBody(Map<String, String> paramsMap) {

		Builder buidler = new FormBody.Builder();
		StringBuffer sb = new StringBuffer();
		Map<String, String> map = paramsMap;
		SortedMap<String, String> sortParams = new TreeMap<String, String>(map);
		Set<Entry<String, String>> es = sortParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (StringUtils.isNotBlank(v)) {
				if (!"uid".equals(k))
					sb.append(k + "=" + v + "&");
				buidler.add(k, v);
			}
		}
		sb.append("key=").append(TokenUtil.APP_TOKEN);
		String signStr = sb.toString();
		String sign = MD5Util.MD5Encode(signStr, "utf-8").toUpperCase();
		buidler.add("sign", sign);
		return buidler.build();
	}

	/**
	 * 请求body
	 * 
	 * @param params
	 * @return
	 */
	public static RequestBody formBody(Map<String, String> params) {

		Builder buidler = new FormBody.Builder();
		Map<String, String> map = params;
		SortedMap<String, String> sortParams = new TreeMap<String, String>(map);
		Set<Entry<String, String>> set = sortParams.entrySet();
		for (Entry<String, String> entry : set) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (StringUtils.isNotBlank(value)) {
				buidler.add(key, value);
			}
		}
		return buidler.build();
	}

	private static Request.Builder requestBuilder(String url) {

		return new Request.Builder().url(url);
	}

	public static void main(String[] args) throws Exception {
		Builder builder = new FormBody.Builder();
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		RequestBody formBody = new FormBody.Builder().add("username","admin").add("password","123456").build();
		Document document = null;
		try {
			document = DocumentHelper.parseText(HttpUtils.post("http://localhost:8080/goose/login", formBody));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		String sts = root.element("retcode").getStringValue();
		System.out.println(sts);
	}
}