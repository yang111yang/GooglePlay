package com.example.googleplay.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.domain.DownloadInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.http.HttpHelper.HttpResult;
import com.example.googleplay.utils.IOUtils;
import com.example.googleplay.utils.LogUtils;
import com.example.googleplay.utils.UIUtils;

/**
 * 下载管理器-单例 饿汉模式：线程安全，构造方法私有化，推荐使用
 * 
 * @author liu
 * @date 2016-11-4
 */
public class DownloadManager {

	public static final int STATE_UNDO = 0; // 未下载

	public static final int STATE_WATING = 1; // 等待下载

	public static final int STATE_DOWNLOAD = 2; // 正在下载

	public static final int STATE_PAUSE = 3; // 暂停下载

	public static final int STATE_ERROR = 4; // 下载出错

	public static final int STATE_SUCCESS = 5; // 下载成功

	/** 观察者集合 */
	private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();

	/** 下载对象集合 */
	private HashMap<String, DownloadInfo> mDownloadInfoMap = new HashMap<String, DownloadInfo>();

	/** 下载任务集合 */
	private HashMap<String, DownloadTask> mDownloadTaskMap = new HashMap<String, DownloadTask>();

	private static DownloadManager sInstance = new DownloadManager();

	private DownloadManager() {
	}

	public static DownloadManager getInstance() {
		return sInstance;
	}

	/**
	 * 2.注册观察者
	 */
	public void registerObserver(DownloadObserver observer) {
		if (observer != null && !mObservers.contains(observer)) {
			mObservers.add(observer);
		}
	}

	/**
	 * 3.注销观察者
	 */
	public void unregisterObserver(DownloadObserver observer) {
		if (observer != null && mObservers.contains(observer)) {
			mObservers.remove(observer);
		}
	}

	/**
	 * 4.通知下载状态变化
	 */
	public void notifyDownloadStateChanged(DownloadInfo info) {
		for (DownloadObserver observer : mObservers) {
			observer.onDownloadStateChanged(info);
		}
	}

	/**
	 * 5.通知下载进度变化
	 */
	public void notifyDownloadProgressChanged(DownloadInfo info) {
		for (DownloadObserver observer : mObservers) {
			observer.onDownloadProgressChanged(info);
		}
	}

	/**
	 * 下载apk
	 */
	public void download(AppInfo appInfo) {

		if (appInfo != null) {
			DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.id);

			// 如果downloadInfo不为空，表示之前下载过，就无需创建对象，要接着原来的位置继续下载，实现断点续传
			if (downloadInfo == null) { // 如果downloadInfo为空，说明是第一次下载，需要创建downloadInfo对象
				downloadInfo = DownloadInfo.copy(appInfo);
			}

			// 将下载状态更新为等待下载
			downloadInfo.currentState = STATE_WATING;
			// 通知状态发生变化，各观察者根据此通知更新主界面
			notifyDownloadStateChanged(downloadInfo);
			// 将下载的对象保存到集合中
			mDownloadInfoMap.put(downloadInfo.id, downloadInfo);
			// 初始化下载任务
			DownloadTask task = new DownloadTask(downloadInfo);
			// 启动下载任务
			ThreadManager.getThreadPool().excute(task);
			// 将下载任务维护到集合中
			mDownloadTaskMap.put(downloadInfo.id, task);
		}

	}

	/**
	 * 暂停下载
	 */
	public void pause(AppInfo appInfo) {

		if (appInfo != null) {
			DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.id);
			// 如果downloadInfo不为空，说明这是一个等待下载或者正在下载的任务
			if (downloadInfo != null) {
				// 获取downloadInfo的状态
				int state = downloadInfo.currentState;
				// 判断state,如果当前状态是正在下载或者等待下载，才暂停下载
				if (state == STATE_DOWNLOAD || state == STATE_WATING) {
					// 停止当前的下载任务
					DownloadTask task = mDownloadTaskMap.get(downloadInfo.id);
					if (task != null) {
						ThreadManager.getThreadPool().cancel(task);
					}
				}

				// 将下载状态更新为暂停下载
				downloadInfo.currentState = STATE_PAUSE;
				// 通知状态发生变化，各观察者根据此通知更新主界面
				notifyDownloadStateChanged(downloadInfo);

			}

		}
	}

	/**
	 * 安装apk
	 */
	public void install(AppInfo appInfo) {
		DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.id);
		if (downloadInfo != null) {
			// 跳到系统的安装页面进行安装
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
					"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(intent);
		}
	}

	/**
	 * 1.定义下载观察者接口
	 */
	public interface DownloadObserver {

		/**
		 * 当下载状态发生变化时，调用此方法
		 */
		public void onDownloadStateChanged(DownloadInfo info);

		/**
		 * 当下载进度发生变化时，调用此方法
		 */
		public void onDownloadProgressChanged(DownloadInfo info);

	}

	class DownloadTask implements Runnable {

		private DownloadInfo downloadInfo;

		private DownloadTask(DownloadInfo info) {
			this.downloadInfo = info;

		}

		@Override
		public void run() {
			LogUtils.i("开始下载啦");
			// 将下载状态更新为暂停下载
			downloadInfo.currentState = STATE_PAUSE;
			// 通知状态发生变化，各观察者根据此通知更新主界面
			notifyDownloadStateChanged(downloadInfo);

			File file = new File(downloadInfo.path);
			HttpResult httpResult;
			if (!file.exists() || file.length() != downloadInfo.currentPos
					|| downloadInfo.currentPos == 0) {// 文件不存在，文件长度与对象的长度不一致，或者对象的当前位置为0
				// 需要从头开始下载
				file.delete(); // 移除无效文件
				downloadInfo.currentPos = 0; // 文件位置清零
				// 从头开始下载文件
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + downloadInfo.downloadUrl);
			} else {
				// 断点续传
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + downloadInfo.downloadUrl
						+ "&range=" + file.length());
			}
			
			InputStream in = null;
			FileOutputStream out = null;
			if (httpResult != null && (in  = httpResult.getInputStream()) != null) {
				try {
					out = new FileOutputStream(file, true);// 在原有的文件的基础上追加
					
					int len = 0;
					byte[] buffer = new byte[1024];
					while ((len = in.read(buffer)) != -1 && downloadInfo.currentState == STATE_DOWNLOAD) {
						out.write(buffer);
						out.flush();
						
						downloadInfo.currentPos += len; // 更新当前下载位置
						notifyDownloadProgressChanged(downloadInfo);// 通知进度更新
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.close(in);
					IOUtils.close(out);
				}
			}
			

		}

	}

}
