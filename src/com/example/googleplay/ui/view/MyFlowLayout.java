package com.example.googleplay.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.googleplay.utils.UIUtils;

/**
 * 自定义排行控件
 * 
 * @author Administrator
 * 
 */
public class MyFlowLayout extends ViewGroup {

	private int mUsedWidth; // 当前行的已使用宽度

	private int mHorizontalSpacing = UIUtils.dip2px(6); // 水平间距

	private int mVerticalSpacing = UIUtils.dip2px(8); // 竖直间距

	private Line mLine; // 当前行对象

	private ArrayList<Line> mLineList = new ArrayList<MyFlowLayout.Line>(); // 维护所有行的集合

	public static final int MAX_LINE = 100; // 最大行数

	public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyFlowLayout(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = l + getPaddingLeft();
		int top = t + getPaddingTop();
		
		// 遍历行对象，摆放每行位置
		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			line.layout(left, top);// 摆放line对象
			top += line.mMaxHeight + mVerticalSpacing; // 更新top值
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取当前自定义控件的有效宽度
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		// 获取当前自定义控件的有效高度
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()
				- getPaddingBottom();

		// 获取宽高模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 获取所有子控件的数量
		int childCount = getChildCount();

		// 循环便利所有子控件
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			// 获取子控件的宽高
			// 如果父控件是确定模式，则子控件包裹内容；否则子控件的模式与父控件一致
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					(widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST
							: widthMode);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					(heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST
							: heightMode);

			// 开始测量
			childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			// 当行对象为空时，初始化一个行对象
			if (mLine == null) {
				mLine = new Line();
			}

			// 获取子控件的测量宽度
			int childWidth = childView.getMeasuredWidth();

			mUsedWidth += childWidth;

			// 判断已使用的宽度是否超出了边界
			if (mUsedWidth < width) {
				// 给当前行对象添加一个子控件
				mLine.addView(childView);

				mUsedWidth += mHorizontalSpacing; // 增加一个水平间距

				if (mUsedWidth > width) {
					// 增加了水平间距后，就超出了边界，需要换行
					boolean isSuccess = newLine();
					if (!isSuccess) {
						break; // 如果创建行失败，则结束循环
					}
				}

			} else {
				// 超出了边界
				if (mLine.getChildCount() == 0) {
					// 1.当前行中没有任何控件，一旦添加当前子控件，就超出了边界(子控件很长)
					mLine.addView(childView); // 强制添加到当前行
					if (!newLine()) { // 换行
						break;
					}
				} else {
					// 2.当前行中有控件，一档添加当前子控件，就超出了边界
					if (!newLine()) { // 换行
						break;
					}
					mLine.addView(childView);
					mUsedWidth += childWidth + mHorizontalSpacing; // 更新已使用宽度
				}
			}
		}

		// 保存最后一行的行对象
		if (mLine != null && mLine.getChildCount() != 0
				&& !mLineList.contains(mLine)) {
			mLineList.add(mLine);
		}

		int totalWidth = MeasureSpec.getSize(widthMeasureSpec); // 控件整体宽度

		int totalHeight = 0; // 控件整体高度

		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			totalHeight += line.mMaxHeight;
		}

		totalHeight += (mLineList.size() - 1) * mVerticalSpacing
				+ getPaddingTop() + getPaddingBottom(); // 增加上下边距和控件间距

		// 根据最新的宽高，测量整体布局的大小
		setMeasuredDimension(totalWidth, totalHeight);

		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 换行
	 */
	private boolean newLine() {
		// 将上一行添加到集合中
		mLineList.add(mLine);
		// 如果集合的大小小于最大行数，创建新行
		if (mLineList.size() < MAX_LINE) {
			mLine = new Line(); // 创建新行
			mUsedWidth = 0; // 已使用宽度清零

			return true; // 创建行成功
		}
		return false; // 创建行失败
	}

	/**
	 * 当前行对象的封装
	 */
	class Line {

		private int mTotalWidth; // 当前所有控件的总宽度

		public int mMaxHeight; // 当前控件一行的高度(以最高的子控件的高度为准)

		ArrayList<View> mChildViewList = new ArrayList<View>(); // 当前行所有子控件的集合

		/**
		 * 添加子控件
		 * 
		 * @param view
		 */
		public void addView(View view) {
			// 添加一个子控件
			mChildViewList.add(view);
			// 总宽度增加一个子控件的宽度
			mTotalWidth += view.getMeasuredWidth();
			// 获取当前子控件的高度
			int height = view.getMeasuredHeight();
			// 获取当前控件一行的高度
			mMaxHeight = mMaxHeight < height ? height : mMaxHeight;
		}

		/**
		 * 获取当前行中的子控件的数量
		 * 
		 * @return
		 */
		public int getChildCount() {
			return mChildViewList.size();
		}

		/**
		 * 子控件的位置设置
		 * 
		 * @param left
		 * @param top
		 */
		public void layout(int left, int top) {
			// 将剩余的控件平均的分配给每个子控件
			int vaildWidth = getMeasuredWidth() - getPaddingLeft()
					- getPaddingRight(); // 屏幕总有效宽度
			// 当前行子控件的个数
			int childCount = getChildCount();
			// 计算剩余宽度
			int surplusWidth = vaildWidth - mTotalWidth - (childCount - 1)
					* mHorizontalSpacing;

			if (surplusWidth >= 0) {
				// 有剩余空间
				int space = (int) ((float) surplusWidth / childCount + 0.5f); // 平均每个控件分配的大小

				// 重新测量子控件
				for (int i = 0; i < childCount; i++) {
					View childView = mChildViewList.get(i);
					// 获取子控件的宽高
					int measuredWidth = childView.getMeasuredWidth();
					int measuredHeight = childView.getMeasuredHeight();

					measuredWidth += space; // 宽度增加

					int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
							measuredWidth, MeasureSpec.EXACTLY);
					int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
							measuredHeight, MeasureSpec.EXACTLY);

					// 重新测量控件
					childView.measure(widthMeasureSpec, heightMeasureSpec);

					// 当控件比较矮时，需要居中显示，控件竖直方向有一定的偏移
					int topOffset = (mMaxHeight - measuredHeight) / 2;
					if (topOffset < 0) {
						topOffset = 0;
					}

					// 设置当前子控件的位置
					childView.layout(left, top + topOffset, left
							+ measuredWidth, top + topOffset + measuredHeight);
					// 更新left的值
					left += measuredWidth + mHorizontalSpacing;
				}

			} else {
				// 这个控件很长，占满整行
				View childView = mChildViewList.get(0);
				childView.layout(left, top,
						left + childView.getMeasuredWidth(),
						top + childView.getMeasuredHeight());
			}

		}
	}

}
