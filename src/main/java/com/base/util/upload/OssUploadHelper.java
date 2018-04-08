package com.base.util.upload;

import org.springframework.web.multipart.MultipartFile;

import com.base.util.FileUtil;
import com.base.util.upload.UploadFile;

public class OssUploadHelper implements UploadHelper {

	private OSSProperty ossProperty;

	public void setOssProperty(OSSProperty ossProperty) {
		this.ossProperty = ossProperty;
	}

	@Override
	public UploadFile uploadFile(MultipartFile file, String rootPath) {
		if (FileUtil.isEmpty(file)) {
			return null;
		}
		UploadFile uploadFile = null;
		try {
			uploadFile = this.getUploadFile(file.getOriginalFilename(), rootPath, file.getInputStream());
			// OSS服务器上文件存储路径,例:home/201808/08/xxx.xml
			String filePath = uploadFile.getFileServerPath() + uploadFile.getFileServerName();
			OssUploadUtils.uploadFile(ossProperty, uploadFile.getInputStream(), ossProperty.getBucketName(), filePath);
			logger.error("OSS upload file success");
		} catch (Exception e) {
			logger.error("OSS upload file fail", e);
		}
		return uploadFile;
	}

}
