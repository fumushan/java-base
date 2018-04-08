package com.base.util.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP文件上传下载服务工具类
 */
public class FtpUploadUtils {

	public static Logger logger = LoggerFactory.getLogger(FtpUploadUtils.class);

	public final static String ENCODING = "";

	/**
	 * 连接FTP服务器
	 */
	public static FTPClient getFtpClient(FtpProperty ftpProperty) {
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpProperty.getHost(), ftpProperty.getPort());
			ftpClient.login(ftpProperty.getUsername(), ftpProperty.getPassword());
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				logger.info("ftp connect fail");
				ftpClient.disconnect();
			} else {
				logger.info("ftp connect success");
			}
		} catch (Exception e) {
			logger.error("ftp connect fail:" + e);
		}
		return ftpClient;
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
			logger.error("ftp disconnect fail:" + e);
		}
	}

	/**
	 * FTP文件上传
	 * 
	 * @param fileServerName文件在FTP服务器的名称
	 * @param fileServerPath文件在FTP服务器的保存路径,例：/home/201808/08/
	 * @param inputStream上传文件流
	 * @return
	 */
	public static boolean uploadFile(FtpProperty ftpProperty, String fileServerName, String fileServerPath,	InputStream inputStream) {
		boolean result = false;
		FTPClient ftpClient = getFtpClient(ftpProperty);
		try {
			// 检查文件路径是否存在,不存在则创建目录
			createAndChangeDirectory(ftpClient, ftpProperty, fileServerPath);
			// 上传文件
			ftpClient.storeFile(new String(fileServerName.getBytes("GBK")), inputStream);
			inputStream.close();
			ftpClient.logout();
			logger.info("ftp upload file success");
			result = true;
		} catch (IOException e) {
			logger.error("ftp upload file fail:" + e);
		} finally {
			disconnect(ftpClient);
		}
		return result;
	}

	/**
	 * FTP文件下载
	 * 
	 * @param ftpProperty服务器配置信息
	 * @param fileServerName文件在FTP服务器的名称
	 * @param filePath文件在FTP服务器的保存路径,例：/home/201808/08/
	 */
	public static InputStream readFile(FtpProperty ftpProperty, String fileServerName, String fileServerPath) {
		InputStream inputStream = null;
		FTPClient ftpClient = getFtpClient(ftpProperty);
		logger.info("begin to read 【" + fileServerPath + "】" + fileServerName);
		try {
			// 切换到文件保存路径
			ftpClient.changeWorkingDirectory(fileServerPath);
			inputStream = ftpClient.retrieveFileStream(new String(fileServerName.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
			logger.info("ftp read file success");
		} catch (FileNotFoundException e) {
			logger.error("ftp can not find file", e);
		} catch (SocketException e) {
			logger.error("connect ftp failed ", e);
		} catch (IOException e) {
			logger.error("ftp read file fail", e);
		} finally {
			disconnect(ftpClient);
		}
		return inputStream;
	}

	/**
	 * FTP文件删除
	 * 
	 * @param ftpProperty服务器配置信息
	 * @param fileServerName文件在FTP服务器的名称
	 * @param filePath文件在FTP服务器的保存路径,例：/home/201808/08/
	 */
	public static void deleteFile(FtpProperty ftpProperty, String fileServerName, String fileServerPath) {
		String pathName = fileServerPath + fileServerName;
		FTPClient ftpClient = getFtpClient(ftpProperty);
		try {
			ftpClient.deleteFile(pathName);
			logger.info("ftp delete file 【" + pathName + "】 success");
		} catch (Exception e) {
			logger.error("ftp delete file 【" + pathName + "】 fail", e);
		}
	}

	/**
	 * 检查文件路径是否存在,不存在则创建目录
	 */
	private static boolean createAndChangeDirectory(FTPClient ftpClient, FtpProperty ftpProperty, String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		try {
			path = new String(path.getBytes(ENCODING), FTP.DEFAULT_CONTROL_ENCODING);
			// 检查路径,存在返回true,不存在就创建目录
			if (ftpClient.changeWorkingDirectory(path)) {
				return true;
			} else {
				if (createMultiDirectory(ftpClient, ftpProperty, path)) {
					if (ftpClient.changeWorkingDirectory(path)) {
						return true;
					}
				} else {
					return false;
				}
			}
		} catch (IOException e) {
			logger.info("ftp create directory fail--------path : " + path);
			return false;
		}
		return true;
	}

	/**
	 * 远程FTP服务器创建多级目录，创建目录失败或发生异常则返回false
	 */
	private static boolean createMultiDirectory(FTPClient ftpClient, FtpProperty ftpProperty, String multiPath) {
		try {
			String[] dirs = multiPath.split("/");
			if (dirs != null) {
				ftpClient.changeWorkingDirectory("/");
				// 按顺序检查目录是否存在，不存在则创建目录
				for (int i = 1; i < dirs.length; i++) {
					if (!ftpClient.changeWorkingDirectory(dirs[i])) {
						if (ftpClient.makeDirectory(dirs[i])) {
							if (!ftpClient.changeWorkingDirectory(dirs[i])) {
								return false;
							}
						} else {
							return false;
						}
					}
				}
			}
		} catch (IOException e) {
			logger.info("create directory fail--------path : " + multiPath);
			return false;
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		FtpProperty ftpProperty = new FtpProperty();
		ftpProperty.setHost("");
		ftpProperty.setPort(2121);
		ftpProperty.setUsername("");
		ftpProperty.setPassword("");
		
		/*File file = new File("E:\\RDB机制.png");
		String fileName = file.getName();
		String fileServerPath = "/home/201808/08";*/
		
		
	}
	
}
