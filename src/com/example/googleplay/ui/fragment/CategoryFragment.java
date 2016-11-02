package com.example.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.example.googleplay.domain.CategoryInfo;
import com.example.googleplay.http.protocol.CategoryProtocol;
import com.example.googleplay.ui.adapter.MyBaseAdapter;
import com.example.googleplay.ui.holder.BaseHolder;
import com.example.googleplay.ui.holder.CategoryHolder;
import com.example.googleplay.ui.holder.TitleHolder;
import com.example.googleplay.ui.view.LoadingPage.ResultState;
import com.example.googleplay.ui.view.MyListView;
import com.example.googleplay.utils.UIUtils;

/**
 * 分类
 * 
 * @author Administrator
 * 
 */
public class CategoryFragment extends BaseFragment {

	private ArrayList<CategoryInfo> data;

	@Override
	public View onCreateSuccessView() {
		MyListView view = new MyListView(UIUtils.getContext());
		view.setAdapter(new CategoryAdapter(data));
		return view;
	}

	@Override
	public ResultState onLoad() {
		CategoryProtocol categoryProtocol = new CategoryProtocol();
		data = categoryProtocol.getData(0);
		return check(data);
	}

	class CategoryAdapter extends MyBaseAdapter<CategoryInfo> {

		public CategoryAdapter(ArrayList<CategoryInfo> data) {
			super(data);
		}
		
		@Override
		public boolean hasMore() {
			return false;
		}
		
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1; // 在原来的基础上增加一种标题类型
		}
		
		@Override
		public int getInnerType(int position) {
			// 判断是否是标题类型还是普通的分类类型
			if (data.get(position).isTitle) {
				// 返回标题类型
				return super.getInnerType(position) + 1; // 在原来的基础上增加一种标题类型
			} else {
				// 返回普通分类类型
				return super.getInnerType(position);
			}
			
		}

		@Override
		public BaseHolder<CategoryInfo> getHolder(int position) {
			// 判断是否是标题类型还是普通的分类类型，来返回不同的holder
			if (data.get(position).isTitle) {
				// 返回标题类型
				return new TitleHolder();
			} else {
				// 返回普通分类类型
				return new CategoryHolder();
			}
		}
		
		@Override
		public ArrayList<CategoryInfo> onLoadMore() {
			return null;
		}

	}
}
