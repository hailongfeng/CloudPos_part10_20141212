package com.cynovo.sirius.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cynovo.sirius.R;
import com.umeng.analytics.MobclickAgent;

public class AbstractSupportV4Fragment extends Fragment {
	ProgressDialog pDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getActivity().getResources().getString(R.string.is_loading));
		pDialog.setCancelable(false);
	}
	
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
