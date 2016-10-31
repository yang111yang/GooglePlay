package com.example.googleplay.ui.fragment;

import com.example.googleplay.ui.view.LoadingPage.ResultState;

import android.view.View;


/**
 * 游戏
 * @author Administrator
 *
 */
public class GameFragment extends BaseFragment {

	@Override
	public View onCreateSuccessView() {
		return null;
	}

	@Override
	public ResultState onLoad() {
		return ResultState.STATE_EMPTY;
	}
	
	
}
