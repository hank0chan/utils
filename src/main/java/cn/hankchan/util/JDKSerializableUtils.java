package cn.hankchan.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Java JDK 序列化工具类
 * @author hankChan
 * 17:09:11 - 14 Apr 2017
 */
public class JDKSerializableUtils {

	/**
	 * 序列化
	 * @param obj 序列化目标对象
	 * @return byte[] 序列化成功返回byte数组，否则返回null
	 */
	public static byte[] serialize(Object obj) {
		if(obj == null) {
			return null;
		}
		ObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream(1024);
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(objectOutputStream, byteArrayOutputStream);
		}
		return null;
	}

	/**
	 * 反序列化
	 * @param bytes byte数组
	 * @return object 反序列化成功返回对象，否则返回null
	 */
	public static Object unserialize(byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			try {
				return objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(objectInputStream, byteArrayInputStream);
		}
		return null;
	}

	/**
	 * 关闭资源（用于反序列化后）
	 * @param objectInputStream
	 * @param byteArrayInputStream
	 */
	private static void close(ObjectInputStream objectInputStream, 
			ByteArrayInputStream byteArrayInputStream) {
		try {
			if(objectInputStream != null) {
				objectInputStream.close();
			}
			if(byteArrayInputStream != null) {
				byteArrayInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭资源（用于序列化后）
	 * @param objectOutputStream
	 * @param byteArrayOutputStream
	 */
	private static void close(ObjectOutputStream objectOutputStream, 
			ByteArrayOutputStream byteArrayOutputStream) {
		try {
			if(byteArrayOutputStream != null) {
				byteArrayOutputStream.close();	
			}
			if(objectOutputStream != null) {
				objectOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
