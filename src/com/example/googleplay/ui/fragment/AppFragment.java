package com.example.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.protocol.AppProtocol;
import com.example.googleplay.ui.adapter.MyBaseAdapter;
import com.example.googleplay.ui.holder.AppHolder;
import com.example.googleplay.ui.holder.BaseHolder;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.ui.view.MyListView;
import com.example.googleplay.utils.UIUtils;

/**
 * 应用
 * 
 * @author Administrator
 * 
 */
public class AppFragment extends BaseFragment {

	private ArrayList<AppInfo> data;

	@Override
	public View onCreateSuccessView() {
		MyListView view = new MyListView(UIUtils.getContext());
		view.setAdapter(new AppAdapter(data));
		return view;
	}

	@Override
	public ResultState onLoad() {
		AppProtocol appProtocol = new AppProtocol();
		data = appProtocol.getData(0);
		return check(data);
	}

	class AppAdapter extends MyBaseAdapter<AppInfo> {

		public AppAdapter(ArrayList<AppInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder<AppInfo> getHolder(int position) {
			return new AppHolder();
		}

		@Override
		public ArrayList<AppInfo> onLoadMore() {
			AppProtocol appProtocol = new AppProtocol();
			ArrayList<AppInfo> moreData = appProtocol.getData(getListSize());
			return moreData;
		}

	}
}
