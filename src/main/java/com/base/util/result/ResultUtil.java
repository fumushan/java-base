package com.base.util.result;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResultUtil {

	public static final String SUCCESS_MSG = "数据加载成功";
	public static final String FAIL_MSG = "数据加载失败";
	public static final String PARAM_MSG = "请求参数错误";
	public static final String HASLOGIN_MSG = "登录状态错误";
	public static SerializerFeature[] feature = { SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse,
			SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect };

	/**
	 * 成功返回结果
	 * 
	 * @param staus
	 * @param msg
	 * @param data
	 * @return
	 */
	public static String success(String msg, Object data, boolean success) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("staus", 100);
		result.put("msg", msg != null ? msg : SUCCESS_MSG);
		result.put("data", data);
		result.put("success", success);
		return JSON.toJSONString(result, feature);
	}

	/**
	 * 失败返回结果
	 * 
	 * @param staus
	 * @param msg
	 * @param data
	 * @return
	 */
	public static String fail(String msg, boolean success) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("staus", 200);
		result.put("msg", msg != null ? msg : FAIL_MSG);
		result.put("success", success);
		return JSON.toJSONString(result, feature);
	}

	/**
	 * 请求参数错误
	 * 
	 * @param staus
	 * @param msg
	 * @return
	 */
	public static String params(String msg, boolean success) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("staus", 300);
		result.put("msg", msg != null ? msg : PARAM_MSG);
		result.put("success", success);
		return JSON.toJSONString(result, feature);
	}

	/**
	 * 是否登录结果
	 * 
	 * @param msg
	 * @param hasLogin
	 * @return
	 */
	public static String hasLogin(String msg, boolean hasLogin) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("staus", 400);
		result.put("msg", msg != null ? msg : HASLOGIN_MSG);
		result.put("hasLogin", false);
		return JSON.toJSONString(result, feature);
	}

}
