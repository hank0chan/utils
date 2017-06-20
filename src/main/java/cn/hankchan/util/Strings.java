package cn.hankchan.util;

/**
 * 
 * @author hankchan
 * 2017年6月20日 - 下午1:22:32
 */
public class Strings {

	/**
	 * return true if str is null or empty string. otherwise false
	 * @param str
	 * @return true if str is null or empty string. otherwise false
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * return empty string if property is null. otherwise return property.
	 * @param property
	 * @return empty string if property is null. otherwise return property.
	 */
	public static String nullToEmpty(String property) {
		if(property == null) {
			return "";
		}
		return property;
	}
	
}
