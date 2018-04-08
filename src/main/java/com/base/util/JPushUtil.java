package com.base.util;

import java.util.HashMap;
import java.util.Map;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Message.Builder;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.model.JpushDataModel;

public class JPushUtil {

	private static final String appKey = "ef2702ce923784b6dfdbc1b0";

	private static final String masterSecret = "f0a443e39f34a98cf5674ed1";

	private static Logger logger = LoggerFactory.getLogger(JPushUtil.class);

	/**
	 * @param alert
	 *            标题
	 * @param msg
	 *            内容
	 * @param registrationID
	 *            极光推送手机唯一标示
	 * @param type
	 *            消息类型 1 新版本 2 多设备登录
	 * @return
	 * @Title: point2point
	 * @Description: 点对点推送
	 */
	public static JpushDataModel point2point(String alert, String msg, String deviceType, String registrationID,
			String type, Map<String, String> extras) {

		PushResult result = null;
		JPushClient pushClient = new JPushClient(masterSecret, appKey);
		Builder builder = Message.newBuilder().setMsgContent(msg).setContentType(type);
		if (extras != null)
			builder.addExtras(extras);
		try {
			PushPayload pushPayload = null;
			PushPayload.Builder pushBuilder = PushPayload.newBuilder();
			if ("1".equals(deviceType)) {
				pushBuilder = pushBuilder.setPlatform(Platform.android());
			} else {
				pushBuilder = pushBuilder.setPlatform(Platform.ios());
			}
			pushPayload = getPushPayload(pushBuilder, registrationID, builder);
			result = pushClient.sendPush(pushPayload);
		} catch (Exception e) {
			logger.error("推送失败 ! ,{}", e);
			return new JpushDataModel("0", "推送失败", null);
		}
		if (result.isResultOK()) {
			return new JpushDataModel("1", "推送成功", null);
		} else {
			return new JpushDataModel("0", "推送失败", null);
		}

	}

	private static PushPayload getPushPayload(PushPayload.Builder pushBuilder, String registrationID, Builder builder) {

		Audience audience = Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(registrationID))
				.build();
		return pushBuilder.setAudience(audience).setMessage(builder.build()).build();
	}

	/**
	 * 全推通知
	 *
	 * @param msg
	 *            推送内容
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public static void pushAllNotification(String title, String msg, String type, Map<String, String> extras){

		JPushClient pushClient = new JPushClient(masterSecret, appKey);
		try {
			PushPayload.Builder pushBuilder = PushPayload.newBuilder().setPlatform(Platform.android_ios());
			pushBuilder.setAudience(Audience.all());
			Notification notification = Notification.newBuilder().setAlert("").addPlatformNotification(
					IosNotification.newBuilder().setContentAvailable(true).addExtras(extras).build()).build();
			PushPayload pushPayload = pushBuilder.setNotification(notification).build();
			pushClient.sendPush(pushPayload);
		} catch (Exception e) {
			logger.error("推送失败 ! ,{}", e);
		}
	}

	/**
	 * 全推消息
	 *
	 * @param msg
	 *            推送内容
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public static void pushAllMsg(String title, String msg, String type, Map<String, String> extras){

		JPushClient jpush = new JPushClient(masterSecret, appKey);
		Builder builder = Message.newBuilder();
		builder.setContentType("").setMsgContent("");
		if (extras != null)
			builder.addExtras(extras);
		try {
			PushPayload pushPayload = null;
			pushPayload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all())
					.setMessage(builder.build()).build();
			jpush.sendPush(pushPayload);
		} catch (Exception e) {
			logger.error("推送失败 ! ,{}", e);
		}
	}

	public static void main(String[] args) throws APIConnectionException, APIRequestException {

		Map<String, String> extras = new HashMap<String, String>();
		extras.put("msg", "333333333");
		extras.put("title", "1");
		extras.put("time", System.currentTimeMillis() + "");
		extras.put("type", "2");
		extras.put("rediType", "1");
		JPushUtil.pushAllMsg("全平台推送自定义测试", "333", "2", extras);
	}
}
