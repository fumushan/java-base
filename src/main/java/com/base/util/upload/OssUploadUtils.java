package com.base.util.upload;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
/**
 * OSS文件上传下载服务工具类
 */
public class OssUploadUtils {

	public final static Logger logger = LoggerFactory.getLogger(OssUploadUtils.class);

	/**
	 * 获取阿里云OSS客户端对象
	 */
	public final static OSSClient getOSSClient(OSSProperty ossProperty) {
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(ossProperty.getEndPoint(),
					new DefaultCredentialProvider(ossProperty.getAccessKeyId(), ossProperty.getAccessKeySecret()),
					null);
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
	 * 创建阿里云存储空间
	 * 
	 * @param ossProperty服务器配置信息
	 * @param bucketName存储空间名：存储空间的命名规范如下：只能包括小写字母、数字和短横线,必须以小写字母或者数字开头和结尾,长度必须在3-63字节之间。
	 * @return
	 */
	public final static boolean createBucket(OSSProperty ossProperty, String bucketName) {
		OSSClient client = getOSSClient(ossProperty);
		Bucket bucket = client.createBucket(bucketName);
		return bucketName.equals(bucket.getName());
	}

	/**
	 * 删除阿里云存储空间
	 * 
	 * @param ossProperty服务器配置信息
	 * @param bucketName存储空间名称
	 */
	public final static void deleteBucket(OSSProperty ossProperty, String bucketName) {
		OSSClient client = getOSSClient(ossProperty);
		try {
			client.deleteBucket(bucketName);
			logger.info("OSS delete bucket success");
		} catch (Exception e) {
			logger.error("OSS delete bucket fail", e);
			throw new ServiceException("OSS服务器删除存储空间失败！！！");
		} finally {
			disconnect(client);
		}
	}

	/**
	 * OSS服务器上传文件
	 * 
	 * @param ossProperty服务器配置信息
	 * @param inputStream上传文件流
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 * @return
	 */
	public final static String uploadFile(OSSProperty ossProperty, InputStream inputStream, String bucketName,
			String filePath) {
		String result = null;
		OSSClient client = getOSSClient(ossProperty);
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
	 * @param ossProperty服务器配置信息
	 * @param file上传文件
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 * @return
	 */
	public final static String uploadFile(OSSProperty ossProperty, File file, String bucketName, String filePath) {
		String result = null;
		OSSClient client = getOSSClient(ossProperty);
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
	 * @param ossProperty服务器配置信息
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 * @return
	 */
	public final static InputStream readFile(OSSProperty ossProperty, String bucketName, String filePath) {
		OSSClient client = getOSSClient(ossProperty);
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
	 * @param ossProperty服务器配置信息
	 * @param bucketName存储空间名称
	 * @param filePath存储空间中文件的路径+文件名,例：home/201808/08/xxx.xml
	 */
	public final static void deleteFile(OSSProperty ossProperty, String bucketName, String filePath) {
		OSSClient client = getOSSClient(ossProperty);
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
