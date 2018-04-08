package com.base.util.upload;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.base.util.DateUtils;

/**
 * 文件上传服务接口
 */
public interface UploadHelper {

	public static Logger logger = LoggerFactory.getLogger(UploadHelper.class);

	/**
	 * 文件分隔符
	 */
	public final static String separator = "/";

	/**
	 * 图片类型
	 */
	public final static String[] imageArray = { "bmp", "gif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico" };

	/**
	 * 
	 * @param file上传文件
	 * @param rootPath文件在FTP服务器上的
	 * @return
	 */
	public UploadFile uploadFile(MultipartFile file, String rootPath);
	
	
	/**
	 * 获取上传文件信息
	 * 
	 * @param fileName文件名称
	 * @param filePath文件在服务器上保存根路径
	 * @param inputStream文件流
	 * @return
	 */
	public default UploadFile getUploadFile(String fileName, String rootPath, InputStream inputStream) {

		String suffix = this.getFileSuffix(fileName);
		String fileType = this.getFileType(suffix);
		String fileServerName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
		Date date = new Date();
		String targetPath = DateUtils.formatDate(date, "yyyyMM") + separator + DateUtils.formatDate(date, "dd");
		String fileServerPath = rootPath + separator + targetPath + separator;

		UploadFile uploadFile = new UploadFile();
		uploadFile.setFileName(fileName);
		uploadFile.setFileType(fileType);
		uploadFile.setFileServerName(fileServerName);
		uploadFile.setFileServerPath(fileServerPath);
		uploadFile.setInputStream(inputStream);

		return uploadFile;
	}

	/**
	 * 获取上传文件后缀名
	 * 
	 * @param fileName文件名称
	 * @return
	 */
	public default String getFileSuffix(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix;
	}

	/**
	 * 获取上传文件类型
	 * 
	 * @param suffix文件后缀名
	 * @return
	 */
	public default String getFileType(String suffix) {
		for (String image : imageArray) {
			if (suffix.equalsIgnoreCase(image)) {
				return "IMAGE";
			}
		}
		return "FILE";
	}

}
