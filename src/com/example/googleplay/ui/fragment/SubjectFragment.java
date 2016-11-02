package com.example.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.example.googleplay.domain.SubjectInfo;
import com.example.googleplay.http.protocol.SubjectProtocol;
import com.example.googleplay.ui.adapter.MyBaseAdapter;
import com.example.googleplay.ui.holder.BaseHolder;
import com.example.googleplay.ui.holder.SubjectHolder;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.ui.view.MyListView;
import com.example.googleplay.utils.UIUtils;

/**
 * 专题
 * 
 * @author Administrator
 * 
 */
public class SubjectFragment extends BaseFragment {

	private ArrayList<SubjectInfo> data;

	@Override
	public View onCreateSuccessView() {
		MyListView view = new MyListView(UIUtils.getContext());
		view.setAdapter(new SubjectAdapter(data));
		return view;
	}

	@Override
	public ResultState onLoad() {
		SubjectProtocol subjectProtocol = new SubjectProtocol();
		data = subjectProtocol.getData(0);
		return check(data);
	}

	class SubjectAdapter extends MyBaseAdapter<SubjectInfo> {

		public SubjectAdapter(ArrayList<SubjectInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder<SubjectInfo> getHolder(int position) {
			return new SubjectHolder();
		}

		@Override
		public ArrayList<SubjectInfo> onLoadMore() {
			SubjectProtocol subjectProtocol = new SubjectProtocol();
			ArrayList<SubjectInfo> moreData = subjectProtocol.getData(getListSize());
			return moreData;
		}

	}

}
