package com.example.googleplay.ui.holder;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreHolder extends BaseHolder<Integer> {
	
	/*
	 * 加载更多的几种状态
	 * 1. 加载更多
	 * 2. 加载失败
	 * 3. 没有更多数据
	 */
	public static final int STATE_MORE_MORE = 1;
	public static final int STATE_MORE_ERROR = 2;
	public static final int STATE_MORE_NONE = 3;
	private LinearLayout llLoadMore;
	private TextView tvLoadError;

	/**
	 * 如果有更多数据，状态为STATE_MORE_MORE，否则为STATE_MORE_NONE，将次状态传递给父类的data
	 * 父类会同时刷新界面
	 * @param hasMore
	 * 					是否有更多数据
	 */
	public MoreHolder(boolean hasMore) {
		setData(hasMore?STATE_MORE_MORE:STATE_MORE_NONE);
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_more);
		llLoadMore = (LinearLayout) view.findViewById(R.id.ll_load_more);
		tvLoadError = (TextView) view.findViewById(R.id.tv_load_error);
		return view;
	}

	@Override
	public void refreshView(Integer data) {
		switch (data) {
		case STATE_MORE_MORE:
			// 加载更多数据
			llLoadMore.setVisibility(View.VISIBLE);
			tvLoadError.setVisibility(View.GONE);
			break;
		case STATE_MORE_NONE:
			// 没有更多数据
			llLoadMore.setVisibility(View.GONE);
			tvLoadError.setVisibility(View.GONE);
			break;
		case STATE_MORE_ERROR:
			// 加载失败
			llLoadMore.setVisibility(View.GONE);
			tvLoadError.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

}
