package com.example.googleplay.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.example.googleplay.http.protocol.HotProtocol;
import com.example.googleplay.ui.view.FlowLayout;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.utils.DrawableUtils;
import com.example.googleplay.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 排行
 * @author Administrator
 *
 */
public class HotFragment extends BaseFragment {

	private ArrayList<String> data;

	@Override
	public View onCreateSuccessView() {
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
		int padding = UIUtils.dip2px(10);
		flowLayout.setPadding(padding, padding, padding, padding);
		flowLayout.setHorizontalSpacing(UIUtils.dip2px(6));  // 水平间距
		flowLayout.setVerticalSpacing(UIUtils.dip2px(8)); // 竖直间距
		
		for (int i = 0; i < data.size(); i++) {
			final String keyword = data.get(i);
			TextView view = new TextView(UIUtils.getContext());
			view.setText(keyword);
			view.setPadding(padding, padding, padding, padding);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // 18sp
			view.setTextColor(Color.WHITE);
			view.setGravity(Gravity.CENTER);
			int color = 0xffcecece;
			Random random = new Random();
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);
//			GradientDrawable bgNormal = DrawableUtils.getGradientDrawable(Color.rgb(r, g, b), UIUtils.dip2px(6));
			StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r, g, b), color, UIUtils.dip2px(6));
			view.setBackgroundDrawable(selector);
			// 只有设置点击事件，状态选择器才起作用
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
				}
			});
			flowLayout.addView(view);
		}
		
		scrollView.addView(flowLayout);
		return scrollView;
	}

	@Override
	public ResultState onLoad() {
		HotProtocol hotProtocol = new HotProtocol();
		data = hotProtocol.getData(0);
		return check(data);
	}
	
	
}
