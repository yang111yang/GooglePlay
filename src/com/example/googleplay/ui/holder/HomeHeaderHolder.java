package com.example.googleplay.ui.holder;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.googleplay.R;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.BitmapHelper;
import com.example.googleplay.utils.LogUtils;
import com.example.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 首页轮播图的holder
 * 
 * @author 刘建阳
 * @date 2016-11-2下午10:29:29
 */
public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

	private ViewPager mViewPager;
	public ArrayList<String> data;
	private BitmapUtils mBitmapUtils;
	private LinearLayout llContainer;
	private int mPreviousPos;

	@Override
	public View initView() {
		// 创建根布局，相对布局
		RelativeLayout rlRoot = new RelativeLayout(UIUtils.getContext());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dip2px(150));
		rlRoot.setLayoutParams(params);
		// 创建ViewPager
		mViewPager = new ViewPager(UIUtils.getContext());
		RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		rlRoot.addView(mViewPager, vpParams);
		
		// 初始化指示器
		llContainer = new LinearLayout(UIUtils.getContext());
		RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		int padding = UIUtils.dip2px(10);
		llContainer.setPadding(padding, padding, padding, padding);// 设置内边距
		
		// 添加规则
		llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); // 底部对齐
		llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); // 右对齐 
		
		// 添加布局
		rlRoot.addView(llContainer,llParams);

		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return rlRoot;
	}

	@Override
	public void refreshView(final ArrayList<String> data) {
		this.data = data;
		// 给viewPager加载数据
		mViewPager.setAdapter(new HomeHeaderAdapter());
		mViewPager.setCurrentItem(data.size() * 100000);
		
		// 初始化指示器
		for (int i = 0; i < data.size(); i++) {
			ImageView point = new ImageView(UIUtils.getContext());
			
			LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			ivParams.leftMargin = UIUtils.dip2px(4);
			
			if (i == 0) {
				// 为默认选中图片
				point.setImageResource(R.drawable.indicator_selected);
			} else {
				// 为默认图片
				point.setImageResource(R.drawable.indicator_normal);
			}
			point.setLayoutParams(ivParams);
			llContainer.addView(point,ivParams);
		}
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				position = position % data.size();
				ImageView point = (ImageView) llContainer.getChildAt(position);
				point.setImageResource(R.drawable.indicator_selected);
				
				ImageView prePoint = (ImageView) llContainer.getChildAt(mPreviousPos);
				prePoint.setImageResource(R.drawable.indicator_normal);
				
				mPreviousPos = position;
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		HomeHeaderTask task = new HomeHeaderTask();
		task.start();
		
	}
	
	class HomeHeaderTask implements Runnable{
		
		public void start(){
			// 移除以前的所有消息,避免消息重发
			UIUtils.getHandler().removeCallbacksAndMessages(null);
			UIUtils.getHandler().postDelayed(this, 3000);
		}

		@Override
		public void run() {
			int currentItem = mViewPager.getCurrentItem();
			currentItem++;
			LogUtils.i("currentItem = " + currentItem);
			mViewPager.setCurrentItem(currentItem);
			
			// 继续发送延时消息，实现内循环
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		
	}

	class HomeHeaderAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % data.size();
			String url = data.get(position);
			ImageView imageView = new ImageView(UIUtils.getContext());
			mBitmapUtils.display(imageView, HttpHelper.URL + "image?name="
					+ url);
			imageView.setScaleType(ScaleType.FIT_XY);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
