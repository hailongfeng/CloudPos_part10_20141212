package com.cynovo.sirius.fragment;

import java.io.File;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cynovo.sirius.R;
import com.cynovo.sirius.adapter.AttributeAdapter;
import com.cynovo.sirius.adapter.AttributeItemAdapter;
import com.cynovo.sirius.adapter.CateAdapter;
import com.cynovo.sirius.core.product.ProductData;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.util.CloudPosApp;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.JavaUtil;
import com.cynovo.sirius.widget.AnyWhereDialog;

public class ProductManagerAddGoodsFragment extends AbstractFragment
		implements OnClickListener, OnEditorActionListener {

	private View result = null;
	private ViewFlipper mViewFlipper = null;
	private Button mPrdMgAttrBtn;
	private EditText mProductName;
	private EditText mProductPrice;
	private FragmentManager fragmentManager;
	private ProductManagerFragment mPrdmgFragment;
	private ProductData mProductData = new ProductData();

	private Spinner mCateBtn = null;
	private Button mSave = null;
	private ImageView mProductPic = null;
	private Button attrBack = null;
	private Button addAttr = null;
	private Button deleteProduct = null;

	private AnyWhereDialog mDialog = null;
	private CateAdapter adapter = null;

	private String oldImagePath = null;
	private ListView mPrdmg_good_listview;
	private ListView mPrdmg_add_attr_listview;

	private AttributeAdapter mAttributeAdapter = null;
	private AttributeItemAdapter mAttributeItemAdapter = null;

	private int defaultCateid = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentManager = getFragmentManager();
		mPrdmgFragment = (ProductManagerFragment) fragmentManager
				.findFragmentById(R.id.productactivity);
		result = inflater.inflate(R.layout.productmanageraddgoodsproduct, container, false);
		mProductName = (EditText) result
				.findViewById(R.id.prdmg_goods_producteditinputname);
//		mProductName.setOnEditorActionListener(this);
		mProductPrice = (EditText) result.findViewById(R.id.prdmg_goods_producteditinputprice);
		mProductPrice.setOnEditorActionListener(this);
		mCateBtn = (Spinner) result.findViewById(R.id.prdmg_goods_showBtn);
		Bundle bundle = getArguments();
	
		adapter = new CateAdapter(this.getActivity());
		mCateBtn.setAdapter(adapter);

		mSave = (Button) result.findViewById(R.id.pdtmg_add_goods_btn);
		mProductPic = (ImageView) result.findViewById(R.id.pdtmg_show_pic_btn);
		attrBack = (Button) result.findViewById(R.id.pdtmg_show_attr_pic_btn);
		addAttr = (Button) result.findViewById(R.id.pdtmg_add_goods_attr_btn);
		addAttr.setVisibility(View.INVISIBLE);
		deleteProduct = (Button) result.findViewById(R.id.prdmg_good_deleteBtn);

		mPrdMgAttrBtn = (Button) result.findViewById(R.id.prdmg_goods_productattr);
		
		if(bundle != null){
			int result = bundle.getInt("result");
			if(result == 1)
				deleteProduct.setVisibility(View.INVISIBLE);
		}
		
		deleteProduct.setOnClickListener(this);
		attrBack.setOnClickListener(this);
		addAttr.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mProductPic.setOnClickListener(this);
		mPrdMgAttrBtn.setOnClickListener(this);

		mViewFlipper = (ViewFlipper) result.findViewById(R.id.prdmg_good_viewflipper);

		mPrdmg_good_listview = (ListView) result.findViewById(R.id.prdmg_good_listview);
		mAttributeItemAdapter = new AttributeItemAdapter(getActivity());
		mPrdmg_good_listview.setAdapter(mAttributeItemAdapter);

		// 第二级，供选择框
		mPrdmg_add_attr_listview = (ListView) result.findViewById(R.id.prdmg_good_attrlistview);
		mAttributeAdapter = new AttributeAdapter(getActivity());
		mPrdmg_add_attr_listview.setAdapter(mAttributeAdapter);

		return result;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(defaultCateid == -1)
			mCateBtn.setSelection(0);
		else
			mCateBtn.setSelection(adapter.getPositionById(defaultCateid));
		
		mProductName.setText(mProductData.getProduct().getName());
		mProductName.setSelection(mProductName.getText().length());
		mProductPrice.setText(mProductData.getProduct().getPrice());
		if (mProductData.getProduct().getProductPhoto() != null
				&& !mProductData.getProduct().getProductPhoto().isEmpty()) {
			Bitmap bitmap = BitmapFactory.decodeFile(Common
					.getRealImagePath(mProductData.getProduct().getProductPhoto()));
			if (bitmap != null)
				mProductPic.setImageBitmap(Common.zoomBitmap(bitmap, 80, 80));
		} else {
			mProductPic.setImageBitmap(null);
			oldImagePath = null;
		}

		mAttributeItemAdapter.setAttrList(mProductData.getAttrList());
		mAttributeAdapter.setCheckedList(mProductData.getAttrList());
		setListViewHeightBasedOnChildren(mPrdmg_good_listview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prdmg_goods_productattr:
			mViewFlipper.setDisplayedChild(1);
			mAttributeAdapter.notifyDataSetChanged();
			// setListViewHeightBasedOnChildren(mPrdmg_add_attr_listview);
			break;
		case R.id.pdtmg_show_attr_pic_btn:
			mViewFlipper.setDisplayedChild(0);
			mAttributeItemAdapter.setAttrList(mProductData.getAttrList());
			// mAttributeAdapter.setCheckedList(mProductData.getAttrList());
			setListViewHeightBasedOnChildren(mPrdmg_good_listview);
			mAttributeItemAdapter.notifyDataSetChanged();
			break;
		case R.id.pdtmg_add_goods_attr_btn:
			break;
		case R.id.pdtmg_show_pic_btn:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			//intent.addCategory(Intent.CATEGORY_APP_GALLERY);
			startActivityForResult(intent, 1);
			break;
		case R.id.pdtmg_add_goods_btn:
			save();
			Common.HideKeyboardIfExist(this);
			break;
		case R.id.prdmg_good_deleteBtn:
			mDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0,
					R.layout.confirm_delete, R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
			Button confirmBtn = (Button) mDialog.findViewById(R.id.confirm_delete);
			Button cancelBtn = (Button) mDialog.findViewById(R.id.cancel_delete);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			mDialog.show();
			break;
		case R.id.confirm_delete:
			delete();
			mDialog.dismiss();
			break;
		case R.id.cancel_delete:
			mDialog.dismiss();
			break;
		}
	}

	public void addNew(int protectCate) {
		this.mProductData = new ProductData();
		if(protectCate == -1)
			defaultCateid = -1;
		else
			defaultCateid = protectCate;
	}

	private void save() {
		if (!mProductName.getText().toString().isEmpty()
				&& !mProductPrice.getText().toString().isEmpty()) {
			int ret = ProductManager.saveProductData(getProductData());
			if (ret == -1) {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addgoods_fail,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addgoods_success,
						Toast.LENGTH_SHORT).show();
				mPrdmgFragment.updateProduct();
				mPrdmgFragment.updateCate();
				mPrdmgFragment.updateAttr();
				mPrdmgFragment.deleteFragment();
				Common.HideKeyboardIfExist(this);
			}

		} else
			Toast.makeText(CloudPosApp.getInstance(), R.string.alert_addgoods_notnull,
					Toast.LENGTH_SHORT).show();
	}

	public ProductData getProductData() {
		if (mProductData == null) {
			mProductData = new ProductData();
		}
		// TODO: mProductData.setCateName(mCateBtn.getText().toString());
		if (mCateBtn.getSelectedItemId() != 0) {
			mProductData.setCateName(ProductManager.getProductCategory(
					(int) mCateBtn.getSelectedItemId()).getProductCategoryName());
			mProductData.getProduct().setProductCategoryID(ProductManager.getProductCategory(
					(int) mCateBtn.getSelectedItemId()).getProductCategoryId());
		} else
			mProductData.getProduct().setProductCategoryID(0);
		mProductData.getProduct().setName(mProductName.getText().toString());
		mProductData.getProduct().setPrice(mProductPrice.getText().toString());

		if (oldImagePath != null) {
			File tmp = new File(oldImagePath);
			mProductData.getProduct().setProductPhoto(tmp.getName());
			JavaUtil.copyFileToDir(this.getActivity(), oldImagePath, Common.IMAGE_PATH);
		}

		return mProductData;
	}

	private void delete() {
		if (mProductData != null && mProductData.getProduct() != null)
			ProductManager.deleteProduct(mProductData.getProduct());
		mPrdmgFragment.updateProduct();
		mPrdmgFragment.updateCate();
		mPrdmgFragment.updateAttr();
		mPrdmgFragment.deleteFragment();
		Common.HideKeyboardIfExist(this);
	}

	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		if (listAdapter.getCount() > 0) {
			totalHeight = listAdapter.getView(0, null, listView)
					.getMinimumHeight() * (listAdapter.getCount());
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 初始化商品的信息
	 * @param mProductData
	 */
	public void setProductData(ProductData mProductData) {
		this.mProductData = mProductData;
		if (mProductData == null)
			this.mProductData = new ProductData();
		else {
			if (mProductData.getmProductCategory() != null)
				defaultCateid = mProductData.getmProductCategory().getProductCategoryId();
			else
				defaultCateid = -1;
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();

			oldImagePath = JavaUtil.getPathFromUri(uri, this.getActivity());

			ContentResolver cr = this.getActivity().getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				if(bitmap != null)
					mProductPic.setImageBitmap(Common.zoomBitmap(bitmap, 80, 80));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId())
			{
			case R.id.prdmg_goods_producteditinputname:
				Common.HideKeyboardIfExist(ProductManagerAddGoodsFragment.this);
				mProductPrice.requestFocus();
				break;
			case R.id.prdmg_goods_producteditinputprice:
				Common.HideKeyboardIfExist(ProductManagerAddGoodsFragment.this);
				break;
			}
		}
		return false;
	}
}
