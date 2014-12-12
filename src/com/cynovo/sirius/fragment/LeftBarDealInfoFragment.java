package com.cynovo.sirius.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.cynovo.sirius.R;
import com.cynovo.sirius.adapter.RecentSalesAdapter;
import com.cynovo.sirius.core.order.LiteOrderInfo;
import com.cynovo.sirius.widget.AddFootView;

public class LeftBarDealInfoFragment extends LeftBarBaseFragment {
	private View leftShowlayout =null;
	private ExpandableListView mExpandableListView = null;
	private RecentSalesAdapter mRecentSalesAdapter = null;
	
	@Override
	public void onResume() {
		updateList();
		super.onResume();
	}
	
	public void updateList() {
		if(mRecentSalesAdapter != null) {
			mRecentSalesAdapter.updateList();
			mRecentSalesAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		leftShowlayout = inflater.inflate(R.layout.leftbardealinfo, container, false);

	    mExpandableListView = (ExpandableListView) leftShowlayout.findViewById(R.id.leftbarexpandListview);

	    mExpandableListView.addFooterView(new AddFootView(getActivity(), mLeftBarFragment,
	    		getString(R.string.all_record), R.drawable.recent_all));
	    mExpandableListView.addFooterView(new AddFootView(getActivity(), mLeftBarFragment,
	    		getString(R.string.sales_statistics), R.drawable.sale));
	    mRecentSalesAdapter = new RecentSalesAdapter(getActivity());
	    
	    mExpandableListView.setAdapter(mRecentSalesAdapter);
	 	mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				RecentSalesAdapter rsA = (RecentSalesAdapter)parent.getExpandableListAdapter();
				LiteOrderInfo loi = (LiteOrderInfo)rsA.getChild(groupPosition, childPosition);
				if(loi != null)
					mLeftBarFragment.addLeftBarProductItemInfoFragment(loi.getOrder().getOrderID(),
							rsA.getPositionInList(groupPosition), 1);
				return true;
			}
		});
		return leftShowlayout;
	}
}
