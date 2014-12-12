package com.cynovo.sirius.fragment;

import android.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public class AbstractFragment extends Fragment {
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}
}
