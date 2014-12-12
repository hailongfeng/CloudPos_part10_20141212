package com.cynovo.sirius.adapter;


import java.util.List;

import android.annotation.SuppressLint;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.product.Product;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.fragment.ProductManagerFragment;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.widget.MoneyView;
 
@SuppressLint("NewApi")
public class ProductManagerAdapter extends ImageCacheBaseAdapter {
                     //运行上下文   
    private List<Product> mPList;    //商品信息集合   
    ProductManagerFragment mf = null;
    int mcate;
    public ProductManagerAdapter(ProductManagerFragment mf,int cate){
    	super(mf.getActivity());
    	this.mf = mf;
    	this.mcate = cate;
    	if(mcate == -1)
    	{
    		mPList = ProductManager.getALLProduct();
    	}
    	else
    	{
    		mPList = ProductManager.getALLProductByCategory(mcate);
    	}
    }
	
    public void updateProductList()
    {
    	if(mcate == -1)
    	{
    		mPList = ProductManager.getALLProduct();
    	}
    	else
    	{
    		mPList = ProductManager.getALLProductByCategory(mcate);
    	}
    	this.notifyDataSetChanged();
    }
    
	@Override
	public int getCount() {
		return mPList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mPList.get(position).getProductID();
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.allproduct_item_manager_info, null);
			holder.showCatePic = (ImageView) convertView.findViewById(R.id.cate_pic);
			holder.showCateName = (TextView) convertView.findViewById(R.id.cate_name);
			holder.showProductCount = (MoneyView) convertView.findViewById(R.id.product_count);
			holder.showEditButton = (Button) convertView.findViewById(R.id.cate_edit);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final int pos = position;
		holder.showEditButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mf.setEditProdect(mPList.get(pos).getProductID());
			}
		});
		
		String name = mPList.get(position).getName();
		holder.showCateName.setText(name.replace('\\', (char)0x0a));
		holder.showProductCount.setMoney(mPList.get(position).getPrice());
		setImageView(holder, position);
		return convertView;
	}
	
	protected void setImageView(ViewHolder viewHolder, int position) {
		String imageName = mPList.get(position).getProductPhoto();	
		if(imageName == null)
			imageName = "IMAGE_DEFAULT";
		Bitmap bitmap = getBitmapFromMemCache(imageName);
		if (bitmap == null) {
			Bitmap tmp = BitmapFactory.decodeFile(Common.getRealImagePath(imageName),mBitmapOption);
			if(tmp == null)
				tmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_product);
			bitmap = Common.zoomBitmap(tmp,100,100);
		}
		
		addBitmapToMemoryCache(imageName, bitmap);
		viewHolder.showCatePic.setImageBitmap(bitmap);
		viewHolder.showCatePic.setVisibility(View.VISIBLE);

	}
	

	 public class ViewHolder {
		public TextView showCateName;
		public ImageView showCatePic;
		public MoneyView showProductCount;
		public Button showEditButton;
	}
	
}
