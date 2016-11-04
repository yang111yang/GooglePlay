package com.example.googleplay.ui.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.protocol.HomeDetailProtocol;
import com.example.googleplay.ui.holder.DetailAppInfoHolder;
import com.example.googleplay.ui.holder.DetailDesInfoHolder;
import com.example.googleplay.ui.holder.DetailDownloadHolder;
import com.example.googleplay.ui.holder.DetailPicsInfoHolder;
import com.example.googleplay.ui.holder.DetailSafeInfoHolder;
import com.example.googleplay.ui.view.LoadingPage;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.utils.UIUtils;

/**
 * 首页应用详情页
 * 
 * @author Administrator
 */
public class HomeDetailActivity extends BaseActivity {

	private String packageName;
	private FrameLayout flDetailAppInfo, flDetailSafeInfo, flDetailDes,
			flDetailDownload;
	private AppInfo data;
	private LoadingPage mLoadingPage;
	private HorizontalScrollView hsvDetailPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLoadingPage = new LoadingPage(UIUtils.getContext()) {

			@Override
			public View onCreateSuccessView() {
				return HomeDetailActivity.this.onCreateSuccessView();
			}

			@Override
			public ResultState initData() {
				return HomeDetailActivity.this.initData();
			}
		};

		setContentView(mLoadingPage);

		// 获取从HomeFragment中传递过来的packageName
		packageName = getIntent().getStringExtra("packageName");
		// 开始加载网络数据
		mLoadingPage.loadData();

		initActionBar();
	}

	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); // 显示左上角返回键，当和侧边栏结合时展示三个杠的图片

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// 关闭当前页面
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public View onCreateSuccessView() {
		// 加载布局
		View view = UIUtils.inflate(R.layout.home_detail_appinfo);

		// 初始化应用信息模块
		flDetailAppInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_appinfo);
		// 动态给帧布局填充页面
		DetailAppInfoHolder appInfoHolder = new DetailAppInfoHolder();
		flDetailAppInfo.addView(appInfoHolder.getRootView());
		appInfoHolder.setData(data);

		// 初始化安全信息描述模块
		flDetailSafeInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_safeinfo);
		DetailSafeInfoHolder safeInfoHolder = new DetailSafeInfoHolder();
		flDetailSafeInfo.addView(safeInfoHolder.getRootView());
		safeInfoHolder.setData(data);

		// 初始化截图模块
		hsvDetailPic = (HorizontalScrollView) view
				.findViewById(R.id.hsv_detail_pic);
		DetailPicsInfoHolder picsInfoHolder = new DetailPicsInfoHolder();
		hsvDetailPic.addView(picsInfoHolder.getRootView());
		picsInfoHolder.setData(data);

		// 初始化应用介绍模块
		flDetailDes = (FrameLayout) view.findViewById(R.id.fl_detail_des);
		DetailDesInfoHolder desInfoHolder = new DetailDesInfoHolder();
		flDetailDes.addView(desInfoHolder.getRootView());
		desInfoHolder.setData(data);

		// 初始化下载模块
		flDetailDownload = (FrameLayout) view
				.findViewById(R.id.fl_detail_download);
		DetailDownloadHolder downloadHolder = new DetailDownloadHolder();
		flDetailDownload.addView(downloadHolder.getRootView());
		downloadHolder.setData(data);

		return view;
	}

	public ResultState initData() {
		// 请求网络，加载数据
		HomeDetailProtocol homeDetailProtocol = new HomeDetailProtocol(
				packageName);
		data = homeDetailProtocol.getData(0);
		if (data != null) {
			return ResultState.STATE_SUCCESS;
		} else {
			return ResultState.STATE_ERROR;
		}
	}

}
