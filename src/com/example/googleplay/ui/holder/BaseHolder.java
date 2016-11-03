package com.example.googleplay.ui.holder;

import android.view.View;

/**
 * holder基类
 * @author Administrator
 * @param <T>
 */
public abstract class BaseHolder<T> {

	private View mRootView;
	private T data;

	public BaseHolder() {
		mRootView = initView();
		// 3.打标记tag
		mRootView.setTag(this);
	}

	/**
	 * 1.加载布局
	 * 2.初始化控件
	 * @return	item的布局对象
	 */
	public abstract View initView();
	
	/**
	 * 返回item的布局对象
	 * @return	item的布局对象
	 */
	public View getRootView(){
		return mRootView;
	}

	/**
	 * 设置当前item的数据
	 * @param data	当前item的数据
	 */
	public void setData(T data){
		this.data = data;
		refreshView(data);
	}
	
	/**
	 * 获取当前item的数据
	 * @return	当前item的数据
	 */
	public T getData(){
		return data;
	}
	
	/**
	 * 4.根据数据来刷新界面
	 * @param data	当前item的数据
	 */
	public abstract void refreshView(T data);
}
