package com.example.googleplay.ui.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.example.googleplay.R;
import com.example.googleplay.ui.fragment.BaseFragment;
import com.example.googleplay.ui.fragment.FragmentFactory;
import com.example.googleplay.ui.view.PagerTab;
import com.example.googleplay.utils.UIUtils;

public class MainActivity extends BaseActivity {

	private PagerTab mPagerTab;
	private ViewPager mViewPager;
	private MyAdapter mAdapter;
	private ActionBarDrawerToggle toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initActionBar();
		initUI();
	}

	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true); // home处可以点击
		actionBar.setDisplayHomeAsUpEnabled(true); // 显示左上角返回键，当和侧边栏结合时展示三个杠的图片

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);

		toggle = new ActionBarDrawerToggle(this, drawer,
				R.drawable.ic_drawer_am, R.string.drawer_open,
				R.string.drawer_close);
		
		toggle.syncState();// 同步状态，将DrawerLayout和开关关联在一起
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// 切换抽屉
			toggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
		// 添加页面切换监听
		mPagerTab.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 开始加载数据
				BaseFragment fragment = FragmentFactory
						.createFragment(position);
				fragment.loadData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	class MyAdapter extends FragmentPagerAdapter {

		private String[] mTabNames;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			mTabNames = UIUtils.getStringArray(R.array.tab_names);
		}

		// 返回页签的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return mTabNames[position];
		}

		// 返回当前页面位置的Fragment对象
		@Override
		public Fragment getItem(int position) {
			return FragmentFactory.createFragment(position);
		}

		// 页签的数量
		@Override
		public int getCount() {
			return mTabNames.length;
		}

	}

}
