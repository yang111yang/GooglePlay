package com.example.googleplay.ui.activity;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.protocol.HomeDetailProtocol;
import com.example.googleplay.ui.holder.DetailAppInfoHolder;
import com.example.googleplay.ui.holder.DetailDesInfoHolder;
import com.example.googleplay.ui.holder.DetailPicsInfoHolder;
import com.example.googleplay.ui.holder.DetailSafeInfoHolder;
import com.example.googleplay.ui.view.LoadingPage;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.utils.UIUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

/**
 * 首页应用详情页
 * 
 * @author Administrator
 */
public class HomeDetailActivity extends BaseActivity {

	private String packageName;
	private FrameLayout flDetailAppInfo, flDetailSafeInfo;
	private AppInfo data;
	private LoadingPage mLoadingPage;
	private HorizontalScrollView hsvDetailPic;
	private FrameLayout flDetailDes;

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
	}

	public View onCreateSuccessView() {
		// 加载布局
		View view = UIUtils.inflate(R.layout.home_detail_appinfo);

		// 初始化应用信息模块
		flDetailAppInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_appinfo);
		// 动态给帧布局填充页面
		DetailAppInfoHolder detailAppInfoHolder = new DetailAppInfoHolder();
		flDetailAppInfo.addView(detailAppInfoHolder.getRootView());
		detailAppInfoHolder.setData(data);

		// 初始化安全信息描述模块
		flDetailSafeInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_safeinfo);

		DetailSafeInfoHolder detailSafeInfoHolder = new DetailSafeInfoHolder();
		flDetailSafeInfo.addView(detailSafeInfoHolder.getRootView());
		detailSafeInfoHolder.setData(data);
		
		// 初始化截图模块
		hsvDetailPic = (HorizontalScrollView) view.findViewById(R.id.hsv_detail_pic);
		DetailPicsInfoHolder detailPicsInfoHolder = new DetailPicsInfoHolder();
		hsvDetailPic.addView(detailPicsInfoHolder.getRootView());
		detailPicsInfoHolder.setData(data);
		
		// 初始化应用介绍模块
		flDetailDes = (FrameLayout) view.findViewById(R.id.fl_detail_des);
		DetailDesInfoHolder detailDesInfoHolder = new DetailDesInfoHolder();
		flDetailDes.addView(detailDesInfoHolder.getRootView());
		detailDesInfoHolder.setData(data);
		
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
