package com.base.util.upload;

import java.io.Serializable;

/**
 * FTP服务器配置类
 */
public class FtpProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * FTP服务器地址
	 */
	private String host;

	/**
	 * FTP服务器端口号
	 */
	private Integer port;

	/**
	 * FTP登录帐号
	 */
	private String username;

	/**
	 * FTP登录密码
	 */
	private String password;

	public FtpProperty() {
	}

	public FtpProperty(String host, Integer port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
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