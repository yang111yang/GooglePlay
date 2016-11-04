package com.example.googleplay.ui.holder;

import android.view.View;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.utils.UIUtils;

public class DetailDownloadHolder extends BaseHolder<AppInfo> {

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_download);
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		
	}

}
