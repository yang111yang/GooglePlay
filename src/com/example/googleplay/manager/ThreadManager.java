package com.example.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import com.example.googleplay.utils.LogUtils;

/**
 * 线程管理器-单例
 * 
 * @author Administrator
 * 
 */
public class ThreadManager {

	private static ThreadPool mThreadPool;

	// 获取单例的线程对象
	public static ThreadPool getThreadPool() {

		if (mThreadPool == null) {

			synchronized (ThreadManager.class) {
				if (mThreadPool == null) {
//					int cpuNum = Runtime.getRuntime().availableProcessors(); // 获取CPU数量
//					int threadNum = cpuNum * 2 + 1; // 根据CPU的数量，计算出合理的线程并发数
					int threadNum = 10; 
					
//					LogUtils.i("CPU的个数：" + cpuNum);
					mThreadPool = new ThreadPool(threadNum, threadNum, 0L);
				}
			}
		}

		return mThreadPool;
	}

	public static class ThreadPool {

		private ThreadPoolExecutor mExecutor;

		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;

		public ThreadPool(int corePoolSize, int maximumPoolSize,
				long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		public void excute(Runnable runnable) {
			if (mExecutor == null) {
				mExecutor = new ThreadPoolExecutor(corePoolSize, // 核心线程数
						maximumPoolSize, // 最大线程数
						keepAliveTime, // 闲置线程存活时间
						TimeUnit.SECONDS, // 时间单位
						new LinkedBlockingDeque<Runnable>(), // 线程队列
						Executors.defaultThreadFactory(), // 线程工厂
						new AbortPolicy()); // 队列已满，而且当前线程数已经超过最大线程数时的异常处理策略
			}

			mExecutor.execute(runnable);

		}

		public void cancel(Runnable runnable) {
			if (mExecutor != null) { // 只有下载状态不是下载中时才执行此方法，如果状态为正在下载则在run方法中暂停下载
				mExecutor.getQueue().remove(runnable);
			}
		}

	}

}
