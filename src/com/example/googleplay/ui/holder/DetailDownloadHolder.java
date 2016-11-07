package com.example.googleplay.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.domain.DownloadInfo;
import com.example.googleplay.manager.DownloadManager;
import com.example.googleplay.manager.DownloadManager.DownloadObserver;
import com.example.googleplay.ui.view.ProgressHorizontal;
import com.example.googleplay.utils.UIUtils;

public class DetailDownloadHolder extends BaseHolder<AppInfo> implements
		DownloadObserver, OnClickListener {

	private FrameLayout flProgress;
	private ProgressHorizontal ph;
	private Button btnDownload;
	private AppInfo mAppInfo;
	private DownloadManager mDM;
	private int mCurrentState;
	private float mProgress;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_download);
		// 初始化控件
		flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
		btnDownload = (Button) view.findViewById(R.id.btn_download);
		flProgress.setOnClickListener(this);
		btnDownload.setOnClickListener(this);
		// 初始化自定义进度条
		ph = new ProgressHorizontal(UIUtils.getContext());
		ph.setProgressTextColor(Color.WHITE); // 字体颜色
		ph.setProgressTextSize(UIUtils.dip2px(18)); // 字体大小
		ph.setProgressBackgroundResource(R.drawable.progress_bg); // 背景图片
		ph.setProgressResource(R.drawable.progress_normal); // 进度条图片
		// 初始化布局参数
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		flProgress.addView(ph, params);

		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		mAppInfo = data;
		// 初始化下载状态
		mDM = DownloadManager.getInstance();
		// 注册观察者,监听下载进度
		mDM.registerObserver(this);

		DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
		if (downloadInfo == null) {
			// 没有下载过
			mCurrentState = DownloadManager.STATE_UNDO;
			mProgress = 0;
		} else {
			// 之前下载过
			mCurrentState = downloadInfo.currentState;
			mProgress = downloadInfo.getProgress();
		}

		// 根据对象的状态刷新界面
		refreshUI(mCurrentState, mProgress);

	}

	/**
	 * 根据对象的状态刷新界面
	 */
	private void refreshUI(int currentState, float progress) {
		mCurrentState = currentState;
		mProgress = progress;
		
		switch (currentState) {
		case DownloadManager.STATE_UNDO:
			flProgress.setVisibility(View.GONE);
			btnDownload.setVisibility(View.VISIBLE);
			btnDownload.setText("下载");
			break;
		case DownloadManager.STATE_WAITING:
			flProgress.setVisibility(View.GONE);
			btnDownload.setVisibility(View.VISIBLE);
			btnDownload.setText("等待下载...");
			break;
		case DownloadManager.STATE_DOWNLOAD:
			flProgress.setVisibility(View.VISIBLE);
			btnDownload.setVisibility(View.GONE);
			ph.setProgress(progress);
			ph.setCenterText("");
			break;
		case DownloadManager.STATE_PAUSE:
			flProgress.setVisibility(View.VISIBLE);
			btnDownload.setVisibility(View.GONE);
			ph.setProgress(progress);
			ph.setCenterText("暂停");
			break;
		case DownloadManager.STATE_ERROR:
			flProgress.setVisibility(View.GONE);
			btnDownload.setVisibility(View.VISIBLE);
			btnDownload.setText("下载失败");
			break;
		case DownloadManager.STATE_SUCCESS:
			flProgress.setVisibility(View.GONE);
			btnDownload.setVisibility(View.VISIBLE);
			btnDownload.setText("安装");
			break;
		default:
			break;
		}

	}

	@Override
	public void onDownloadStateChanged(final DownloadInfo info) {
		refreshOnMainThread(info);
	}

	@Override
	public void onDownloadProgressChanged(DownloadInfo info) {
		refreshOnMainThread(info);
	}

	private void refreshOnMainThread(final DownloadInfo info) {
		if (info.id.equals(mAppInfo.id)) {
			UIUtils.runOnUIThread(new Runnable() {

				@Override
				public void run() {
					refreshUI(info.currentState, info.getProgress());
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_progress:
		case R.id.btn_download:
			// 根据当前的状态来决定相关操作
			if (mCurrentState == DownloadManager.STATE_UNDO
					|| mCurrentState == DownloadManager.STATE_ERROR
					|| mCurrentState == DownloadManager.STATE_PAUSE) {
				// 开始下载
				mDM.download(getData());
			} else if (mCurrentState == DownloadManager.STATE_DOWNLOAD
					|| mCurrentState == DownloadManager.STATE_WAITING) {
				// 暂停下载
				mDM.pause(getData());
			} else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
				// 开始安装
				mDM.install(getData());
			}
			break;

		default:
			break;
		}
	}
}
