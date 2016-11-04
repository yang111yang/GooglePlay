package com.example.googleplay.ui.holder;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.utils.LogUtils;
import com.example.googleplay.utils.UIUtils;

/**
 * 详情页-应用描述模块
 * @author Administrator
 *
 */
public class DetailDesInfoHolder extends BaseHolder<AppInfo> {

	private ImageView ivArrow;

	private TextView tvAuthor;

	private TextView tvDes;

	private RelativeLayout rlToggle;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_desinfo);

		tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
		tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		rlToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
		rlToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		tvDes.setText(data.des);
		tvAuthor.setText(data.author);

		// 放在消息队列中，解决当只有3行描述时也是7行高度的bug
		tvDes.post(new Runnable() {
			
			@Override
			public void run() {
				params = (LayoutParams) tvDes.getLayoutParams();
				params.height = getShortHeight();
				tvDes.setLayoutParams(params);
			}
		});
	}

	private boolean isOpen = false;// 标记安全描述开关状态，默认关闭

	private LinearLayout.LayoutParams params;

	/**
	 * 安全描述的开关
	 */
	protected void toggle() {
		ValueAnimator animator = null;
		if (isOpen) {
			// 关闭
			isOpen = false;
			// 属性动画
			if (getCompleteHeight() > getShortHeight()) {
				animator = ValueAnimator.ofInt(getCompleteHeight(),
						getShortHeight());
			}
		} else {
			// 开启
			isOpen = true;
			if (getCompleteHeight() > getShortHeight()) {
				animator = ValueAnimator.ofInt(getShortHeight(),
						getCompleteHeight());
			}
		}

		// 动画状态改变监听
		if (animator != null) {
			animator.addUpdateListener(new AnimatorUpdateListener() {
				// 启动动画之后，会不断的回调此方法来获取最新的高度值
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// 获取最新的高度值
					Integer height = (Integer) animation.getAnimatedValue();

					LogUtils.i("最新高度：" + height);

					// 重新修改布局的高度值
					params.height = height;
					tvDes.setLayoutParams(params);

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
					// ScrollView滑动到底部
					final ScrollView scrollView = getScrollView();
					// 为了安全和稳定性，把滑动到底部的方法放到消息队列中去
					scrollView.post(new Runnable() {
						
						@Override
						public void run() {
							scrollView.fullScroll(ScrollView.FOCUS_DOWN); 
						}
					});
					
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
	}

	/**
	 * 获取7行TextView的高度
	 */
	public int getShortHeight() {
		int width = tvDes.getMeasuredWidth(); // 宽度

		TextView textView = new TextView(UIUtils.getContext());
		textView.setText(getData().des);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // 文字大小
		textView.setMaxLines(7); // 最大行数为7

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY); // 宽度确定不变，match_parent
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST); // 高度包裹内容，wrap_content
		textView.measure(widthMeasureSpec, heightMeasureSpec);

		return textView.getMeasuredHeight();

	}

	/**
	 * 获取完整TextView的高度
	 */
	public int getCompleteHeight() {
		int width = tvDes.getMeasuredWidth(); // 宽度

		TextView textView = new TextView(UIUtils.getContext());
		textView.setText(getData().des);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // 文字大小
		// textView.setMaxLines(7); // 最大行数为7

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY); // 宽度确定不变，match_parent
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST); // 高度包裹内容，wrap_content
		textView.measure(widthMeasureSpec, heightMeasureSpec);

		return textView.getMeasuredHeight();

	}
	
	public ScrollView getScrollView(){
		ViewParent parent = tvDes.getParent();
		while (!(parent instanceof ScrollView)) {
			parent = parent.getParent();
		}
		
		return (ScrollView) parent;
	}
	

}
