package com.base.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @project : tykj-common
 * @createTime : 2017年6月5日 : 下午3:07:08
 * @author : lukewei
 * @description :
 */
public class FileUtil extends FileUtils {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private static final String DEFAULT_CHART_SET = "UTF-8";

	/**
	 * 获取上传文件的文件名
	 * 
	 * @param file
	 * @return
	 */
	public static String getOriginalFilename(MultipartFile file) {

		String filename = file.getOriginalFilename();
		if (filename == null) {
			return "";
		}
		int unixSep = filename.lastIndexOf("/");
		int winSep = filename.lastIndexOf("\\");
		int pos = (winSep > unixSep ? winSep : unixSep);
		return pos != -1 ? filename.substring(pos + 1) : filename;
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName) {

		int unixSep = fileName.lastIndexOf(".");
		return unixSep != -1 ? fileName.substring(unixSep + 1) : fileName;
	}

	/**
	 * 判断MultipartFile 是否为空
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isEmpty(MultipartFile file) {

		return file == null || file.getSize() == 0;
	}

	/**
	 * 单文件下载
	 * 
	 * @param response
	 * @param file
	 */
	public static void downLoadFile(HttpServletResponse response, File file) {

		try {
			if (file != null && file.exists() && !file.isDirectory()) {
				response.setContentType("application/x-download");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + (new String(file.getName().getBytes("gb2312"), "ISO-8859-1")));
				copyFile(file, response.getOutputStream());
			}
		} catch (Exception e) {
			logger.info("downLoad file error : ", e);
		}
	}

	/**
	 * 单文件下载
	 * 
	 * @param response
	 * @param fileName
	 */
	public static void downLoadFile(HttpServletResponse response, InputStream inputStream, String fileName) {

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + (new String(fileName.getBytes("gb2312"), "ISO-8859-1")));
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] buff = new byte[512];
			while (inputStream.read(buff) != -1) {
				outputStream.write(buff);
			}
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			logger.info("downLoad file error : ", e);
		}
	}

	public static String inputStreamToString(InputStream inputStream) {

		try {
			File file = File.createTempFile("temp", ".txt");
			copyInputStreamToFile(inputStream, file);
			forceDelete(file);
			return readFileToString(file, Charset.forName(DEFAULT_CHART_SET));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
