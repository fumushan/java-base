package com.base.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数签名工具
 * 
 * @author msi
 *
 */
public class SignUtil {
	
	public static String APP_TOKEN = TokenUtil.APP_TOKEN;
	final static Logger logger = LoggerFactory.getLogger(SignUtil.class);

	/**
	 * 生成签名
	 * 
	 * @param paramsMap
	 * @return
	 */
	public static String createSign(Map<String, String> paramsMap) {
		
		StringBuffer sb = new StringBuffer();
		Map<String, String> map = paramsMap;
		SortedMap<String, String> sortParams = new TreeMap<String, String>(map);
		sortParams.forEach((key,value) ->{
			sb.append(key + "=" + value + "&");
		});
		sb.append("key=").append(APP_TOKEN);
		return MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
	}
	
}
