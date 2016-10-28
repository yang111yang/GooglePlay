package com.example.googleplay.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.example.googleplay.global.GooglePlayApplication;

public class UIUtils {

	public static Context getContext() {
		return GooglePlayApplication.getContext();
	}

	public static Handler getHandler() {
		return GooglePlayApplication.getHandler();
	}

	public static int getMainThreadId() {
		return GooglePlayApplication.getMainThreadId();
	}

	/********************** 获取资源文件 *************************/

	// 获取字符串
	public static String getString(int id) {
		return getContext().getResources().getString(id);
	}

	// 获取字符串数组
	public static String[] getStringArray(int id) {
		return getContext().getResources().getStringArray(id);
	}

	// 获取图片
	public static Drawable getDrawable(int id) {
		return getContext().getResources().getDrawable(id);
	}

	// 获取颜色
	public static int getColor(int id) {
		return getContext().getResources().getColor(id);
	}
	// 获取id对应的颜色选择器
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return null;
	}

	// 获取尺寸
	public static int getDimen(int id) {
		return getContext().getResources().getDimensionPixelSize(id);
	}

	/**********************dip和px之间的转换 *************************/

	public static int dip2px(float dip) {
		// 获取设备密度
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5f);
	}

	public static float px2dip(float px) {
		// 获取设备密度
		float density = getContext().getResources().getDisplayMetrics().density;
		return px / density;
	}
	
	/**********************加载布局文件 *************************/
	
	public static View inflate(int id){
		return View.inflate(getContext(), id, null);
	}
	
	/**********************判断是否运行在主线程 *************************/
	
	public static boolean isRunOnUIThread(){
		// 获取当前线程id，如果当前线程id和主线程id相同，就是主线程
		int myTid = android.os.Process.myTid();
		if (myTid == getMainThreadId()) {
			return true;
		}
		return false;
	}
	
	public static void runOnUIThread(Runnable r){
		if (isRunOnUIThread()) {
			// 已经是主线程，直接运行
			r.run();
		} else {
			// 如果是子线程，借用handler让其运行在主线程
			getHandler().post(r);
		}
	}

	

}
