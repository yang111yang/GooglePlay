package com.example.googleplay.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googleplay.http.protocol.RecommendProtocol;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.ui.view.fly.ShakeListener;
import com.example.googleplay.ui.view.fly.ShakeListener.OnShakeListener;
import com.example.googleplay.ui.view.fly.StellarMap;
import com.example.googleplay.ui.view.fly.StellarMap.Adapter;
import com.example.googleplay.utils.LogUtils;
import com.example.googleplay.utils.UIUtils;

/**
 * 推荐
 * 
 * @author Administrator
 * 
 */
public class RecommendFragment extends BaseFragment {

	private ArrayList<String> data;

	@Override
	public View onCreateSuccessView() {
		final StellarMap stellar = new StellarMap(UIUtils.getContext());
		stellar.setAdapter(new RecommendAdapter());
		// 随机方式，将控件划分为9行6列的格子，然后在格子中随机显示
		stellar.setRegularity(6, 9);
		// 设置内边距
		int padding = UIUtils.dip2px(10);
		stellar.setInnerPadding(padding, padding, padding, padding);
		// 设置默认页面
		stellar.setGroup(0, true);
		
		ShakeListener shake = new ShakeListener(UIUtils.getContext());
		shake.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake() {
				//跳转到下一页
				stellar.zoomIn();
			}
		});
		return stellar;
	}

	@Override
	public ResultState onLoad() {
		RecommendProtocol recommendProtocol = new RecommendProtocol();
		data = recommendProtocol.getData(0);
		return check(data);
	}

	class RecommendAdapter implements Adapter {

		// 返回组数
		@Override
		public int getGroupCount() {
			return 2;
		}

		// 返回每组的子元素的个数
		@Override
		public int getCount(int group) {
			int count = data.size() / getGroupCount();
			if (group == getGroupCount() - 1) {
				// 将除不尽，余数添加到最后一页
				count += data.size() % getGroupCount();
			}
			return count;
		}

		// 初始化布局
		@Override
		public View getView(int group, int position, View convertView) {
			position += group*getCount(group -1);
			LogUtils.i("position = " + position);
			TextView view = new TextView(UIUtils.getContext());
			Random random = new Random();
			//设置随机大小
			int size = 16 + random.nextInt(10);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			// 设置随机颜色(30-200)
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);
			view.setTextColor(Color.rgb(r, g, b));
			final String keyword = data.get(position);
			view.setText(keyword);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
				}
			});
			return view;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			if (isZoomIn) {
				// 往下滑加载上一页
				if (group > 0) {
					group--;
				} else {
					// 跳转到最后 一页
					group = getGroupCount() - 1;
				}
			} else {
				// 往上滑加载下一页
				if (group < getGroupCount() - 1) {
					group++;
				} else {
					// 跳转到第一页
					group = 0;
				}
			}
			return group;
		}

	}
}
