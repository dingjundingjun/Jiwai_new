package com.sounuo.jiwai.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.util.Log;

public class FileUtil {
	private static final String TAG = "FileUtil";

	// 拷贝文件
	public static void copyFile(String oldPath, String newPath) {
		try {
			File fromFile = new File(oldPath);
			File toFile = new File(newPath);
			if (oldPath.equals(newPath)){
				return;
			}
			if (fromFile.exists()) { // 文件存在时
				FileInputStream fosfrom = new FileInputStream(fromFile);
				FileOutputStream fosto = new FileOutputStream(toFile);
				fosto.flush();
				byte bt[] = new byte[1024 * 5];
				int c;
				while ((c = fosfrom.read(bt)) > 0) {
					fosto.write(bt, 0, c);
				}
				fosto.getFD().sync();
				fosfrom.close();
				fosto.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
	
	public static void deleteFile(String path){
		File file=new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}
