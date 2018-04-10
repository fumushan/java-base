package com.base.util.excel.model;

import com.base.util.excel.annotation.ExcelField;

public class UserModel {

	@ExcelField(title = "姓名", order = 1)
	private String name;

	@ExcelField(title = "账号", order = 2)
	private String username;

	@ExcelField(title = "密码", order = 3)
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
