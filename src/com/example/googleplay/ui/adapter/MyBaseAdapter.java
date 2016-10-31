package com.example.googleplay.ui.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.googleplay.ui.holder.BaseHolder;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	
	private ArrayList<T> data;

	public MyBaseAdapter(ArrayList<T> data) {
		this.data = data;
		
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<T> holder = null;
		if (convertView == null) {
			// 子类返回具体对象
			holder = getHolder();
		} else {
			holder = (BaseHolder<T>) convertView.getTag();
		}
		
		// 根据数据来刷新界面
		holder.setData(getItem(position));
		return holder.getRootView();
	}

	public abstract BaseHolder<T> getHolder();
	
}
