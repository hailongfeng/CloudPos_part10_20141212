package com.cynovo.sirius.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.product.ProductCategory;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.util.CloudPosApp;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.widget.AnyWhereDialog;

public class ProductManagerAddKindFragment extends AbstractFragment implements
		OnClickListener, OnEditorActionListener {
	private View result = null;
	private ProductCategory mProductCategory = new ProductCategory(-1, "", 1);;
	private ProductManagerFragment mPrdmgFragment;
	private EditText mCateName = null;
	private Button mDeleteBtn = null;
	private Button mSave = null;
	private AnyWhereDialog mDialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		result = inflater.inflate(R.layout.productmanageraddgoodskinds,
				container, false);
		FragmentManager fragmentManager = getFragmentManager();
		mPrdmgFragment = (ProductManagerFragment) fragmentManager
				.findFragmentById(R.id.productactivity);
		mSave = (Button) result.findViewById(R.id.productmanageritembtn);
		mDeleteBtn = (Button) result.findViewById(R.id.prdmg_kind_delete);
		mCateName = (EditText) result.findViewById(R.id.prdmg_kind_editinput);
		mCateName.setOnEditorActionListener(this);
		Bundle bundle = getArguments();
		if (bundle != null) {
			int result = bundle.getInt("result");
			if (result == 1)
				mDeleteBtn.setVisibility(View.INVISIBLE);
		}

		mSave.setOnClickListener(this);
		mDeleteBtn.setOnClickListener(this);
		if (mProductCategory != null) {
			mCateName.setText(mProductCategory.getProductCategoryName());
			mCateName.setSelection(mCateName.getText().length());
		}
		return result;
	}

	public ProductCategory getProductCategory() {
		return mProductCategory;
	}

	public void setProductCategory(ProductCategory mProductCategory) {
		this.mProductCategory = mProductCategory;
		if (mCateName != null) {
			mCateName.setText(mProductCategory.getProductCategoryName());
			mCateName.setSelection(mCateName.getText().length());
		}
	}

	private void save() {
		if (mProductCategory != null && !mCateName.getText().toString().isEmpty()) {
			int ret = -1;
			mProductCategory.setProductCategoryName(mCateName.getText().toString());

			if (!ProductManager.productCateHasSame(mProductCategory)) {
				ret = ProductManager.addProductCate(mProductCategory);
			} else {
				ret = ProductManager.updateProductCate(mProductCategory);
			}

			if (ret == -1) {
				Toast.makeText(CloudPosApp.getInstance(),
						R.string.alert_addcate_fail, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CloudPosApp.getInstance(),
						R.string.alert_addcate_success, Toast.LENGTH_SHORT).show();
				Common.HideKeyboardIfExist(this);
				mPrdmgFragment.deleteFragment();
			}

		} else {
			Toast.makeText(CloudPosApp.getInstance(),
					R.string.alert_addcate_notnull, Toast.LENGTH_SHORT).show();
		}
		mPrdmgFragment.updateCate();
	}

	private void delete() {
		if (mProductCategory != null) {
			ProductManager.deleteProductCate(mProductCategory.getProductCategoryId());
			Common.HideKeyboardIfExist(this);
			mPrdmgFragment.deleteFragment();
		}
		mPrdmgFragment.updateCate();
	}

	public void addNew() {
		mProductCategory = new ProductCategory(-1, "", 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prdmg_kind_delete:
			mDialog = new AnyWhereDialog(getActivity(), 540, 280, 0, 0,
					R.layout.confirm_delete, R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, true);
			Button confirmBtn = (Button) mDialog.findViewById(R.id.confirm_delete);
			Button cancelBtn = (Button) mDialog.findViewById(R.id.cancel_delete);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			mDialog.show();
			break;
		case R.id.productmanageritembtn:
			save();
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

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			switch (v.getId()) {
			case R.id.prdmg_kind_editinput:
				Common.HideKeyboardIfExist(ProductManagerAddKindFragment.this);
				break;
			}
		}
		return false;
	}
}
