package com.example.googleplay.ui.holder;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.domain.AppInfo.SafeInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.BitmapHelper;
import com.example.googleplay.utils.LogUtils;
import com.example.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 首页应用详情页 - 安全模块
 * @author Administrator
 *
 */
public class DetailSafeInfoHolder extends BaseHolder<AppInfo> {

	private ImageView[] mSafeIcons; // 安全标识图片
	
	private ImageView[] mDesIcons; // 安全描述图片
	
	private TextView[] mSafeDes; // 安全描述文字
	
	private LinearLayout[] mSafeDesBar; // 安全描述条目(图片 + 文字)
	
	private BitmapUtils mBitmapUtils;
	
	private RelativeLayout rlDesRoot;
	
	private LinearLayout llDesRoot;
	
	private int mDesHeight;

	private LinearLayout.LayoutParams mParams;
	
	private ImageView ivArrow;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_safeinfo);

		mSafeIcons = new ImageView[4];
		mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
		mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
		mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
		mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);

		mDesIcons = new ImageView[4];
		mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
		mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
		mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
		mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);

		mSafeDes = new TextView[4];
		mSafeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
		mSafeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
		mSafeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
		mSafeDes[3] = (TextView) view.findViewById(R.id.tv_des4);

		mSafeDesBar = new LinearLayout[4];
		mSafeDesBar[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
		mSafeDesBar[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
		mSafeDesBar[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
		mSafeDesBar[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

		mBitmapUtils = BitmapHelper.getBitmapUtils();
		
		rlDesRoot = (RelativeLayout) view.findViewById(R.id.rl_des_root);
		rlDesRoot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
			}
		});

		llDesRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);
		
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		
		
		return view;
	}

	private boolean isOpen = false;// 标记安全描述开关状态，默认关闭
	
	/**
	 * 安全描述的开关
	 */
	protected void toggle() {
		ValueAnimator animator = null;
		if (isOpen) {
			// 关闭
			isOpen = false;
			// 属性动画
			animator = ValueAnimator.ofInt(mDesHeight,0);
		} else{
			// 开启
			isOpen = true;
			animator = ValueAnimator.ofInt(0,mDesHeight);
		}
		
		// 动画状态改变监听
		animator.addUpdateListener(new AnimatorUpdateListener() {
			// 启动动画之后，会不断的回调此方法来获取最新的高度值
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// 获取最新的高度值
				Integer height = (Integer) animation.getAnimatedValue();
				
				LogUtils.i("最新高度：" + height);
				
				// 重新修改布局的高度值
				mParams.height = height;
				llDesRoot.setLayoutParams(mParams);
				
			}
		});
		
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// 动画结束的事件，更新小箭头的方向
				if (isOpen) {
					ivArrow.setImageResource(R.drawable.arrow_up);
				} else {
					ivArrow.setImageResource(R.drawable.arrow_down);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
			}
		});
		
		animator.setDuration(200);
		animator.start();
	}

	@Override
	public void refreshView(AppInfo data) {
		ArrayList<SafeInfo> safe = data.safe;
		for (int i = 0; i < 4; i++) {
			if (i < safe.size()) {
				SafeInfo safeInfo = data.safe.get(i);
				// 展示数据
				// 安全标识图片
				mBitmapUtils.display(mSafeIcons[i], HttpHelper.URL
						+ "image?name=" + safeInfo.safeUrl);
				// 安全描述图片
				mBitmapUtils.display(mDesIcons[i], HttpHelper.URL
						+ "image?name=" + safeInfo.safeDesUrl);
				// 安全描述文字
				mSafeDes[i].setText(safeInfo.safeDes);

			} else {
				// 隐藏多余的图片
				mSafeIcons[i].setVisibility(View.GONE);
				
				// 隐藏剩余的条目
				mSafeDesBar[i].setVisibility(View.GONE);
			}
		}
		
		// 获取安全描述的完整高度
		llDesRoot.measure(0, 0);
		mDesHeight = llDesRoot.getMeasuredHeight();
		
		// 修改安全描述布局的高度为0，达到隐藏的目的
		mParams = (LinearLayout.LayoutParams) llDesRoot.getLayoutParams();
		mParams.height = 0;
		llDesRoot.setLayoutParams(mParams);
		LogUtils.i("安全描述的高度：" + mDesHeight);
		
	}

}
