package com.cynovo.sirius.util;

import android.annotation.SuppressLint;


/**
 * 常量类
 * 
 * @author Feel
 */
@SuppressLint("SdCardPath")
public class CynovoConstant {
	/**
	 * 日志和数据存储的目录
	 */
	public static final String STORE_BASE_PATH = "/mnt/sdcard/DCIM/";
	/**
	 * 备份数据存储的目录
	 */
	public static final String STORE_BACKUP_PATH = "/mnt/sdcard/DCIM/DB_IMAGE/";
	public static final String STORE_BACKUP_NAME = "backup.zip";
	public static final String STORE_BACKUP_FOLDER = "/mnt/sdcard/DCIM/DB_IMAGE";
	
//	public static final String STORE_BUSINESSDATA_BACKUP_FOLDER = "/mnt/sdcard/DCIM/BusinessData/";
	public static final String STORE_BUSINESSDATA_BACKUP_PRODUCT = "/mnt/sdcard/DCIM/product.xls";
	public static final String STORE_BUSINESSDATA_EXPORT_SALES = "/mnt/sdcard/DCIM/";
	
	
	/**
	 * 数据库名字
	 */
	public static final String DB_NAME = "cloud_pos_par10.db";
	/**
	 * 数据库存储的完整路径
	 */
	public static final String DB_FULL_STORE_PATH = "/mnt/sdcard/DCIM/cloud_pos_par10.db";
}
