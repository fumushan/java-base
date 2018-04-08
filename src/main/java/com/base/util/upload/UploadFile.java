package com.base.util.upload;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 上传文件信息
 */
public class UploadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 文件类型
	 */
	private String fileType;

	/**
	 * 文件在服务器名称
	 */
	private String fileServerName;

	/**
	 * 文件在服务器保存路径
	 */
	private String fileServerPath;

	/**
	 * 临时文件
	 */
	@JSONField(serialize = false)
	private File tempFile;

	/**
	 * 文件流
	 */
	@JSONField(serialize = false)
	private InputStream inputStream;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileServerName() {
		return fileServerName;
	}

	public void setFileServerName(String fileServerName) {
		this.fileServerName = fileServerName;
	}

	public String getFileServerPath() {
		return fileServerPath;
	}

	public void setFileServerPath(String fileServerPath) {
		this.fileServerPath = fileServerPath;
	}

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}