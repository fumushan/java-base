package com.base.util.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtil {

	public final static String MAIL_HOST = "smtp.163.com";// 邮箱服务器协议地址
	public final static String MAIL_USERNAME = "15088658623@163.com";// 邮箱账号
	public final static String MAIL_PASSWORD = "fms880930";// 邮箱授权码

	public static Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", MAIL_HOST);
		props.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(props);
		// 是否启用debug模式
		session.setDebug(true);
		return session;
	}

	public static void sendSimpleMail(String from, String to, String title, String content) {
		Session session = MailUtil.getSession();
		try {
			// 创建MimeMessage格式的邮件
			MimeMessage message = new MimeMessage(session);

			// 设置发收者的邮箱
			Address fromAddress = new InternetAddress(MAIL_USERNAME);
			message.setFrom(fromAddress);

			// 设置接收者的邮箱
			Address toAddress = new InternetAddress(to);
			message.setRecipient(RecipientType.TO, toAddress);

			message.setSubject(title);
			message.setText(content);
			message.saveChanges();

			Transport transport = session.getTransport("smtp");
			transport.connect(MAIL_HOST, MAIL_USERNAME, MAIL_PASSWORD);// 服务器地址，邮箱账号，授权码
			transport.sendMessage(message, message.getAllRecipients());// 发送邮件
			transport.close();// 关闭服务器通道
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		
		String title = "江湖笑";
		String content = "江湖笑，恩怨了";
		String from = "15088658623@163.com";
		String to = "15088658623@163.com";
		
		MailUtil.sendSimpleMail(from, to, title, content);
	}

}
