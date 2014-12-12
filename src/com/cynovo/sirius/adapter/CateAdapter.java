package com.cynovo.sirius.adapter;

import java.util.List;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.product.ProductCategory;
import com.cynovo.sirius.core.product.ProductManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CateAdapter extends BaseAdapter {

	 private LayoutInflater listInflater; 
	 private List<ProductCategory> list = ProductManager.getAllProductCategory();
	 @SuppressWarnings("unused")
	private Context context = null;
	 public  CateAdapter(Context context){
		 this.context = context;
		 listInflater = LayoutInflater.from(context);
		 list.add(0,new ProductCategory(0,context.getString(R.string.nocate),1));
	 }
	 

	@Override
	public int getCount() {
		if(list == null)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if(list == null)
			return 0;
		return list.get(position);
	}

	public int getPositionById(int cateId)
	{
		if(list == null)
			return -1;
		for(int i = 0 ; i < list.size() ; i++)
		{
			if(list.get(i).getProductCategoryId() == cateId)
				return i;
		}
		return -1;
	}
	
	@Override
	public long getItemId(int position) {
		if(list == null)
			return 0;
		return list.get(position).getProductCategoryId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = listInflater.inflate(R.layout.spinner_item_layout, null);

			holder.showTextView = (TextView) convertView.findViewById(R.id.cate_name);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.showTextView.setText(list.get(position).getProductCategoryName() );
		return convertView;
		
	}

	public class ViewHolder {
			public TextView showTextView;
		}

}
