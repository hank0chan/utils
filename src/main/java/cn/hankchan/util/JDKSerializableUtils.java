package cn.hankchan.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Java JDK serialize utils
 * @author hankChan
 * 17:09:11 - 14 Apr 2017
 */
public class JDKSerializableUtils {

	/**
	 * serialize
	 * @param obj serialize target object
	 * @return byte[] after serialize success. otherwise return null
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
	 * unserialize
	 * @param bytes target byte[]
	 * @return object after unserialize success. otherwise return null
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
