package com.example.googleplay.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.CategoryInfo;
import com.example.googleplay.utils.UIUtils;

/**
 * 分类模块标题的holder
 * @author Administrator
 *
 */
public class TitleHolder extends BaseHolder<CategoryInfo> {

	private TextView tvTitle;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_title);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		tvTitle.setText(data.title);
	}

}
