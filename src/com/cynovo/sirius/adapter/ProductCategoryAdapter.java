package com.cynovo.sirius.adapter;

import java.util.List;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.product.ProductCategory;
import com.cynovo.sirius.util.MyLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商品列表适配器
 */
public class ProductCategoryAdapter extends BaseAdapter {
	// 运行上下文
	private List<ProductCategory> mCategoryList; // 商品信息集合
	private LayoutInflater listInflater;

	private static int ALL_ONSALE_PIC = R.drawable.item_discount;
	private static int ALL_PRODUCT_PIC = R.drawable.item_all;
	private static int PRODUCT_CATE_PIC = R.drawable.item_list;

	public static final int PRODUCT_TYPE_ALL = 0;
	public static final int PRODUCT_TYPE_ONSALE = 1;
	public static final int PRODUCT_TYPE_CATE = 2;

	private Context mContext;

	public ProductCategoryAdapter(Context context,
			List<ProductCategory> categoryList) {
		mContext = context;
		this.mCategoryList = categoryList;
		listInflater = LayoutInflater.from(context);
	}

	public void udateList(List<ProductCategory> categoryList) {
		this.mCategoryList = categoryList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCategoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.allproduct_item_info,
					null);
			holder.showImageKind = (ImageView) convertView
					.findViewById(R.id.allproductimageview);
			holder.showTextView = (TextView) convertView
					.findViewById(R.id.allproducttextview);
			holder.showImageMore = (ImageView) convertView
					.findViewById(R.id.allproductimageviewmore);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyLog.d("position="+position);
		holder.showImageKind.setBackgroundResource(PRODUCT_CATE_PIC);
		holder.showTextView.setText(mCategoryList.get(position)
				.getProductCategoryName());

		holder.showImageMore.setBackgroundResource(R.drawable.item_more);

		return convertView;
	}

	public class ViewHolder {
		public TextView showTextView;
		public ImageView showImageKind;
		public ImageView showImageMore;
	}

}
