package com.example.googleplay.ui.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.googleplay.ui.holder.BaseHolder;
import com.example.googleplay.ui.holder.MoreHolder;
import com.example.googleplay.utils.UIUtils;

/**
 * 对Adapter的封装
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	/** 正常布局类型 ，注意此时必须从0开始 */
	private static final int TYPE_NORMAL = 1;

	/** 加载更多布局类型 */
	private static final int TYPE_MORE = 0;

	private ArrayList<T> data;

	public MyBaseAdapter(ArrayList<T> data) {
		this.data = data;

	}

	/**
	 * 返回加载布局类型的数量
	 */
	@Override
	public int getViewTypeCount() {
		return 2; // 返回2中布局类型：普通布局 + 加载更多
	}

	/**
	 * 返回加载布局的类型
	 */
	@Override
	public int getItemViewType(int position) {
		if (position == getCount() - 1) {
			return TYPE_MORE; // 返回的是加载更多的布局
		} else {
			return getInnerType(position);
		}
	}

	/**
	 * 返回除加载更多外的布局的类型，此方法可由子类重写，从而返回不同的类型
	 * 
	 * @return 默认返回的是普通的布局类型
	 */
	public int getInnerType(int position) {
		return TYPE_NORMAL;
	}

	@Override
	public int getCount() {
		return data.size() + 1;
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
			if (getItemViewType(position) == TYPE_MORE) {
				// 加载更多的布局类型
				holder = (BaseHolder<T>) new MoreHolder(hasMore());
			} else {
				// 子类返回具体对象
				holder = getHolder(position);
			}
		} else {
			holder = (BaseHolder<T>) convertView.getTag();
		}

		// 根据数据来刷新界面
		if (getItemViewType(position) != TYPE_MORE) {
			holder.setData(getItem(position));
		} else {
			// 加载更多布局
			MoreHolder moreHolder = (MoreHolder) holder;
			loadMore(moreHolder);
		}
		return holder.getRootView();
	}

	/**
	 * 是否还有更多数据，此方法可子类可重写
	 * 
	 * @return 默认返回true，有更多数据
	 */
	public boolean hasMore() {
		return true;
	}

	/**
	 * 获取子类返回的具体对象
	 * 
	 * @return 子类返回的具体对象
	 */
	public abstract BaseHolder<T> getHolder(int position);

	/**
	 * 标记是否正在加载更多
	 */
	private boolean isLoadMore = false;

	/**
	 * 加载更多数据
	 */
	public void loadMore(final MoreHolder holder) {
		if (!isLoadMore) {
			isLoadMore = true;
			new Thread() {
				public void run() {
					final ArrayList<T> moreData = onLoadMore();
					UIUtils.runOnUIThread(new Runnable() {

						@Override
						public void run() {
							if (moreData != null) {
								// 默认每页有 20条数据，如果少于20条，则没有更多数据了，到最后一页了
								if (moreData.size() < 20) {
									holder.setData(MoreHolder.STATE_MORE_NONE);
									Toast.makeText(UIUtils.getContext(),
											"没有更多数据了", Toast.LENGTH_SHORT)
											.show();
								} else {
									// 有更多数据
									holder.setData(MoreHolder.STATE_MORE_MORE);
								}
								// 将更多数据追加到当前集合中
								data.addAll(moreData);
								// 刷新界面
								MyBaseAdapter.this.notifyDataSetChanged();
							} else {
								// 加载更多数据失败
								holder.setData(MoreHolder.STATE_MORE_ERROR);
							}
							isLoadMore = false;
						}
					});

				};
			}.start();
		}
	}

	/**
	 * 加载更多数据
	 * 
	 * @return 更多数据的集合
	 */
	public abstract ArrayList<T> onLoadMore();

	/**
	 * 获取当前集合的大小
	 * 
	 * @return
	 */
	public int getListSize() {
		return data.size();
	}

}
