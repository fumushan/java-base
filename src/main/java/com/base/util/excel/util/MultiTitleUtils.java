package com.base.util.excel.util;

import java.io.Serializable;

public class MultiTitleUtils implements Serializable{

	private static final long serialVersionUID = 1L;

	private String title ;
	
	private int length;
	
	public MultiTitleUtils(String title, int length) {
		this.title = title;
		this.length = length;
	}

	public MultiTitleUtils() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	
}
