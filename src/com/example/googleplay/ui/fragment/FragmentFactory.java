package com.example.googleplay.ui.fragment;

import java.util.HashMap;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	
	private static HashMap<Integer, BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();
	
	@SuppressWarnings("unused")
	public static Fragment createFragment(int pos){
		
		BaseFragment fragment = new BaseFragment();
		if (fragment == null) {
			switch (pos) {
			case 1:
				fragment = new HomeFragment();
				break;
			case 2:
				fragment = new AppFragment();
				break;
			case 3:
				fragment = new GameFragment();
				break;
			case 4:
				fragment = new SubjectFragment();
				break;
			case 5:
				fragment = new RecommendFragment();
				break;
			case 6:
				fragment = new CategoryFragment();
				break;
			case 7:
				fragment = new HotFragment();
				break;
				
			default:
				break;
			}
			mFragmentMap.put(pos, fragment);
		}
		return fragment;
	}
	
}
