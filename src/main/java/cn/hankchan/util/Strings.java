package cn.hankchan.util;

/**
 * 字符串工具类
 * @author hankchan
 * 2017年6月20日 - 下午1:22:32
 */
public class Strings {


	/**
	 * 如果所有参数都不为空，返回true。否则false
	 * @param strs 参数列表
	 * @return 所有参数都不为空，返回true，否则false
	 */
	public static boolean areNotEmpty(String ... strs) {
		boolean result = true;
		if(strs == null || strs.length == 0) {
			return false;
		} else {
			for(String str : strs) {
				if(isNullOrEmpty(str)) {
					return false;
				}
			}
		}
		return result;
	}

	/**
	 * 检查字符串是否为空
	 * @param str 字符串
	 * @return 检查字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		return str.isEmpty() ? true : false;
	}

	/**
	 * 如果str是null或者空字符串，返回true。否则返回false
	 * @param str 字符串
	 * @return 如果str是null或者空字符串，返回true。否则返回false
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	/**
	 * 如果str是null，返回空字符串。否则返回它本身
	 * @param str 字符串
	 * @return 如果str是null，返回空字符串。否则返回它本身
	 */
	public static String nullToEmpty(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 如果str是空字符串，返回null。否则返回它本身
	 * @param str 字符串
	 * @return 如果str是空字符串，返回null。否则返回它本身
	 */
	public static String emptyToNull(String str) {
		if(str == null) {
			return null;
		}
		return str.isEmpty() ? null : str;
	}
}
