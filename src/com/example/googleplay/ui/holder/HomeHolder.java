package com.example.googleplay.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

public class HomeHolder extends BaseHolder<String> {

	private TextView tv_content;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_home);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		return view;
	}

	@Override
	public void refreshView(String data) {
		tv_content.setText(data);
	}

}
