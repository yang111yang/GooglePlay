package com.example.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;
import android.widget.ListView;

import com.example.googleplay.ui.adapter.MyBaseAdapter;
import com.example.googleplay.ui.holder.BaseHolder;
import com.example.googleplay.ui.holder.HomeHolder;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.utils.UIUtils;

/**
 * 首页
 * @author Administrator
 *
 */
public class HomeFragment extends BaseFragment {
	
	private ArrayList<String> data;

	@Override
	public View onCreateSuccessView() {
//		TextView textView = new TextView(UIUtils.getContext());
//		textView.setText(getClass().getSimpleName());
		ListView view = new ListView(UIUtils.getContext());
		view.setAdapter(new HomeAdapter(data));
		return view;
	}

	@Override
	public ResultState onLoad() {
		data = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			data.add("测试数据" + i);
		}
		return ResultState.STATE_SUCCESS;
	}
	
	class HomeAdapter extends MyBaseAdapter<String> {

		public HomeAdapter(ArrayList<String> data) {
			super(data);
		}

		@Override
		public BaseHolder<String> getHolder() {
			return new HomeHolder();
		}

//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = null;
//			if (convertView == null) {
//				// 1.加载布局
//				convertView = UIUtils.inflate(R.layout.list_item_home);
//				// 2.初始化控件
//				holder = new ViewHolder();
//				holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
//				// 3.给convertView打标记tag
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			// 4.根据数据来刷新界面
//			String content = getItem(position);
//			holder.tvContent.setText(content);
//			return convertView;
//		}
		
	}
	

}
