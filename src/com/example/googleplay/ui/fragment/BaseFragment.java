package com.example.googleplay.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.googleplay.ui.view.LoadingPage;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.utils.UIUtils;


public abstract class BaseFragment extends Fragment {

	private LoadingPage mLoadingPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		TextView textView = new TextView(UIUtils.getContext());
//		textView.setText(getClass().getSimpleName());
		
		mLoadingPage = new LoadingPage(UIUtils.getContext()){

			@Override
			public View onCreateSuccessView() {
				return BaseFragment.this.onCreateSuccessView();
			}

			@Override
			public ResultState initData() {
				return onLoad();
			}
			
		};
		return mLoadingPage;
	}
	
	/**
	 * 初始化加载成功的页面，必须由子类实现
	 * @return	加载的视图
	 */
	public abstract View onCreateSuccessView();
	
	/**
	 * 加载网络数据的方法，必须由子类实现
	 * @return	加载完成后返回的状态码
	 */
	public abstract ResultState onLoad();
	
	/**
	 * 加载网络数据
	 */
	public void loadData(){
		if (mLoadingPage != null) {
			mLoadingPage.loadData();
		}
	}
	
	/**
	 * 对返回网络数据的合法性进行校验
	 * @param obj  返回网络数据
	 * @return	
	 */
	public ResultState check(Object obj){
		if (obj != null) {
			if (obj instanceof ArrayList) {
				ArrayList list = (ArrayList) obj;
				if (list.isEmpty()) {
					return ResultState.STATE_EMPTY;
				} else {
					return ResultState.STATE_SUCCESS;
				}
			}
		}
		return ResultState.STATE_ERROR;
	}
	
}
