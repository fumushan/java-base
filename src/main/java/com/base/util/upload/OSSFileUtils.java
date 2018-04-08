package com.base.util.upload;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * OSS上传下载文件服务类
 */
public class OSSFileUtils {

	public final static Logger logger = LoggerFactory.getLogger(OSSFileUtils.class);

	private final static String endPoint = "";// 阿里云API的内或外网域名

	private final static String accessKeyId = "";// 阿里云API的密钥Access Key ID

	private final static String accessKeySecret = ""; // 阿里云API的密钥Access Key Secret

	/**
	 * 获取阿里云OSS客户端对象
	 */
	public final static OSSClient getOSSClient() {
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(endPoint, new DefaultCredentialProvider(accessKeyId, accessKeySecret), null);
			logger.info("OSS connect success");
		} catch (Exception e) {
			logger.error("OSS connect fail", e);
		}
		return ossClient;
	}

	/**
	 * 关闭阿里云OSS客户端对象
	 */
	public final static void disconnect(OSSClient client) {
		try {
			if (client != null) {
				client.shutdown();
			}
			logger.info("OSS disconnect success");
		} catch (Exception e) {
			logger.error("OSS disconnect fail:" + e);
		}
	}

	/**
	 * OSS服务器上传文件
	 * 
	 * @param inputStream上传文件流
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 * @return
	 */
	public final static String uploadFile(InputStream inputStream, String bucketName, String filePath) {
		String result = null;
		OSSClient client = getOSSClient();
		try {
			if (inputStream != null) {
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setCacheControl("no-cache");
				metadata.setHeader("Pragma", "no-cache");
				metadata.setContentEncoding("utf-8");
				metadata.setContentType(getContentType(filePath));
				PutObjectResult putResult = client.putObject(bucketName, filePath, inputStream, metadata);
				result = putResult.getETag();
				logger.info("OSS upload file success");
			}
		} catch (Exception e) {
			logger.error("OSS upload file fail", e);
			throw new ServiceException("OSS服务器上传文件失败！！！");
		} finally {
			disconnect(client);
		}

		return result;
	}

	/**
	 * OSS服务器上传文件
	 * 
	 * @param file上传文件
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 * @return
	 */
	public final static String uploadFile(File file, String bucketName, String filePath) {
		String result = null;
		OSSClient client = getOSSClient();
		try {
			if (file != null) {
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setCacheControl("no-cache");
				metadata.setHeader("Pragma", "no-cache");
				metadata.setContentEncoding("utf-8");
				metadata.setContentType(getContentType(filePath));
				PutObjectResult putResult = client.putObject(bucketName, filePath, file, metadata);
				result = putResult.getETag();
				logger.info("OSS upload file success");
			}
		} catch (Exception e) {
			logger.error("OSS upload file fail", e);
			throw new ServiceException("OSS服务器上传文件失败！！！");
		} finally {
			disconnect(client);
		}
		return result;
	}

	/**
	 * OSS服务器读取文件
	 * 
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 * @return
	 */
	public final static InputStream readFile(String bucketName, String filePath) {
		OSSClient client = getOSSClient();
		try {
			OSSObject ossObject = client.getObject(bucketName, filePath);
			return ossObject.getObjectContent();
		} catch (Exception e) {
			logger.error("OSS read fail", e);
			throw new ServiceException("OSS服务器上读取文件失败");
		} finally {
			disconnect(client);
		}
	}

	/**
	 * OSS服务器删除文件
	 * 
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 */
	public final static void deleteFile(String bucketName, String filePath) {
		OSSClient client = getOSSClient();
		try {
			client.deleteObject(bucketName, filePath);
			logger.info("OSS delete 【" + bucketName + "】" + filePath + "success");
		} catch (Exception e) {
			logger.error("OSS delete 【" + bucketName + "】" + filePath + "fail", e);
			throw new ServiceException("OSS服务器上删除文件失败");
		} finally {
			disconnect(client);
		}
	}

	/**
	 * 通过文件名判断文件内容类型
	 */
	private static String getContentType(String fileName) {

		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("bmp".equalsIgnoreCase(fileExtension))
			return "image/bmp";
		if ("gif".equalsIgnoreCase(fileExtension))
			return "image/gif";
		if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)
				|| "png".equalsIgnoreCase(fileExtension))
			return "image/png";
		if ("html".equalsIgnoreCase(fileExtension))
			return "text/html";
		if ("txt".equalsIgnoreCase(fileExtension))
			return "text/plain";
		if ("vsd".equalsIgnoreCase(fileExtension))
			return "application/vnd.visio";
		if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension))
			return "application/vnd.ms-powerpoint";
		if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)
				|| "pdf".equalsIgnoreCase(fileExtension))
			return "application/msword";
		if ("xml".equalsIgnoreCase(fileExtension))
			return "text/xml";
		return "application/msword";
	}

}
