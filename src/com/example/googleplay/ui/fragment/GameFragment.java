package com.example.googleplay.ui.fragment;

import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.utils.UIUtils;

import android.view.View;
import android.widget.TextView;


/**
 * 游戏
 * @author Administrator
 *
 */
public class GameFragment extends BaseFragment {

	@Override
	public View onCreateSuccessView() {
		TextView view = new TextView(UIUtils.getContext());
		view.setText("GameFragment");
		return view;
	}

	@Override
	public ResultState onLoad() {
		return ResultState.STATE_SUCCESS;
	}
	
	
}
