package com.base.util.jpush;

import java.util.HashMap;
import java.util.Map;

import cn.jiguang.common.ClientConfig;
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

public class JPushUtil {

	private static final String JPushAppKey = "ef2702ce923784b6dfdbc1b0";
	private static final String JPushMasterSecret = "f0a443e39f34a98cf5674ed1";

	/**
	 * 默认极光推送客户端
	 */
	public static JPushClient defaultJPushClient() {
		JPushClient jPushClient = new JPushClient(JPushMasterSecret, JPushAppKey);
		return jPushClient;
	}

	/**
	 * 极光推送客户端
	 */
	public static JPushClient customJPushClient() {
		ClientConfig config = ClientConfig.getInstance();
		// config.setMaxRetryTimes(5);
		config.setConnectionTimeout(10 * 1000); // 设置连接超时
		// config.setSSLVersion("TLSv1.1"); // 设置极光服务器支持版本SSLv3, TLSv1, TLSv1.1, TLSv1.2
		// config.setApnsProduction(false); // 设置生产环境
		// config.setTimeToLive(60 * 60 * 24); // 设置推送存活时间
		JPushClient jPushClient = new JPushClient(JPushMasterSecret, JPushAppKey, null, config);
		return jPushClient;
	}

	/**
	 * 点对点推送
	 * 
	 * @param alert标题
	 * @param msg内容
	 * @param deviceType设备类型
	 * @param registrationID极光推送手机唯一标示
	 * @param type消息类型:1新版本;2多设备登录
	 * @return
	 */
	public static JPushModel pushToOne(String alert, String msg, String deviceType, String registrationID, String type,
			Map<String, String> extras) {
		JPushClient jpush = new JPushClient(JPushMasterSecret, JPushAppKey);
		PushResult result = null;
		Builder builder = Message.newBuilder().setMsgContent(msg).setContentType(type);
		if (extras != null)
			builder.addExtras(extras);
		try {
			PushPayload pp = null;
			// 安卓
			if ("1".equals(deviceType)) {
				pp = PushPayload.newBuilder().setPlatform(Platform.android())
						.setAudience(Audience.newBuilder()
								.addAudienceTarget(AudienceTarget.registrationId(registrationID)).build())
						.setMessage(builder.build()).build();
			} else {
				// IOS
				pp = PushPayload.newBuilder().setPlatform(Platform.ios())
						.setAudience(Audience.newBuilder()
								.addAudienceTarget(AudienceTarget.registrationId(registrationID)).build())
						.setMessage(builder.build()).build();
			}
			result = jpush.sendPush(pp);
		} catch (Exception e) {
			e.printStackTrace();
			return new JPushModel("0", "推送失败", null);
		}
		if (result.isResultOK()) {
			return new JPushModel("1", "推送成功", null);
		} else {
			return new JPushModel("0", "推送失败", null);
		}

	}

	/**
	 * 全推通知
	 * 
	 * @param title
	 * @param msg
	 * @param type
	 * @param extras
	 */
	public static void pushAllNotification(String title, String msg, String type, Map<String, String> extras) {
		JPushClient jpush = new JPushClient(JPushMasterSecret, JPushAppKey);
		try {
			PushPayload pp = null;
			pp = PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
					.setNotification(Notification.newBuilder().setAlert("")
							.addPlatformNotification(
									IosNotification.newBuilder().setContentAvailable(true).addExtras(extras).build())
							.build())
					.build();
			;
			jpush.sendPush(pp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 全推消息
	 * 
	 * @param title
	 * @param msg
	 * @param type
	 * @param extras
	 */
	public static void pushAllMsg(String title, String msg, String type, Map<String, String> extras) {
		JPushClient jpush = new JPushClient(JPushMasterSecret, JPushAppKey);
		Builder builder = Message.newBuilder();
		builder.setContentType("");
		builder.setMsgContent("");
		if (extras != null)
			builder.addExtras(extras);
		try {
			PushPayload pp = null;
			pp = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all())
					.setMessage(builder.build()).build();
			jpush.sendPush(pp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// JPushClient jpush = new JPushClient( JPushMasterSecret, JPushAppKey );
		// jpu.point2point( "苏荷酒吧", "测试", "", Constant.IOS);

		// JPushClient jpush = new JPushClient( JPushMasterSecret, JPushAppKey );
		// jpush.sendNotificationAll( "1111111/" );

		Map<String, String> extras = new HashMap<String, String>();
		extras.put("msg", "333333333");
		extras.put("title", "1");
		extras.put("time", System.currentTimeMillis() + "");
		extras.put("type", "2");
		extras.put("rediType", "1");
		// JPushUtil.point2point("取消预约", "neirong","2", "0707e64e4e0", "6",
		// null);
		JPushUtil.pushToOne("", "", "2", "1", "", extras);
		// JPushUtil.pushAllMsg("全平台推送自定义测试", "333", "2", extras);
		// JPushUtil.pushAllNotification("广告推送测试", "333", "3", extras);
		// JPushClient jpush = new JPushClient( JPushMasterSecret, JPushAppKey );
		/*
		 * jpush.sendIosNotificationWithRegistrationID("title",extras, "0707e64e4e0" );
		 * jpush.sendAndroidMessageWithRegistrationID( "title", "message", "0707e64e4e0"
		 * );
		 */

		/*
		 * PushResult result = null; PushPayload pp = PushPayload.newBuilder()
		 * .setPlatform(Platform.android_ios()) .setAudience(Audience.newBuilder()
		 * .addAudienceTarget(AudienceTarget.registrationId("0707e64e4e0")) .build())
		 * .setMessage(Message.newBuilder() .setMsgContent("") .addExtra("type", 8)
		 * .build()) .build(); result = jpush.sendPush(pp);
		 */
	}
}