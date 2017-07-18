package cn.hankchan.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 二位数组工具类
 * @author hankchan
 * 2017年6月29日 - 上午11:17:50
 */
public class ArraysUtils {

	/**
	 * 二维float数组转为byte数组
	 * @param floatArrays 二维数组
	 * @return 成功返回byte数组，否则返回null
	 */
	public static byte[] floatArrays2Byte(float[][] floatArrays) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(floatArrays);
			oos.flush();
			oos.close();
			bytes = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
	
	/**
	 * byte数组转为float二维数组
	 * @param bytes byte数组
	 * @return 成功返回float二维数组，否则返回null
	 */
	public static float[][] byte2FloatArrays(byte[] bytes) {
		float[][] floatArrays = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			floatArrays = (float[][]) ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return floatArrays;
	}
}
