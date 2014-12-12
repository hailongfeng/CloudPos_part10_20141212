package com.cynovo.sirius.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cynovo.sirius.R;
import com.cynovo.sirius.adapter.UpdateAdapter;
import com.cynovo.sirius.util.Common;


public class SettingFragment extends AbstractFragment implements OnItemClickListener {
	private ListView mUpdteListView = null;
	private Integer[] name={R.string.merchant_message,R.string.check_update,R.string.copy_function};
	private List<Integer> dataList = null;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction = null;
	private int container_layout = R.id.update_framelayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.setting_manager, container, false);
		dataList = new ArrayList<Integer>();
		 for(int i=0;i<name.length;i++)
			 dataList.add(name[i]);
			 
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		SettingMerchantFragment proshow = new SettingMerchantFragment();
		fragmentTransaction.add(container_layout, proshow);
		fragmentTransaction.commit();
		 
		mUpdteListView = (ListView) result.findViewById(R.id.update_listview);
		mUpdteListView.setAdapter(new UpdateAdapter(getActivity(), dataList));
		mUpdteListView.setItemChecked(0, true);
		mUpdteListView.setOnItemClickListener(this);
		
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Integer fragment = (Integer) arg0.getItemAtPosition(arg2);
		if(fragment != null)
			upload_fragment(arg2);
		// 隐藏键盘
		Common.HideKeyboardIfExist(SettingFragment.this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(mUpdteListView != null) {
			mUpdteListView.setItemChecked(0, true);
			upload_fragment(0);
		}
	}

	private void addFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(container_layout, fragment);
		fragmentTransaction.commit();
	}
	
	private boolean IsFragment(String name){
		fragmentManager = getFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(container_layout);
		if(fragment != null &&fragment.getClass().getSimpleName().equals(name) )
			return true;
		return false;
	}
	
	private void upload_fragment(int position){
		switch (position) {
		case 0:
			if(!IsFragment("UpdateMerchantFragment")){
				SettingMerchantFragment fragment = new SettingMerchantFragment();
				addFragment(fragment);
			}
			break;
		case 1:
			if(!IsFragment("UpdateCheckoutFragment")){
				SettingUpdateFragment fragment = new SettingUpdateFragment();
				addFragment(fragment);
			}
			break;
		case 2:
			if(!IsFragment("UpdateBackupFragment")){
				SettingBackupFragment fragment = new SettingBackupFragment();
				addFragment(fragment);
			}
			break;
		}
	}
}
