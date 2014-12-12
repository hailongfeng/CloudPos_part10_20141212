package com.cynovo.sirius.fragment;

 
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cynovo.sirius.R;

public class BackFragment extends  AbstractFragment {

	View result = null;
	Button buttonBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		result = inflater.inflate(R.layout.producttitle_layout, container, false);
		return result;
	}
}
