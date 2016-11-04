package com.example.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.googleplay.R;
import com.example.googleplay.manager.ThreadManager;
import com.example.googleplay.utils.UIUtils;

/**
 * 根据当前控件的状态来动态的现实当前页面的布局 - 未加载 - 加载中 - 加载失败 - 数据为空 - 加载成功
 * 
 * @author Administrator
 * 
 */
public abstract class LoadingPage extends FrameLayout {

	private static final int STATE_LOAD_UNDO = 1; // 加载失败
	private static final int STATE_LOAD_LOADING = 2; // 正在加载
	private static final int STATE_LOAD_ERROR = 3; // 加载失败
	private static final int STATE_LOAD_EMPTY = 4; // 数据为空
	private static final int STATE_LOAD_SUCCESS = 5; // 加载成功

	private int mCurrentState = STATE_LOAD_UNDO; // 当前控件的状态
	private View mLoadingPage;
	private View mErrorPage;
	private View mEmptyPage;
	private View mSuccessPage;

	public LoadingPage(Context context) {
		super(context);
		initView();
	}

	public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		// 初始化加载中的布局
		if (mLoadingPage == null) {
			mLoadingPage = UIUtils.inflate(R.layout.page_loading);
			addView(mLoadingPage); // 将该布局添加到帧布局中
		}

		// 初始化加载失败的布局
		if (mErrorPage == null) {
			mErrorPage = UIUtils.inflate(R.layout.page_error);
			// 加载失败，点击重试
			Button btnRetry = (Button) mErrorPage.findViewById(R.id.btn_retry);
			btnRetry.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 请求网络数据
					loadData();
				}
			});
			addView(mErrorPage);
		}

		// 初始化数据为空的布局
		if (mEmptyPage == null) {
			mEmptyPage = UIUtils.inflate(R.layout.page_empty);
			addView(mEmptyPage);
		}

		showRightPage();
	}

	/**
	 * 根据当前的状态，来显示现实那个布局
	 */
	private void showRightPage() {
		mLoadingPage
				.setVisibility((mCurrentState == STATE_LOAD_UNDO || mCurrentState == STATE_LOAD_LOADING) ? View.VISIBLE
						: View.GONE);
		mErrorPage
				.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE
						: View.GONE);
		mEmptyPage
				.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE
						: View.GONE);

		if (mSuccessPage == null && mCurrentState == STATE_LOAD_SUCCESS) {
			mSuccessPage = onCreateSuccessView();
			if (mSuccessPage != null) {
				addView(mSuccessPage);
			}
		}

		if (mSuccessPage != null) {
			mSuccessPage
					.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE
							: View.GONE);
		}

	}

	/**
	 * 开始加载数据
	 */
	public void loadData() {
		if (mCurrentState != STATE_LOAD_LOADING) {
			mCurrentState = STATE_LOAD_LOADING;
//			new Thread() {
//				public void run() {
//					final ResultState resultState = initData();
//					// 运行在主线程
//					UIUtils.runOnUIThread(new Runnable() {
//						@Override
//						public void run() {
//							if (resultState != null) {
//								mCurrentState = resultState.getState(); // 网络加载结束后，更新网络状态
//								// 根据最新的网络状态来刷新页面
//								showRightPage();
//							}
//						}
//					});
//				};
//			}.start();
			ThreadManager.getThreadPool().excute(new Runnable() {
				
				@Override
				public void run() {
					final ResultState resultState = initData();
					// 运行在主线程
					UIUtils.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							if (resultState != null) {
								mCurrentState = resultState.getState(); // 网络加载结束后，更新网络状态
								// 根据最新的网络状态来刷新页面
								showRightPage();
							}
						}
					});
				}
			});
		}
	}

	/**
	 * 当加载成功时，调用此方法
	 * @return 加载数据后返回的枚举
	 */
	public abstract View onCreateSuccessView();

	/**
	 * 加载数据
	 */
	public abstract ResultState initData();

	/**
	 * 定义加载数据后返回的枚举
	 */
	public enum ResultState {
		STATE_SUCCESS(STATE_LOAD_SUCCESS), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_ERROR(
				STATE_LOAD_ERROR);

		private int state;

		private ResultState(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}

	}

}
