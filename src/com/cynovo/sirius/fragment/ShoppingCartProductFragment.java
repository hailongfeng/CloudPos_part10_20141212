package com.cynovo.sirius.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

import com.cynovo.sirius.R;
import com.cynovo.sirius.adapter.SaleProductAdapter;
import com.cynovo.sirius.core.DealModel;
import com.cynovo.sirius.core.ShoppingCart;
import com.cynovo.sirius.core.order.OrderContentData;
import com.cynovo.sirius.util.BasicArithmetic;
import com.cynovo.sirius.widget.AnyWhereDialog;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;

/**
 * 购物车商品fragment
 */
public class ShoppingCartProductFragment extends BaseListFragment implements
		OnClickListener {

	private int mItemHeight = 82;
	private AnyWhereDialog mDialog = null;
	private AttrDialogFragment adf = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private class MyOnDismissCallback implements OnDismissCallback {

		private SaleProductAdapter mAdapter;

		public MyOnDismissCallback(SaleProductAdapter adapter) {
			mAdapter = adapter;
		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				mAdapter.removePosition(position);
			}
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		SaleProductAdapter mAdapter = new SaleProductAdapter(getActivity(),
				DealModel.getInstance().getShoppingCart());

		SwipeDismissAdapter swipeDismissAdapter = new SwipeDismissAdapter(
				mAdapter, new MyOnDismissCallback(mAdapter));
		swipeDismissAdapter.setListView(getListView());
		getListView().setAdapter(swipeDismissAdapter);

		final ListView list = getListView();
		list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
					int toPos = list.getFirstVisiblePosition();
					int nextpos = view.pointToPosition(0, mItemHeight / 2);
					if (toPos != nextpos) {
						toPos = nextpos;
					}
					list.smoothScrollToPositionFromTop(toPos, 0);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	@Override
	public void onListItemClick(final ListView l, View v, final int position,
			long id) {
		super.onListItemClick(l, v, position, id);

		int[] location = new int[2];
		Object tmp = l.getItemAtPosition(position);
		if (DealModel.getInstance().getOrderProcessStatus() != DealModel.ORDER_PROCESS_SELECE)
			return;
		if (tmp != null) {
			OrderContentData ocd = (OrderContentData) tmp;
			if (BasicArithmetic.compare(ocd.getOnePrice(), "0.00") > 0
					|| ocd.getProductId() != ShoppingCart.MANUID) {
				v.getLocationInWindow(location);
				adf = AttrDialogFragment.newInstance(ocd.getProduct()
						.getProductID(), ocd.getProduct().getProductCategoryID());	
				adf.setPosition(position - l.getFirstVisiblePosition());
				adf.show(getFragmentManager(), "dialog");

				adf.setOrderContent(DealModel.getInstance().getShoppingCart()
						.getOrderContentByIndex(position));
				DealModel.getInstance().getShoppingCart()
						.setCurrentEditIndex(position);
			}

		} else {
			mDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0,
					R.layout.clean_up_shoppingcart, R.style.Theme_dialog1,
					Gravity.LEFT | Gravity.TOP, true);
			Button mCancelButton = (Button) mDialog
					.findViewById(R.id.clean_up_cancel_button);
			Button mPositiveButton = (Button) mDialog
					.findViewById(R.id.clean_up_position);
			mCancelButton.setOnClickListener(this);
			mPositiveButton.setOnClickListener(this);
			mDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clean_up_cancel_button:
			mDialog.dismiss();
			break;
		case R.id.clean_up_position:
			DealModel.getInstance().removeAll();
			mDialog.dismiss();
			break;
		}
	}

}
