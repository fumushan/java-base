package com.base.util.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LoginUtil {

	// 创建本地线程变量
	private final static ThreadLocal<LoginUser> localUser = new ThreadLocal<>();

	public final static String LOGIN_USER = "SESSION_LOGIN_USER";

	public final static String LOGIN_TOKEN = "SESSION_LOGIN_TOKEN";

	/**
	 * 设置LoginUser信息到Session中
	 */
	public static void setLoginUser(HttpServletRequest request, LoginUser loginUser) {
		localUser.set(loginUser);
		request.getSession().setAttribute(LOGIN_USER, loginUser);
	}

	/**
	 * 从Session中获取LoginUser信息
	 */
	public static LoginUser getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LOGIN_USER);
		localUser.set(loginUser);
		return loginUser;
	}

	/**
	 * 从Session中获取LoginUser信息
	 */
	public static LoginUser getLoginUser() {
		if (localUser.get() == null) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LOGIN_USER);
			localUser.set(loginUser);
		}
		return localUser.get();
	}

	/**
	 * 从Session中删除LoginUser信息
	 */
	public static void removeLoginUser(HttpServletRequest request) {
		request.getSession().invalidate();
	}

	/**
	 * 设置Token
	 */
	public static void setToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_TOKEN, session.getId());
	}

	/**
	 * 获取Token
	 */
	public static String getToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute(LOGIN_TOKEN);
	}

	/**
	 * 获取请求的request
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取请求的response
	 */
	public static HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 判断是否AJAX请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

}
