package cn.hankchan.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展的集合操作类
 * @author hankchan
 * 2017年6月20日 - 下午3:04:29
 */
public class Collections {

	/**
	 * 如果Map集合不为null，返回自身，否则返回空Map集合
	 * @param obj obj
	 * @param <K> k
	 * @param <V> v
	 * @return 如果Map集合不为null，返回自身，否则返回空Map集合
	 */
	public static <K, V> Map<K, V> nullToEmpty(Map<K, V> obj) {
		if(obj == null) {
			return new HashMap<>();
		}
		return obj;
	}
	
	/**
	 * 如果List集合不为null，返回自身，否则返回空List集合
	 * @param obj obj
	 * @param <T> t
	 * @return 如果List集合不为null，返回自身，否则返回空List集合
	 */
	public static <T> List<T> nullToEmpty(List<T> obj) {
		if(obj == null) {
			return new ArrayList<T>();
		}
		return obj;
	}
	
	/**
	 * 如果List集合不为空，返回自身，否则返回null
	 * @param obj obj
	 * @param <T> t
	 * @return 如果List集合不为空，返回自身，否则返回null
	 */
	public static <T> List<T> emptyToNull(List<T> obj) {
		if(obj == null) {
			return null;
		}
		return obj.isEmpty() ? null : obj;
	}
	
	/**
	 * 如果Map集合不为空，返回自身，否则返回null
	 * @param obj obj
	 * @param <K> k
	 * @param <V> v
	 * @return 如果Map集合不为空，返回自身，否则返回null
	 */
	public static <K, V> Map<K, V> emptyToNull(Map<K, V> obj) {
		if(obj == null) {
			return null;
		}
		return obj.isEmpty() ? null : obj;
	}
}
