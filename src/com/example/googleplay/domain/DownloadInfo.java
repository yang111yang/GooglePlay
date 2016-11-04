package com.example.googleplay.domain;

import java.io.File;

import com.example.googleplay.manager.DownloadManager;

import android.os.Environment;

/**
 * 下载对象的封装
 * @author liu
 * @date 2016-11-4
 */
public class DownloadInfo {

	public String id;
	public static String name;
	public String downloadUrl;
	public String packageName;
	public long size;
	public String path;

	public long currentPos; // 当前下载位置
	public int currentState; // 当前下载状态

	public static final String GOOGLE_MARKET = "google_market"; // sd卡根目录文件夹名称
	public static final String DOWNLOAD = "download"; // 子文件夹名称，存放下载文件

	/**
	 * 获取下载进度
	 * 
	 * @return
	 */
	public float getProgress() {
		if (size == 0) {
			return 0;
		}
		float progress = currentPos / (float) size;
		return progress;
	}

	/**
	 * 获取文件下载路径
	 * 
	 * @return
	 */
	public static String getFilePath() {

		StringBuffer sb = new StringBuffer();

		String sdcard = Environment.getExternalStorageDirectory()
				.getAbsolutePath(); // 获取sd卡根目录

		sb.append(sdcard);
		sb.append(File.separator);
		sb.append(GOOGLE_MARKET);
		sb.append(File.separator);
		sb.append(DOWNLOAD);

		if (createDir(sb.toString())) {
			// 文件夹已经存在或者创建完成
			return sb.toString() + File.separator + name + ".apk"; // 返回文件路径
		}

		return null;
	}

	public static boolean createDir(String dir) {
		File file = new File(dir);
		// 如果文件夹不存在或者不是一个文件夹
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		return true; // 文件夹存在
	}

	/**
	 * 从APPInfo中拷贝一个DownloadInfo对象
	 * 
	 * @param info
	 * @return
	 */
	public static DownloadInfo copy(AppInfo info) {
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.id = info.id;
		downloadInfo.name = info.name;
		downloadInfo.downloadUrl = info.downloadUrl;
		downloadInfo.packageName = info.packageName;
		downloadInfo.size = info.size;
		downloadInfo.currentPos = 0;
		downloadInfo.currentState = DownloadManager.STATE_UNDO;
		downloadInfo.path = getFilePath();

		return downloadInfo;
	}

}
