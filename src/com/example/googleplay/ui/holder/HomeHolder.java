package com.example.googleplay.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.domain.DownloadInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.manager.DownloadManager;
import com.example.googleplay.manager.DownloadManager.DownloadObserver;
import com.example.googleplay.ui.view.ProgressArc;
import com.example.googleplay.utils.BitmapHelper;
import com.example.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 首页的Holder
 * 
 * @author Administrator
 * 
 */
public class HomeHolder extends BaseHolder<AppInfo> implements
		DownloadObserver, OnClickListener {

	private TextView tvName, tvSize, tvDes;
	private ImageView ivIcon;
	private RatingBar rbStar;
	private BitmapUtils mBitmapUtils;
	private FrameLayout flDownload;
	private TextView tvDownload;
	private ProgressArc pbProgress;
	private AppInfo mAppInfo;
	private DownloadManager mDM;
	private int mCurrentState;
	private float mProgress;

	@Override
	public View initView() {
		// 1.加载布局
		View view = UIUtils.inflate(R.layout.list_item_home);
		// 2.初始化控件
		tvName = (TextView) view.findViewById(R.id.tv_name);
		tvSize = (TextView) view.findViewById(R.id.tv_size);
		tvDes = (TextView) view.findViewById(R.id.tv_des);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		rbStar = (RatingBar) view.findViewById(R.id.rb_star);

		// 初始化控件
		flDownload = (FrameLayout) view.findViewById(R.id.fl_download);
		tvDownload = (TextView) view.findViewById(R.id.tv_download);
		// 初始化圆形进度条
		pbProgress = new ProgressArc(UIUtils.getContext());
		// 设置圆形进度条直径
		pbProgress.setArcDiameter(UIUtils.dip2px(26));
		// 设置进度条颜色
		pbProgress.setProgressColor(UIUtils.getColor(R.color.progress));
		// 设置进度条宽高布局参数
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				UIUtils.dip2px(27), UIUtils.dip2px(27));
		flDownload.addView(pbProgress, params);

		flDownload.setOnClickListener(this);

		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		tvName.setText(data.name);
		tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
		tvDes.setText(data.des);
		rbStar.setRating((float) data.stars);
		mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name="
				+ data.iconUrl);

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
		refreshUI(mCurrentState, mProgress,data.id);

	}

	/**
	 * 刷新界面
	 * 
	 * @param progress
	 * @param state
	 */
	private void refreshUI(int state, float progress, String id) {
		// 由于listview的重用机制，确保刷新之前，确实是同一个应用
		if (!getData().id.equals(id)) {
			return;
		}
		
		mCurrentState = state;
		mProgress = progress;
		switch (state) {
		case DownloadManager.STATE_UNDO:
			pbProgress.setBackgroundResource(R.drawable.ic_download);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvDownload.setText("下载");
			break;
		case DownloadManager.STATE_WAITING:
			pbProgress.setBackgroundResource(R.drawable.ic_download);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
			tvDownload.setText("等待");
			break;
		case DownloadManager.STATE_DOWNLOAD:
			pbProgress.setBackgroundResource(R.drawable.ic_pause);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
			pbProgress.setProgress(progress, true);
			tvDownload.setText((int) (progress * 100) + "%");
			break;
		case DownloadManager.STATE_PAUSE:
			pbProgress.setBackgroundResource(R.drawable.ic_resume);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			flDownload.setVisibility(View.VISIBLE);
			break;
		case DownloadManager.STATE_ERROR:
			pbProgress.setBackgroundResource(R.drawable.ic_redownload);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvDownload.setText("失败");
			break;
		case DownloadManager.STATE_SUCCESS:
			pbProgress.setBackgroundResource(R.drawable.ic_install);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvDownload.setText("安装");
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.fl_download:
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

	private void refreshOnMainThread(final DownloadInfo info) {
		if (info.id.equals(mAppInfo.id)) {
			UIUtils.runOnUIThread(new Runnable() {

				@Override
				public void run() {
					refreshUI(info.currentState, info.getProgress(), info.id);
				}
			});
		}
	}

	@Override
	public void onDownloadStateChanged(DownloadInfo info) {
		refreshOnMainThread(info);
	}

	@Override
	public void onDownloadProgressChanged(DownloadInfo info) {
		refreshOnMainThread(info);
	}

}
