package cn.hankchan.util;

import com.google.common.io.Files;

import java.io.*;

/**
 * 流操作工具类
 * @author hankChan
 *    2017年8月10日
 */
public class IOUtils {

	/**
	 * 删除文件
	 * @param fileName 文件完整路径
	 */
	public static void deleteFile(String fileName) {
		(new File(fileName)).delete();
	}

	/**
	 * byte Array转为File
	 * @param bytes 二进制流
	 * @param fileName 文件全路径名
	 * @return 成功返回File，否则null
	 * @throws IOException exception
	 */
	public static File bytes2File(byte[] bytes, String fileName) throws IOException {
		File file = new File(fileName);
		Files.write(bytes, file);
		return file;
	}

	/**
	 * File转为byte Array
	 * @param fileName 文件全路径名
	 * @return 成功返回byte二进制流，否则null
	 */
	public static byte[] file2Bytes(String fileName) {
		byte[] result = null;
		InputStream inputStream = null;
		try {
			File file = new File(fileName);
			inputStream = new FileInputStream(file);
			result = new byte[inputStream.available()];
			inputStream.read(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 从InputStream中获取byte Array
	 * @param is 输入流
	 * @return byte Array
	 */
	public static byte[] getBytes(InputStream is){  
		byte[] buffer = null;
		ByteArrayOutputStream bos=null;
		try {  
			bos = new ByteArrayOutputStream(1000);  
			byte[] b = new byte[1000];  
			int n;  
			while ((n = is.read(b)) != -1) {  
				bos.write(b, 0, n);  
			}  
			buffer = bos.toByteArray();  
		} catch (IOException e) {
			e.printStackTrace();  
		}finally {
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			if(bos!=null){  
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  
		return buffer;  
	}

	/**
	 * 从InputStream中获取指定编码格式的字符串
	 * @param is InputStream
	 * @param code 编码格式
	 * @return 字符串
	 */
	public static String getString(InputStream is,String code){
		try {
			return new String(getBytes(is),code);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new String(getBytes(is));
		}
	}
}
