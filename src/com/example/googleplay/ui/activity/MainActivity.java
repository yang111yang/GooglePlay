package com.example.googleplay.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.googleplay.R;
import com.example.googleplay.ui.fragment.FragmentFactory;
import com.example.googleplay.ui.view.PagerTab;
import com.example.googleplay.utils.UIUtils;

public class MainActivity extends BaseActivity {

	private PagerTab mPagerTab;
	private ViewPager mViewPager;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUI();
	}

	private void initUI() {
		mPagerTab = (PagerTab) findViewById(R.id.pager_tab); // 页签指示器
		mViewPager = (ViewPager) findViewById(R.id.viewpager); // ViewPager
		// 创建适配器
		mAdapter = new MyAdapter(getSupportFragmentManager());
		// 给ViewPager设置适配器
		mViewPager.setAdapter(mAdapter);
		// 把页签指示器和ViewPager关联起来
		mPagerTab.setViewPager(mViewPager);
	}

	class MyAdapter extends FragmentPagerAdapter {

		private String[] mTabNames;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			mTabNames = UIUtils.getStringArray(R.array.tab_names);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return mTabNames[position];
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = FragmentFactory.createFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return mTabNames.length;
		}

	}

}
