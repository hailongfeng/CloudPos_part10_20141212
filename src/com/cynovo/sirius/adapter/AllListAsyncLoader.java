package com.cynovo.sirius.adapter;

import java.util.List;

import com.cynovo.sirius.core.order.LiteOrderInfo;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class AllListAsyncLoader extends AsyncTaskLoader<List<LiteOrderInfo>> {

	public AllListAsyncLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<LiteOrderInfo> loadInBackground() {
		// TODO Auto-generated method stub
		return null;
	}

}
