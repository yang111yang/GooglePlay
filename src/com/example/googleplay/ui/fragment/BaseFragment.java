package com.example.googleplay.ui.fragment;

import com.example.googleplay.utils.UIUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BaseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = new TextView(UIUtils.getContext());
		textView.setText(getClass().getSimpleName());
		return textView;
	}
	
	
}
