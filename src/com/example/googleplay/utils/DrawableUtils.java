package com.example.googleplay.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {

	/**
	 * 获取一个shape对象
	 * @param color	背景色
	 * @param radius 圆角半径
	 * @return shape对象
	 */
	public static GradientDrawable getGradientDrawable(int color, int radius){
		// xml中定义的shape对应 此类
		GradientDrawable shape = new GradientDrawable();
		shape.setShape(GradientDrawable.RECTANGLE); // 矩形
		shape.setCornerRadius(radius); // 圆角半径
		shape.setColor(color); // 颜色
		return shape;
	}
	
	/**
	 * 获取状态选择器
	 * @param normal	普通状态的图片
	 * @param press		按下状态的图片
	 * @return	状态选择器
	 */
	public static StateListDrawable getSelector(Drawable normal, Drawable press){
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[]{android.R.attr.state_pressed}, press); // 按下图片
		selector.addState(new int[]{}, normal);
		return selector;
	}
	
	/**
	 * 获取状态选择器
	 * @param normal	普通状态的图片
	 * @param press		按下状态的图片
	 * @return	状态选择器
	 */
	public static StateListDrawable getSelector(int normal, int press, int radius){
		GradientDrawable bgNormal = getGradientDrawable(normal, radius);
		GradientDrawable bgPress = getGradientDrawable(press, radius);
		StateListDrawable selector = getSelector(bgNormal, bgPress);
		return selector;
	}
	
	
}
