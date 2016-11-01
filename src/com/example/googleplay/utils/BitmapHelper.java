package com.example.googleplay.utils;

import com.lidroid.xutils.BitmapUtils;
/**
 * BitmapUtils的单例模式
 * @author Administrator
 *
 */
public class BitmapHelper {
	
	private static BitmapUtils mBitmapUtils = null;
	
	public static BitmapUtils getBitmapUtils(){
		if (mBitmapUtils == null) {
			synchronized (BitmapHelper.class) {
				if (mBitmapUtils == null) {
					mBitmapUtils = new BitmapUtils(UIUtils.getContext());
				}
			}
		}
		return mBitmapUtils;
	}

}
