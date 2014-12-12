package com.cynovo.sirius.adapter;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.ShoppingCart;
import com.cynovo.sirius.core.TotalMoneyObserver;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.widget.MoneyView;

public class SaleProductAdapter extends ImageCacheBaseAdapter implements TotalMoneyObserver {
	public static final int ITEM_NORMAL = 0;
	public static final int ITEM_DISABLE = 1;
	public static final int ITEM_CLEAR = 2;
	private ShoppingCart mShoppingCart;
	private View mClearView = null;

	public SaleProductAdapter(Context context, ShoppingCart mShoppingCart) {
		super(context);
//		mShoppingCart.setSaleProductAdapter(this);
		// 注册观察者
		mShoppingCart.registerObs(this);
		setShoppingCart(mShoppingCart);
		mClearView = listInflater.inflate(R.layout.sale_product_clear, null);
	}

	public void setShoppingCart(ShoppingCart mShoppingCart) {
		this.mShoppingCart = mShoppingCart;
	}

	@Override
	public int getCount() {
		int listcount = mShoppingCart.getCount();

		if (listcount > 1) {
			return listcount + 1;
		} else if (listcount == 0) {
			return 0;
		} else if (listcount == 1) {
			if (mShoppingCart.hasUnNewAddedManuItem())
				return 1;
			else
				return 2;
		}
		return 0;
	}

	public void removePosition(int pos) {
		if (pos >= 0 && pos < mShoppingCart.getCount()) {
			mShoppingCart.remove(pos);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getItemViewType(int position) {
		int listcount = getCount();
		if (position < mShoppingCart.getCount())
			return ITEM_NORMAL;
		else if (mShoppingCart.hasUnNewAddedManuItem()
				&& ((listcount == 1 && position == 0) || (listcount > 2 && position == listcount - 2)))
			return ITEM_DISABLE;
		return ITEM_CLEAR;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		if (getItemViewType(position) == ITEM_NORMAL)
			return mShoppingCart.getOrderContentById(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == ITEM_CLEAR)
			return mClearView;
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.sale_product_item_info, null);
			holder.showImagepicture = (ImageView) convertView
					.findViewById(R.id.sale_product_item_imageview);
			holder.showTextViewname = (TextView) convertView
					.findViewById(R.id.sale_product_item_name);
			holder.showTextViwnumber = (TextView) convertView
					.findViewById(R.id.sale_product_item_nuber);
			holder.showTextViewprice = (MoneyView) convertView
					.findViewById(R.id.sale_product_item_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mShoppingCart.getOrderContentById(position).getProductId() == ShoppingCart.MANUID
				&& mShoppingCart.getOrderContentById(position)
						.getmOrderContentRemark() != null
				&& mShoppingCart.getOrderContentById(position)
						.getmOrderContentRemark().getRemark() != null
				&& !mShoppingCart.getOrderContentById(position)
						.getmOrderContentRemark().getRemark().isEmpty()) {
			holder.showTextViewname.setText(mShoppingCart
					.getOrderContentById(position).getmOrderContentRemark()
					.getRemark());
		} else {
			holder.showTextViewname.setText(mShoppingCart
					.getOrderContentById(position).getOrderContentName().replace('\\', ' '));
		}
		holder.showTextViwnumber.setText(""
				+ mShoppingCart.getOrderContentById(position).getCount());
		holder.showTextViewprice.setMoney(mShoppingCart.getOrderContentById(position).getItemAmount());
		setImageView(holder, position);
		return convertView;
	}

	protected void setImageView(ViewHolder viewHolder, int position) {
		String imageName = mShoppingCart.getOrderContentById(position).getPhoto();
		int productid = mShoppingCart.getOrderContentById(position).getProductId();
		File f = new File(Common.getRealImagePath(imageName));
		if (imageName == null || imageName.isEmpty() || !f.exists()) {
			if (productid == 0) {
				imageName = "MANU_INPUT_DEFAULT";
			} else {
				imageName = "IMAGE_DEFAULT";
			}
		}

		Bitmap bitmap = getBitmapFromMemCache(imageName);
		if (bitmap == null) {
			if (imageName.equals("MANU_INPUT_DEFAULT")) {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.cart_manual);
			} else if (imageName.equals("IMAGE_DEFAULT")) {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.default_product);
			} else
				bitmap = Common.zoomBitmap(BitmapFactory.decodeFile(
						Common.getRealImagePath(imageName), mBitmapOption), 100, 100);
			addBitmapToMemoryCache(imageName, bitmap);
		}
		viewHolder.showImagepicture.setImageBitmap(bitmap);
	}

	@Override
	public void totalMoneyChanged(String money) {
		this.notifyDataSetChanged();

	}

	public class ViewHolder {
		public ImageView showImagepicture;
		public TextView showTextViewname;
		public TextView showTextViwnumber;
		public MoneyView showTextViewprice;
		// public Button deleteButton;
	}

}
