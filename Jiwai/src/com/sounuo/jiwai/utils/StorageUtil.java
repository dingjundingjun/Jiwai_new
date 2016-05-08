package com.sounuo.jiwai.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

import com.sounuo.jiwai.R;

public class StorageUtil {

    private static final int ERROR = -1;

    /**
     * 获取SD卡文件
     *
     * @param
     * @param
     * @return SD卡文件
     */
    public final static File getSDCardFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取SD卡状态
     * @return SD卡状态
     */
    public static String getSDCardState() {
        return Environment.getExternalStorageState();
    }

    /**
     * flash是否存在
     */
    public static boolean externalMemoryAvailable() {
        return getSDCardState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取flash剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取flash剩余总空间
     *
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 判断SD卡是否可用
     * @return boolean 放回Sd卡是否可用
     */
    public static boolean isExistSDCard() {
        if (StorageUtil.externalMemoryAvailable()) {
            return true;
        }
        return false;
    }


    /**
     * 获取SD卡路径
     * @return
     */
    public static String getLocalPath() {
        return getSDCardFile().getAbsolutePath()+ "/";
    }

    /**
     * 获取Data目录路径
     * @return Data目录路径
     */
    public static String getDataPath() {
        return Environment.getDataDirectory().toString();
    }



}
