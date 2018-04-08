package com.base.util.upload;

import org.springframework.web.multipart.MultipartFile;

import com.base.util.FileUtil;

/**
 * FTP上传文件服务类
 */
public class FtpUploadHelper implements UploadHelper {

	private FtpProperty ftpProperty;

	public void setFtpProperty(FtpProperty ftpProperty) {
		this.ftpProperty = ftpProperty;
	}

	@Override
	public UploadFile uploadFile(MultipartFile file, String rootPath) {
		if (FileUtil.isEmpty(file)) {
			return null;
		}

		UploadFile uploadFile = null;
		try {
			uploadFile = getUploadFile(file.getOriginalFilename(), rootPath, file.getInputStream());
			FtpUploadUtils.uploadFile(ftpProperty, uploadFile.getFileServerName(), uploadFile.getFileServerPath(), uploadFile.getInputStream());
			logger.info("ftp upload file success");
		} catch (Exception e) {
			logger.error("ftp upload file fail:" + e);
		}
		return uploadFile;
	}

}
