package com.example.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.protocol.HomeProtocol;
import com.example.googleplay.ui.adapter.MyBaseAdapter;
import com.example.googleplay.ui.holder.BaseHolder;
import com.example.googleplay.ui.holder.HomeHeaderHolder;
import com.example.googleplay.ui.holder.HomeHolder;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.ui.view.MyListView;
import com.example.googleplay.utils.UIUtils;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class HomeFragment extends BaseFragment {

	private ArrayList<AppInfo> data;
	private ArrayList<String> mPictureList;

	@Override
	public View onCreateSuccessView() {
		// TextView textView = new TextView(UIUtils.getContext());
		// textView.setText(getClass().getSimpleName());
		MyListView view = new MyListView(UIUtils.getContext());

		// 添加首页轮播图
		HomeHeaderHolder header = new HomeHeaderHolder();
		view.addHeaderView(header.getRootView());
		view.setAdapter(new HomeAdapter(data));
		if (mPictureList != null) {
			header.setData(mPictureList);
		}
		return view;
	}

	/**
	 * 运行在子线程，可以直接执行耗时的网络操作
	 */
	@Override
	public ResultState onLoad() {
		// data = new ArrayList<String>();
		// for (int i = 0; i < 20; i++) {
		// data.add("测试数据" + i);
		// }
		HomeProtocol homeProtocol = new HomeProtocol();
		data = homeProtocol.getData(0);

		mPictureList = homeProtocol.getPictureList();
		
		return check(data); // 校验数据并返回
	}

	class HomeAdapter extends MyBaseAdapter<AppInfo> {

		public HomeAdapter(ArrayList<AppInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder<AppInfo> getHolder(int position) {
			return new HomeHolder();
		}

		/**
		 * 运行在子线程，可以直接执行耗时的网络操作
		 */
		@Override
		public ArrayList<AppInfo> onLoadMore() {
			// ArrayList<String> moreData = new ArrayList<String>();
			// for (int i = 0; i < 20; i++) {
			// moreData.add("测试更多数据" + i);
			// }
			// SystemClock.sleep(2000);

			HomeProtocol homeProtocol = new HomeProtocol();
			ArrayList<AppInfo> moreData = homeProtocol.getData(getListSize());
			return moreData;
		}

		// @Override
		// public boolean hasMore() {
		// return false;
		// }

		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// ViewHolder holder = null;
		// if (convertView == null) {
		// // 1.加载布局
		// convertView = UIUtils.inflate(R.layout.list_item_home);
		// // 2.初始化控件
		// holder = new ViewHolder();
		// holder.tvContent = (TextView)
		// convertView.findViewById(R.id.tv_content);
		// // 3.给convertView打标记tag
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		// // 4.根据数据来刷新界面
		// String content = getItem(position);
		// holder.tvContent.setText(content);
		// return convertView;
		// }

	}

}
