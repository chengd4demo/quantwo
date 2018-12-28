package com.qt.air.cleaner.vo.common;

/**
 * @ClassName: RetResponse
 * @Description: TODO
 * @author MaBin
 * @date 2017年8月14日 下午5:25:20
 * 
 */
public class RetResponse {

	private final static String SUCCESS = "Successful";

	public static <T> RetResult<T> makeOKRsp() {
		return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS);
	}

	public static <T> RetResult<T> makeOKRsp(T data) {
		return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setData(data);
	}

	public static <T> RetResult<T> makeErrRsp(String message) {
		return new RetResult<T>().setCode(RetCode.FAIL).setMsg(message);
	}

	public static <T> RetResult<T> makeRsp(int code, String msg) {
		return new RetResult<T>().setCode(code).setMsg(msg);
	}

	public static <T> RetResult<T> makeRsp(int code, String msg, T data) {
		return new RetResult<T>().setCode(code).setMsg(msg).setData(data);
	}
}
