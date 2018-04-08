package com.base.util.upload;

/**
 * OSS服务器配置类
 */
public class OSSProperty {

	/**
	 * 阿里云API的内或外网域名
	 */
	private String endPoint;

	/**
	 * 阿里云API的密钥Access Key ID
	 */
	private String accessKeyId;

	/**
	 * 阿里云API的密钥Access Key Secret
	 */
	private String accessKeySecret;

	/**
	 * 阿里云OSS存储空间
	 */
	private String bucketName;

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

}
