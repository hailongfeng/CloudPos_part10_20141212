package com.cynovo.sirius.adapter;

 
import java.io.File;
import java.util.List;
import com.cynovo.sirius.R;
import com.cynovo.sirius.core.ShoppingCart;
import com.cynovo.sirius.core.TotalMoneyObserver;
import com.cynovo.sirius.core.order.OrderContentData;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.widget.MoneyView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailAdapter extends ImageCacheBaseAdapter implements TotalMoneyObserver{
 
	    private List<OrderContentData> mOrderContentData = null;
	    
	    public OrderDetailAdapter(Context context,List<OrderContentData> mOrderContentData){
	    	super(context);
	    	this.mOrderContentData = mOrderContentData;
	    }
		
	    public OrderDetailAdapter(Context context){
	    	super(context);

	    }
	    
	    public void setListData(List<OrderContentData> mOrderContentData)
	    {
	    	this.mOrderContentData = mOrderContentData;
	    }
	    
		@Override
		public int getCount() {
			if(mOrderContentData != null)
				return mOrderContentData.size();
			else
				return 0;
		}
		
		@Override
		public Object getItem(int position) {
			return mOrderContentData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		
		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = listInflater.inflate(R.layout.sale_product_item_info, null);
				holder.showImagepicture = (ImageView) convertView.findViewById(R.id.sale_product_item_imageview);
				holder.showTextViewname = (TextView) convertView.findViewById(R.id.sale_product_item_name);
				holder.showTextViwnumber = (TextView) convertView.findViewById(R.id.sale_product_item_nuber);
				holder.showTextViewprice = (MoneyView) convertView.findViewById(R.id.sale_product_item_price);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(mOrderContentData.get(position).getProductId() == ShoppingCart.MANUID && 
					mOrderContentData.get(position).getmOrderContentRemark() != null &&
					mOrderContentData.get(position).getmOrderContentRemark().getRemark() != null &&
					!mOrderContentData.get(position).getmOrderContentRemark().getRemark().isEmpty())
			{
				holder.showTextViewname.setText((CharSequence)mOrderContentData.get(position).getmOrderContentRemark().getRemark());
			}
			else
			{
				holder.showTextViewname.setText((CharSequence) mOrderContentData.get(position).getOrderContentName().replace('\\', ' '));
			}
			
			holder.showTextViwnumber.setText(""+mOrderContentData.get(position).getCount());
			holder.showTextViewprice.setMoney(mOrderContentData.get(position).getOrderContent().getAmount());
			setImageView(holder,position);
			return convertView;
		}

		protected void setImageView(ViewHolder viewHolder, int position) {
			
			String imageName = mOrderContentData.get(position).getPhoto();
			int productid = mOrderContentData.get(position).getProductId();
			File f = new File(Common.getRealImagePath(imageName));
			if(imageName == null || imageName.isEmpty() || !f.exists())
			{
				if(productid == 0)
				{
					imageName = "MANU_INPUT_DEFAULT";
				}
				else
				{
					imageName = "IMAGE_DEFAULT";
				}
				
			}
			

			Bitmap bitmap = getBitmapFromMemCache(imageName);
			if (bitmap == null) {
				if(imageName.equals("MANU_INPUT_DEFAULT"))
				{
					bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cart_manual);
				}
				else if(imageName.equals("IMAGE_DEFAULT"))
				{
					bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_product);
				}
				else
					bitmap = Common.zoomBitmap(BitmapFactory.decodeFile(Common.getRealImagePath(imageName),mBitmapOption),100,100);
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
		}
}
