package com.base.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.base.util.excel.handle.ExcelHeader;
import com.base.util.excel.handle.ExcelTemplate;
import com.base.util.excel.util.MultiTitleUtils;
import com.base.util.excel.util.Utils;

public class ExcelUtils {

	static private ExcelUtils excelUtils = new ExcelUtils();

	private ExcelUtils() {
	}

	public static ExcelUtils getInstance() {
		return excelUtils;
	}

	/*----------------------------------------读取Excel操作基于注解映射---------------------------------------------*/
	/* 一. 操作流程 ： */
	/* 1) 读取表头信息,与给出的Class类注解匹配 */
	/* 2) 读取表头下面的数据内容, 按行读取, 并映射至java对象 */
	/* 二. 参数说明 */
	/* *) excelPath => 目标Excel路径 */
	/* *) InputStream => 目标Excel文件流 */
	/* *) clazz => java映射对象 */
	/* *) offsetLine => 开始读取行坐标(默认0) */
	/* *) limitLine => 最大读取行数(默认表尾) */
	/* *) sheetIndex => Sheet索引(默认0) */

	public <T> List<T> readExcel2Objects(String excelPath, Class<T> clazz, int offsetLine, int limitLine,
			int sheetIndex) throws Exception {
		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		return readExcel2BeanHandler(workbook, clazz, offsetLine, limitLine, sheetIndex);
	}

	public <T> List<T> readExcel2Objects(InputStream is, Class<T> clazz, int offsetLine, int limitLine, int sheetIndex)
			throws Exception {
		Workbook workbook = WorkbookFactory.create(is);
		return readExcel2BeanHandler(workbook, clazz, offsetLine, limitLine, sheetIndex);
	}

	public <T> List<T> readExcel2Objects(String excelPath, Class<T> clazz, int sheetIndex) throws Exception {
		return readExcel2Objects(excelPath, clazz, 0, Integer.MAX_VALUE, sheetIndex);
	}

	public <T> List<T> readExcel2Objects(String excelPath, Class<T> clazz) throws Exception {
		return readExcel2Objects(excelPath, clazz, 0, Integer.MAX_VALUE, 0);
	}

	public <T> List<T> readExcel2Objects(InputStream is, Class<T> clazz, int sheetIndex) throws Exception {
		return readExcel2Objects(is, clazz, 0, Integer.MAX_VALUE, sheetIndex);
	}

	public <T> List<T> readExcel2Objects(InputStream is, Class<T> clazz) throws Exception {
		return readExcel2Objects(is, clazz, 0, Integer.MAX_VALUE, 0);
	}

	private <T> List<T> readExcel2BeanHandler(Workbook workbook, Class<T> clazz, int offsetLine, int limitLine,
			int sheetIndex) throws Exception {

		Sheet sheet = workbook.getSheetAt(sheetIndex);
		Row row = sheet.getRow(offsetLine);
		List<T> list = new ArrayList<T>();
		Map<Integer, ExcelHeader> maps = Utils.getHeaderMap(row, clazz);
		if (maps == null || maps.size() <= 0)
			throw new RuntimeException("要读取的Excel的格式不正确，检查是否设定了合适的行");
		int maxLine = sheet.getLastRowNum() > (offsetLine + limitLine) ? (offsetLine + limitLine)
				: sheet.getLastRowNum();
		for (int i = offsetLine + 1; i <= maxLine; i++) {
			row = sheet.getRow(i);
			T obj = clazz.newInstance();
			for (Cell cell : row) {
				int ci = cell.getColumnIndex();
				ExcelHeader header = maps.get(ci);
				if (null == header)
					continue;
				String filed = header.getFiled();
				String val = Utils.getCellValue(cell);
				Object value = Utils.str2TargetClass(val, header.getFiledClazz());
				BeanUtils.copyProperty(obj, filed, value);
			}
			list.add(obj);
		}
		return list;
	}

	/*----------------------------------------读取Excel操作无映射--------------------------------------------------*/
	/* 一. 操作流程 ： */
	/*
	 * *) 按行读取Excel文件,存储形式为 Cell->String => Row->List<Cell> => Excel->List<Row>
	 */
	/* 二. 参数说明 */
	/* *) excelPath => 目标Excel路径 */
	/* *) InputStream => 目标Excel文件流 */
	/* *) offsetLine => 开始读取行坐标(默认0) */
	/* *) limitLine => 最大读取行数(默认表尾) */
	/* *) sheetIndex => Sheet索引(默认0) */

	public List<List<String>> readExcel2List(String excelPath, int offsetLine, int limitLine, int sheetIndex)
			throws Exception {

		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		return readExcel2ObjectsHandler(workbook, offsetLine, limitLine, sheetIndex);
	}

	public List<List<String>> readExcel2List(InputStream is, int offsetLine, int limitLine, int sheetIndex)
			throws Exception {

		Workbook workbook = WorkbookFactory.create(is);
		return readExcel2ObjectsHandler(workbook, offsetLine, limitLine, sheetIndex);
	}

	public List<List<String>> readExcel2List(String excelPath, int offsetLine) throws Exception {

		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		return readExcel2ObjectsHandler(workbook, offsetLine, Integer.MAX_VALUE, 0);
	}

	public List<List<String>> readExcel2List(InputStream is, int offsetLine) throws Exception {

		Workbook workbook = WorkbookFactory.create(is);
		return readExcel2ObjectsHandler(workbook, offsetLine, Integer.MAX_VALUE, 0);
	}

	public List<List<String>> readExcel2List(String excelPath) throws Exception {

		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		return readExcel2ObjectsHandler(workbook, 0, Integer.MAX_VALUE, 0);
	}

	public List<List<String>> readExcel2List(InputStream is) throws Exception {

		Workbook workbook = WorkbookFactory.create(is);
		return readExcel2ObjectsHandler(workbook, 0, Integer.MAX_VALUE, 0);
	}

	private List<List<String>> readExcel2ObjectsHandler(Workbook workbook, int offsetLine, int limitLine,
			int sheetIndex) throws Exception {

		List<List<String>> list = new ArrayList<>();
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		int maxLine = sheet.getLastRowNum() > (offsetLine + limitLine) ? (offsetLine + limitLine)
				: sheet.getLastRowNum();
		for (int i = offsetLine; i <= maxLine; i++) {
			List<String> rows = new ArrayList<>();
			Row row = sheet.getRow(i);
			for (Cell cell : row) {
				String val = Utils.getCellValue(cell);
				rows.add(val);
			}
			list.add(rows);
		}
		return list;
	}

	/*--------------------------------------------基于模板、注解导出excel-------------------------------------------*/
	/* 一. 操作流程 ： */
	/* 1) 初始化模板 */
	/* 2) 根据Java对象映射表头 */
	/* 3) 写入数据内容 */
	/* 二. 参数说明 */
	/* *) templatePath => 模板路径 */
	/* *) sheetIndex => Sheet索引(默认0) */
	/* *) data => 导出内容List集合 */
	/* *) extendMap => 扩展内容Map(具体就是key匹配替换模板#key内容) */
	/* *) clazz => 映射对象Class */
	/* *) isWriteHeader => 是否写入表头 */
	/* *) targetPath => 导出文件路径 */
	/* *) os => 导出文件流 */

	public void exportObjects2Excel(String templatePath, int sheetIndex, List<?> data, Map<String, String> extendMap,
			String targetPath) throws Exception {

		exportExcelByModuleHandler(templatePath, sheetIndex, data, extendMap).write2File(targetPath);
	}

	public void exportObjects2Excel(String templatePath, int sheetIndex, List<?> data, Map<String, String> extendMap,
			OutputStream os) throws Exception {

		exportExcelByModuleHandler(templatePath, sheetIndex, data, extendMap).write2Stream(os);
	}

	public void exportObjects2Excel(String templatePath, List<?> data, Map<String, String> extendMap, String targetPath)
			throws Exception {

		exportObjects2Excel(templatePath, 0, data, extendMap, targetPath);
	}

	public void exportObjects2Excel(String templatePath, List<?> data, Map<String, String> extendMap, OutputStream os)
			throws Exception {

		exportObjects2Excel(templatePath, 0, data, extendMap, os);
	}

	public void exportObjects2Excel(String templatePath, List<?> data, String targetPath) throws Exception {

		exportObjects2Excel(templatePath, 0, data, null, targetPath);
	}

	public void exportObjects2Excel(String templatePath, List<?> data, OutputStream os) throws Exception {

		exportObjects2Excel(templatePath, 0, data, null, os);
	}

	private ExcelTemplate exportExcelByModuleHandler(String templatePath, int sheetIndex, List<?> data,
			Map<String, String> extendMap) throws Exception {

		Class<?> clazz = this.getGenericType(data);
		ExcelTemplate templates = ExcelTemplate.getInstance(templatePath, sheetIndex);
		templates.extendData(extendMap);
		List<ExcelHeader> headers = Utils.getHeaderList(clazz);
		templates.createNewRow();
		for (ExcelHeader header : headers) {
			templates.createCell(header.getTitle(), null);
		}
		for (Object object : data) {
			templates.createNewRow();
			templates.insertSerial(null);
			for (ExcelHeader header : headers) {
				templates.createCell(BeanUtils.getProperty(object, header.getFiled()), null);
			}
		}
		return templates;
	}

	/*---------------------------------------基于模板、注解导出Map数据----------------------------------------------*/
	/* 一. 操作流程 ： */
	/* 1) 初始化模板 */
	/* 2) 根据Java对象映射表头 */
	/* 3) 写入数据内容 */
	/* 二. 参数说明 */
	/* *) templatePath => 模板路径 */
	/* *) sheetIndex => Sheet索引(默认0) */
	/* *) data => 导出内容Map集合 */
	/* *) extendMap => 扩展内容Map(具体就是key匹配替换模板#key内容) */
	/* *) clazz => 映射对象Class<?> */
	/* *) isWriteHeader => 是否写入表头 */
	/* *) targetPath => 导出文件路径 */
	/* *) os => 导出文件流 */
	public void exportObject2Excel(String templatePath, int sheetIndex, Map<String, List<Object>> data,
			Map<String, String> extendMap, Class<?> clazz, boolean isWriteHeader, String targetPath) throws Exception {

		exportExcelByModuleHandler(templatePath, sheetIndex, data, extendMap, clazz, isWriteHeader)
				.write2File(targetPath);
	}

	public void exportObject2Excel(String templatePath, int sheetIndex, Map<String, List<Object>> data,
			Map<String, String> extendMap, Class<?> clazz, boolean isWriteHeader, OutputStream os) throws Exception {

		exportExcelByModuleHandler(templatePath, sheetIndex, data, extendMap, clazz, isWriteHeader).write2Stream(os);
	}

	public void exportObject2Excel(String templatePath, Map<String, List<Object>> data, Map<String, String> extendMap,
			Class<?> clazz, String targetPath) throws Exception {

		exportExcelByModuleHandler(templatePath, 0, data, extendMap, clazz, false).write2File(targetPath);
	}

	public void exportObject2Excel(String templatePath, Map<String, List<Object>> data, Map<String, String> extendMap,
			Class<?> clazz, OutputStream os) throws Exception {

		exportExcelByModuleHandler(templatePath, 0, data, extendMap, clazz, false).write2Stream(os);
	}

	private ExcelTemplate exportExcelByModuleHandler(String templatePath, int sheetIndex,
			Map<String, List<Object>> data, Map<String, String> extendMap, Class<?> clazz, boolean isWriteHeader)
			throws Exception {

		ExcelTemplate templates = ExcelTemplate.getInstance(templatePath, sheetIndex);
		templates.extendData(extendMap);
		List<ExcelHeader> headers = Utils.getHeaderList(clazz);
		if (isWriteHeader) {
			templates.createNewRow();
			for (ExcelHeader header : headers) {
				templates.createCell(header.getTitle(), null);
			}
		}
		for (Map.Entry<String, List<Object>> entry : data.entrySet()) {
			for (Object object : entry.getValue()) {
				templates.createNewRow();
				templates.insertSerial(entry.getKey());
				for (ExcelHeader header : headers) {
					templates.createCell(BeanUtils.getProperty(object, header.getFiled()), entry.getKey());
				}
			}
		}
		return templates;
	}

	/*----------------------------------------无模板基于注解导出---------------------------------------------------*/
	/**
	 * 无模板基于注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param sheetName
	 *            Sheet索引名(默认0)
	 * @param targetPath
	 *            导出文件路径
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportBean2Excel(List<?> data, String sheetName, String targetPath, String createUser)
			throws Exception {

		FileOutputStream fos = new FileOutputStream(new File(targetPath));
		exportBean2ExcelHandler(data, sheetName, createUser).write(fos);
	}

	/**
	 * 无模板基于注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param sheetName
	 *            Sheet索引名(默认0)
	 * @param os
	 *            输出流
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportBean2Excel(List<?> data, String sheetName, OutputStream os, String createUser) throws Exception {

		exportBean2ExcelHandler(data, sheetName, createUser).write(os);
	}

	/**
	 * 无模板基于注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param sheetName
	 *            Sheet索引名(默认0)
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportBean2Excel(List<?> data, String targetPath, String createUser) throws Exception {

		FileOutputStream fos = new FileOutputStream(new File(targetPath));
		exportBean2ExcelHandler(data, null, createUser).write(fos);
	}

	/**
	 * 无模板基于注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param clazz
	 *            映射对象Class<?>
	 * @param os
	 *            输出流
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, OutputStream os, String createUser) throws Exception {

		exportBean2ExcelHandler(data, null, createUser).write(os);
	}

	private Class<?> getGenericType(List<?> data) {

		if (CollectionUtils.isNotEmpty(data)) {
			return data.get(0).getClass();
		}
		return Object.class;
	}

	/**
	 * 
	 * @param data
	 * @param sheetName
	 * @param createUser
	 * @return
	 * @throws Exception
	 */
	public Workbook exportBean2ExcelHandler(List<?> data, String sheetName, String createUser) throws Exception {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = createSheet(sheetName, workbook);
		int rowIndex = 0;
		if (CollectionUtils.isEmpty(data)) {
			return workbook;
		}
		Class<?> clazz = this.getGenericType(data);
		List<ExcelHeader> headers = Utils.getHeaderList(clazz);
		rowIndex = this.setWorkBookInfo(workbook, sheet, rowIndex, headers.size(), createUser);
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < headers.size(); i++) {
			row.createCell(i).setCellValue(headers.get(i).getTitle());
			if (StringUtils.isNotBlank(headers.get(i).getTitle())) {
				sheet.setColumnWidth(i, headers.get(i).getTitle().length() * 1000);
			}
		}
		rowIndex++;
		Object value;
		for (int i = 0; i < data.size(); i++) {
			row = sheet.createRow(rowIndex);
			value = data.get(i);
			for (int j = 0; j < headers.size(); j++) {
				Cell cell = row.createCell(j);// .setCellValue(BeanUtils.getProperty(value,
												// headers.get(j).getFiled()));
				this.setCellValue(headers.get(j), cell, value);
			}
			rowIndex++;
		}
		return workbook;
	}

	private void setCellValue(ExcelHeader header, Cell cell, Object value) throws Exception {

		Object filedValue = PropertyUtils.getProperty(value, header.getFiled());
		if (filedValue == null) {
			return;
		}
		if (header.getFiledClazz() == Date.class) {
			Date date = (Date) filedValue;
			cell.setCellValue(DateFormatUtils.format(date, header.getPattern()));
		} else {
			cell.setCellValue(filedValue.toString());
		}
	}

	/*-----------------------------------------无模板无注解导出----------------------------------------------------*/
	/* 一. 操作流程 ： */
	/* 1) 写入表头内容(可选) */
	/* 2) 写入数据内容 */
	/* 二. 参数说明 */
	/* *) data => 导出内容List集合 */
	/* *) header => 表头集合,有则写,无则不写 */
	/* *) sheetName => Sheet索引名(默认0) */
	/* *) isXSSF => 是否Excel2007以上 */
	/* *) targetPath => 导出文件路径 */
	/* *) os => 导出文件流 */

	/**
	 * 无模板无注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param header
	 *            表头集合
	 * @param title
	 *            field
	 * @param sheetName
	 *            Sheet索引名(默认0)
	 * @param targetPath
	 *            导出文件路径
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, String[] header, String[] title, String sheetName, String targetPath,
			String createUser) throws Exception {

		FileOutputStream os = new FileOutputStream(new File(targetPath));
		exportExcelNoModuleHandler(data, header, title, sheetName, createUser).write(os);
	}

	/**
	 * 无模板无注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param header
	 *            表头集合
	 * @param title
	 *            field
	 * @param sheetName
	 *            Sheet索引名(默认0)
	 * @param os
	 *            输出流
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, String[] header, String[] title, String sheetName,
			HttpServletResponse response, String createUser) throws Exception {

		exportExcelNoModuleHandler(data, header, title, sheetName, createUser).write(this.getOutPutStream(response));
	}

	/**
	 * 无模板无注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param header
	 *            表头集合
	 * @param title
	 *            field
	 * @param targetPath
	 *            导出文件路径
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, String[] header, String[] title, String targetPath, String createUser)
			throws Exception {

		FileOutputStream os = new FileOutputStream(new File(targetPath));
		exportExcelNoModuleHandler(data, header, title, null, createUser).write(os);
	}

	/**
	 * 无模板无注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param header
	 *            表头集合
	 * @param title
	 *            field
	 * @param os
	 *            输出流
	 * @param createUser
	 *            创建人
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, String[] header, String[] title, HttpServletResponse response,
			String createUser) throws Exception {

		exportExcelNoModuleHandler(data, header, title, null, createUser).write(this.getOutPutStream(response));
	}

	/**
	 * 无模板无注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param os
	 *            输出流
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, HttpServletResponse response) throws Exception {

		exportExcelNoModuleHandler(data, null, null, null, null).write(this.getOutPutStream(response));
	}

	/**
	 * 工作薄写出
	 * 
	 * @param workbook
	 * @param response
	 * @throws IOException
	 */
	public void writeWorkBook(Workbook workbook, HttpServletResponse response) throws IOException {

		if (workbook != null) {
			workbook.write(this.getOutPutStream(response));
		}
	}

	/**
	 * 二维表头 导出
	 * 
	 * @param data
	 *            数据
	 * @param largeTitle
	 *            大标题
	 * @param createUser
	 *            创建人
	 * @param mergeTitle
	 *            合并标题
	 * @param title
	 *            标题
	 * @param field
	 *            数据对应字段名
	 * @param sheetName
	 */
	public void exportObjects2Excel(List<?> data, String largeTitle, String createUser, MultiTitleUtils[] mergeTitle,
			String[] title, String[] field, HttpServletResponse response) throws Exception {

		exportMultiTitleExcelNoModule(data, largeTitle, createUser, mergeTitle, title, field, null)
				.write(this.getOutPutStream(response));
	}

	/**
	 * 无模板无注解导出
	 * 
	 * @param data
	 *            导出内容List集合
	 * @param targetPath
	 *            导出文件路径
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, String targetPath, String createUser) throws Exception {

		FileOutputStream os = new FileOutputStream(new File(targetPath));
		exportExcelNoModuleHandler(data, null, null, null, createUser).write(os);
	}

	private OutputStream getOutPutStream(HttpServletResponse response) throws IOException {

		response.setContentType("application/x-xls ");
		return response.getOutputStream();
	}

	/**]
	 * 水平样式
	 * @param workbook
	 * @return
	 */
	public CellStyle getCellStyle(Workbook workbook) {

		CellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}
	
	/**
	 * 垂直居中
	 * @param workbook
	 * @return
	 */
	public CellStyle getVerticalCellStyle(Workbook workbook) {

		CellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	public Workbook exportExcelNoModuleHandler(List<?> data, String[] header, String[] title, String sheetName,
			String createUser) throws Exception {

		Workbook workbook = new XSSFWorkbook();
		if (CollectionUtils.isEmpty(data)) {
			return workbook;
		}
		Sheet sheet = this.createSheet(sheetName, workbook);
		int rowIndex = 0;
		CellStyle headerSyle = this.getCellStyle(workbook);
		if (null != header && header.length > 0) {
			rowIndex = this.setWorkBookInfo(workbook, sheet, rowIndex, header.length, createUser);
			rowIndex = createHeader(header, sheet, rowIndex, headerSyle);
		}
		return exportWorkBook(data, title, workbook, sheet, rowIndex);
	}

	/**
	 * 二维表头 导出
	 * 
	 * @param data
	 *            数据
	 * @param sheetTitle
	 *            sheet标题
	 * @param createUser
	 *            创建人
	 * @param mergeTitle
	 *            合并标题
	 * @param title
	 *            标题
	 * @param field
	 *            数据对应字段名
	 * @param sheetName
	 */
	public Workbook exportMultiTitleExcelNoModule(List<?> data, String sheetTitle, String createUser,
			MultiTitleUtils[] mergeTitle, String[] title, String[] field, String sheetName) throws Exception {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = this.createSheet(sheetName, workbook);
		int rowIndex = 0;
		CellStyle headerStyle = this.getCellStyle(workbook);
		if (null != title && title.length > 0) {
			int length = title.length;
			rowIndex = this.setWorkBookTitle(workbook, sheet, rowIndex, length, sheetTitle); // 设置表的标题1
			rowIndex = this.setWorkBookInfo(workbook, sheet, rowIndex, length, createUser); // 设置创建人信息2
		}
		rowIndex = createHeader(mergeTitle, title, sheet, rowIndex, headerStyle); // 二维表头34
		return exportWorkBook(data, field, workbook, sheet, rowIndex);
	}

	/**
	 * 二维表头 导出
	 * 
	 * @param data
	 * @param mergeTitle
	 * @param title
	 * @param field
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public Workbook exportMultiTitleExcelNoModule(List<?> data, MultiTitleUtils[] mergeTitle, String[] title,
			String[] field, String sheetName) throws Exception {

		Workbook workbook = new XSSFWorkbook();
		if (CollectionUtils.isEmpty(data)) {
			return workbook;
		}
		Sheet sheet = this.createSheet(sheetName, workbook);
		int rowIndex = 0;
		CellStyle headerStyle = this.getCellStyle(workbook);
		rowIndex = createHeader(mergeTitle, title, sheet, rowIndex, headerStyle); // 二维表头34
		return exportWorkBook(data, field, workbook, sheet, rowIndex);
	}

	/**
	 * 二维表头 导出 无创建人、创建时间等信息
	 * 
	 * @param data
	 *            导出数据
	 * @param title
	 *            对象属性名称
	 * @param field
	 *            对象属性
	 * @param response
	 * @throws Exception
	 */
	public void exportObjects2Excel(List<?> data, MultiTitleUtils[] mergeTitle, String[] title, String[] field,
			HttpServletResponse response) throws Exception {
		exportMultiTitleExcelNoModule(data, mergeTitle, title, field, null).write(this.getOutPutStream(response));
	}

	/**
	 * 创建sheet
	 * 
	 * @param sheetName
	 * @param workbook
	 * @return
	 */
	private Sheet createSheet(String sheetName, Workbook workbook) {

		if (StringUtils.isNotBlank(sheetName)) {
			return workbook.createSheet(sheetName);
		}
		return workbook.createSheet();
	}

	/**
	 * 导出数据
	 * 
	 * @param data
	 * @param title
	 * @param workbook
	 * @param sheet
	 * @param rowIndex
	 * @return
	 */
	public Workbook exportWorkBook(List<?> list, String[] title, Workbook workbook, Sheet sheet, int rowIndex) {

		CellStyle cellSyle = this.getCellStyle(workbook);
		if (CollectionUtils.isEmpty(list)) {
			return workbook;
		}
		Object data = list.get(0);
		if (data.getClass().isArray()) {
			writeArray(list, sheet, rowIndex, cellSyle);
		} else if (data instanceof Collection) {
			writeCollection(list, sheet, rowIndex, cellSyle);
		} else if (data instanceof Map) {
			writeMap(list, title, sheet, rowIndex, cellSyle);
		} else {
			writeDefault(list, sheet, rowIndex, cellSyle);
		}
		return workbook;
	}

	private int writeDefault(List<?> list, Sheet sheet, int rowIndex, CellStyle cellSyle) {

		for (Object object : list) {
			Row row = sheet.createRow(rowIndex);
			setCellValue(row, 0, object.toString(), cellSyle);
			rowIndex++;
		}
		return rowIndex;
	}

	private void writeMap(List<?> list, String[] title, Sheet sheet, int rowIndex, CellStyle cellSyle) {

		for (Object object : list) {
			Row row = sheet.createRow(rowIndex);
			Map<?, ?> items = (Map<?, ?>) object;
			if (title != null) {
				for (int i = 0; i < title.length; i++) {
					setCellValue(row, i, items.get(title[i]), cellSyle);
				}
			} else {
				int j = 0;
				for (Entry<?, ?> entry : items.entrySet()) {
					setCellValue(row, j, entry.getValue(), cellSyle);
					j++;
				}
			}
			rowIndex++;
		}
	}

	private int writeCollection(List<?> list, Sheet sheet, int rowIndex, CellStyle cellSyle) {

		for (Object object : list) {
			Row row = sheet.createRow(rowIndex);
			int j = 0;
			Collection<?> items = (Collection<?>) object;
			for (Object item : items) {
				this.setCellValue(row, j, item.toString(), cellSyle);
				j++;
			}
			rowIndex++;
		}
		return rowIndex;
	}

	private int writeArray(List<?> list, Sheet sheet, int rowIndex, CellStyle cellSyle) {

		for (Object object : list) {
			Row row = sheet.createRow(rowIndex);
			for (int j = 0; j < Array.getLength(object); j++) {
				this.setCellValue(row, j, Array.get(object, j), cellSyle);
			}
			rowIndex++;
		}
		return rowIndex;
	}

	/**
	 * 创建表头
	 * 
	 * @param header
	 * @param sheet
	 * @param rowIndex
	 * @param headerStyle
	 * @return
	 */
	public int createHeader(String[] header, Sheet sheet, int rowIndex, CellStyle headerStyle) {

		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < header.length; i++) {
			Cell cell = row.createCell(i, CellType.STRING);
			// cell.setCellStyle(headerStyle);
			cell.setCellValue(header[i]);
			sheet.setColumnWidth(i, header[i].length() * 1000);
		}
		rowIndex++;
		return rowIndex;
	}

	/**
	 * 二维表头
	 * 
	 * @param header
	 * @param sheet
	 * @param rowIndex
	 * @param headerStyle
	 * @return
	 */
	public int createHeader(MultiTitleUtils[] largeTitle, String[] header, Sheet sheet, int rowIndex,
			CellStyle headerStyle) {

		Row row = sheet.createRow(rowIndex); // 第三行大标题
		int start = 0;
		int end = 0;
		Cell cell = null;
		CellRangeAddress cra = null;
		if(null != largeTitle){
			for (int j = 0; j < largeTitle.length; j++) {
				cell = row.createCell(start, CellType.STRING);
				cell.setCellStyle(headerStyle);
				int length = largeTitle[j].getLength();
				cell.setCellValue(largeTitle[j].getTitle());
				end += length;
				if (length > 1) {
					cra = new CellRangeAddress(rowIndex, rowIndex, start, end - 1);
					sheet.addMergedRegion(cra);
				}
				start = end;
			}
			rowIndex++;
		}

		row = sheet.createRow(rowIndex); // 第四行大标题
		Cell second = null;
		for (int i = 0; i < header.length; i++) {
			second = row.createCell(i, CellType.STRING);
			second.setCellStyle(headerStyle);
			second.setCellValue(header[i]);
			sheet.setColumnWidth(i, header[i].length() * 1000);
		}
		rowIndex++;
		return rowIndex;
	}

	/**
	 * 单元格数据
	 * 
	 * @param row
	 * @param index
	 * @param value
	 * @param cellSyle
	 */
	private void setCellValue(Row row, int index, Object value, CellStyle cellSyle) {

		Cell cell = row.createCell(index, CellType.STRING);
		if (value != null) {
			cell.setCellStyle(cellSyle);
			cell.setCellValue(value.toString());
		}
	}

	/**
	 * 设置工作薄信息 导出时间 导出人等
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param headerSize
	 */
	private Integer setWorkBookInfo(Workbook workBook, Sheet sheet, Integer rowIndex, Integer headerSize,
			String createUser) {

		CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, 0, headerSize - 1);
		sheet.addMergedRegion(cra);
		Row row = sheet.createRow(rowIndex);
		row.setHeightInPoints(30);
		Cell cell = row.createCell(0);
		CellStyle style = workBook.createCellStyle();
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setFillForegroundColor(HSSFColorPredefined.LIGHT_TURQUOISE.getIndex());

		Font hssfFont = workBook.createFont();
		hssfFont.setItalic(true);
		hssfFont.setFontHeightInPoints((short) 12);
		hssfFont.setFontName("楷体");
		hssfFont.setBold(true);
		style.setFont(hssfFont);
		cell.setCellStyle(style);
		String now = LocalDateTime.now().toString().replace("T", " ");
		cell.setCellValue("创建人: " + createUser + " 创建时间: " + now.substring(0, now.lastIndexOf(".")));
		rowIndex++;
		return rowIndex;
	}

	/**
	 * 设置表的标题
	 * 
	 * @param workBook
	 * @param sheet
	 * @param rowIndex
	 * @param headerSize
	 * @param createUser
	 * @return
	 */
	public Integer setWorkBookTitle(Workbook workBook, Sheet sheet, Integer rowIndex, Integer headerSize,
			String createTitle) {

		CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, 0, headerSize - 1);
		sheet.addMergedRegion(cra);
		Row row = sheet.createRow(rowIndex);
		row.setHeightInPoints(30);
		Cell cell = row.createCell(0);
		CellStyle style = workBook.createCellStyle();
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(HSSFColorPredefined.LIGHT_TURQUOISE.getIndex());

		Font hssfFont = workBook.createFont();
		hssfFont.setItalic(true);
		hssfFont.setFontHeightInPoints((short) 12);
		hssfFont.setFontName("楷体");
		hssfFont.setBold(true);
		style.setFont(hssfFont);
		cell.setCellStyle(style);
		cell.setCellValue(createTitle);
		rowIndex++;
		return rowIndex;
	}
}