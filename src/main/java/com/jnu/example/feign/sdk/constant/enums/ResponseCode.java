package com.jnu.example.feign.sdk.constant.enums;

/**
 *  @Author: zy
 *  @Date: 2020/4/14 22:31
 *  @Description: 状态码
 *  #1001～1999 区间表示参数错误
 *  #2001～2999 区间表示用户错误
 *  #3001～3999 区间表示接口异常
 */
public enum ResponseCode {
	/* 成功 */
	SUCCESS(200, "操作成功"),

	/* 无权限 */
	NO_PERMISSION(401, "没有权限"),
	PAGE_NOT_FOUND(404,"页面不存在"),

	/* 默认失败 */
	FAIL(500, "服务器异常"),

	/* 参数错误：1000～1999 */
	PARAM_NOT_VALID(1001, "参数无效"),
	PARAM_IS_BLANK(1002, "参数为空"),
	PARAM_TYPE_ERROR(1003, "参数类型错误"),
	PARAM_NOT_COMPLETE(1004, "参数缺失"),

	/* 用户错误 */
	USER_NOT_LOGIN(2001, "用户未登录"),
	USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
	USER_CREDENTIALS_ERROR(2003, "密码错误"),
	USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
	USER_ACCOUNT_DISABLE(2005, "账号不可用"),
	USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
	USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
	USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
	USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
	TOKEN_EXPIRED(2101,"token过期"),
	TOKEN_INVALID(2102,"token无效"),
	SIGNATURE_ERROR(2103,"签名失败"),
	AUTHENTICATION_FAIL(2104,"认证失败"),
	THREE_AUTHORIZATION_CODE_ERROR(2105,"第三方授权码错误"),

	/* 接口异常 */
	FILE_UPLOAD_FAIL(3001, "文件上传失败"),
	HTTP_CONTENT_TYPE_ERROR(3002,"HTTP请求content-type错误"),
	HTTP_REQUEST_METHOD_NOT_SUPPORTED(3003,"不支持的HTTP请求方法"),
	RRQUEST_PARAMETER_MISSING(3004,"请求参数缺失"),
	PATH_VARIABLE_MISSING(3005,"路径参数缺失"),
	JSON_PARSE_ERROR(3006,"JSON解析异常");

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 状态消息
	 */
	private String message;

	/**
	 * @author: zy
	 * @description: 构造函数
	 * @date: 2020/3/1 20:31
	 * @param :
	 * @return :
	 */
	ResponseCode(Integer code, String message)  {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public ResponseCode setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResponseCode setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * @author: zy
	 * @description: 根据code获取message
	 * @date: 2020/3/1 20:31
	 * @param :
	 * @return :
	 */
	public static String getMessage(Integer code) {
		for (ResponseCode ele : values()) {
			if (ele.getCode().equals(code)) {
				return ele.getMessage();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("{code:%d;msg:%s}",code,message);
	}
}
