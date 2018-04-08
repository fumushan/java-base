package com.base.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // 通用ID

	private String token; // 通行证

	private Integer flag;// 数据标识： 0_禁用，1_启用

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created;// 创建时间

	private String creator;// 创建人

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modified;// 修改时间

	private String modifier;// 修改人

	private Integer pageNum;// 分页功能--当前页码

	private Integer pageSize;// 分页功能--每页记录条数

	private String sort; // 分页功能--排序

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}