package com.base.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP上传下载文件服务类
 */
public class FtpFileUtils {

	public static Logger logger = LoggerFactory.getLogger(FtpFileUtils.class);

	/**
	 * 配置FTP服务器
	 */
	public static String FTP_HOST = "";// FTP服务器地址
	public static int FTP_PORT = 2121;// FTP服务器端口号
	public static String FTP_USERNAME = "";// FTP登录帐号
	public static String FTP_PASSWORD = "";// FTP登录密码

	/**
	 * 连接FTP服务器
	 */
	public static FTPClient getFtpClient() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(FTP_HOST, FTP_PORT);
			ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				logger.info("ftp login fail");
				ftpClient.disconnect();
			} else {
				logger.info("ftp login success");
			}
		} catch (Exception e) {
			logger.error("ftp login fail", e);
		}
		return ftpClient;
	}

	/**
	 * 从FTP服务器上下载文件
	 * 
	 * @param fileName文件名称
	 * @param filePath服务器路径
	 */
	public static InputStream readFile(String fileName, String filePath) {
		InputStream inputStream = null;
		FTPClient ftpClient = getFtpClient();
		try {
			ftpClient.changeWorkingDirectory(filePath);
			inputStream = ftpClient
					.retrieveFileStream(new String(fileName.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
			logger.info("ftp read file success");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("ftp read file fail", e);
		} finally {
			disconnect(ftpClient);
		}
		return inputStream;
	}

	/**
	 * 上传文件到FTP服务器
	 * 
	 * @param fileName文件名称
	 * @param filePath文件在FTP服务器的保存路径
	 * @param inputStream上传文件(流)
	 * @return
	 */
	public static boolean uploadFile(String fileName, String filePath, InputStream inputStream) {
		boolean flag = false;
		FTPClient ftpClient = getFtpClient();
		try {
			// 检查文件路径是否存在
			if (!ftpClient.changeWorkingDirectory(filePath)) {
				ftpClient.makeDirectory(filePath);
			}
			ftpClient.changeWorkingDirectory(filePath);
			// 上传文件
			ftpClient.storeFile(new String(fileName.getBytes("GBK")), inputStream);
			logger.info("ftp upload success");
			System.out.print("ftp upload success");
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("ftp upload fail");
		} finally {
			disconnect(ftpClient);
		}
		return flag;
	}

	/**
	 * 关闭FTP服务器
	 */
	public static void disconnect(FTPClient ftpClient) {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
			logger.info("ftp disconnect success");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("ftp disconnect fail", e);
		}
	}

	public static void main(String[] args) throws Exception {
		String fileName = "权威指南.pdf";
		String filePath = "E:/WorkSpace-FTP/image";

		File file = new File("F:/JavaScript权威指南(第6版)(中文版).pdf");
		System.out.println(file.getPath());
		InputStream uploadStream = new FileInputStream(file);
		uploadFile(fileName, filePath, uploadStream);
	}

}
