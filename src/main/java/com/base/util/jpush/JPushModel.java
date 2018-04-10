package com.base.util.jpush;

public class JPushModel {
	private String status; // 0表示OK，表示Fail
	private String msg;
	private Object data; // 可能是对象，也可能是列表

	public JPushModel(String status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
