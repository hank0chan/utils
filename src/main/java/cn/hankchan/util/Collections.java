package cn.hankchan.util;

import java.util.ArrayList;
import java.util.Collection;
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
	 * 判断Map集合是否null或者空
	 * @param map Map集合
	 * @param <K> k 键
	 * @param <V> v 值
	 * @return 集合为null或者空，返回true。否则返回false
	 */
	public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}
	
	/**
	 * 判断Collection集合是否null或者空
	 * @param collection Collection集合
	 * @param <T> t 值
	 * @return 集合为null或者空，返回true。否则返回false
	 */
	public static <T> boolean isNullOrEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}
	
	/**
	 * 如果Map集合不为null，返回自身，否则返回空Map集合
	 * @param map map
	 * @param <K> k 键
	 * @param <V> v 值
	 * @return 如果Map集合不为null，返回自身，否则返回空Map集合
	 */
	public static <K, V> Map<K, V> nullToEmpty(Map<K, V> map) {
		return map == null ? new HashMap<>() : map;
	}
	
	/**
	 * 如果List集合不为null，返回自身，否则返回空List集合
	 * @param list list
	 * @param <T> t 值
	 * @return 如果List集合不为null，返回自身，否则返回空List集合
	 */
	public static <T> List<T> nullToEmpty(List<T> list) {
		return list == null ? new ArrayList<>() : list;
	}
	
	/**
	 * 如果List集合不为空，返回自身，否则返回null
	 * @param list list
	 * @param <T> t 值
	 * @return 如果List集合不为空，返回自身，否则返回null
	 */
	public static <T> List<T> emptyToNull(List<T> list) {
		if(list == null) {
			return null;
		}
		return list.isEmpty() ? null : list;
	}
	
	/**
	 * 如果Map集合不为空，返回自身，否则返回null
	 * @param map map
	 * @param <K> k 键
	 * @param <V> v 值
	 * @return 如果Map集合不为空，返回自身，否则返回null
	 */
	public static <K, V> Map<K, V> emptyToNull(Map<K, V> map) {
		if(map == null) {
			return null;
		}
		return map.isEmpty() ? null : map;
	}
}
