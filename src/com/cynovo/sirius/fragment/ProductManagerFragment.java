package com.cynovo.sirius.fragment;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cynovo.sirius.R;
import com.cynovo.sirius.adapter.ProductAttrManagerAdapter;
import com.cynovo.sirius.adapter.ProductCategoryManagerAdapter;
import com.cynovo.sirius.adapter.ProductManagerAdapter;
import com.cynovo.sirius.adapter.ProductOnsaleManagerAdapter;
import com.cynovo.sirius.core.order.Onsale;
import com.cynovo.sirius.core.order.OrderManager;
import com.cynovo.sirius.core.product.Product;
import com.cynovo.sirius.core.product.ProductAttribute;
import com.cynovo.sirius.core.product.ProductCategory;
import com.cynovo.sirius.core.product.ProductData;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.widget.MyImageButton1;

public class ProductManagerFragment extends AbstractFragment implements
		AdapterView.OnItemClickListener, OnClickListener {

	View result = null;
	BottomBar bottomBar = null;

//	ProductShowItemFragment mProductShowFragment;
	ProductManagerAddGoodsFragment mfm = null;
	ProductManagerAddKindFragment mkm = null;
	ProductManagerAttrFragment mam = null;
	ProductManagerOnSaleFragment msm = null;

	FragmentManager fragmentManager;

	ListView mCateListView = null;
	ListView mProductListView = null;
	ListView mProductAttrListView = null;
	ListView mProductOnsaleListView = null;

	ViewFlipper mViewFlipper = null;
	ViewFlipper mViewFlipperTop = null;
	Button mPrdMg_BackToallBtn;

	boolean mItemPressFlag = false;

	public List<Product> productList = null;
	ProductManagerAdapter mProductInfoAdapter = null;
	ProductCategoryManagerAdapter mProductCategoryManagerAdapter = null;
	ProductAttrManagerAdapter mProductAttrManagerAdapter = null;
	ProductOnsaleManagerAdapter mProductOnsaleManagerAdapter = null;

	Button mPrdmg_backtoall = null;

	Button addCate = null;
	Button addProduct = null;
	Button addNewAttrBtn = null;
	Button addNewOnsaleBtn = null;

	TextView mSecondProdectTextVIew = null;

	FragmentManager mFragmentManager = null;
	FragmentTransaction fragmentTransaction = null;
	
	int protectCate = 0;
	private static final int PRODUCT_CATE = 0;
	private static final int PRODUCT_GOODS = 1;
	private static final int PRODUCT_ATTR = 2;
	private static final int PRODUCT_ONSALE = 3;
	private int currentRightType = -1;
	private Fragment currentRightFragment = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		result = inflater.inflate(R.layout.productmanagerfragment_layout,
				container, false);
		
		bottomBar = new BottomBar(result);
		
		//商品选择列表上面显示的商品种类
		mSecondProdectTextVIew = (TextView) result
				.findViewById(R.id.product_second_summary);

		//四个商品列表
		mCateListView = (ListView) result
				.findViewById(R.id.catemanager_listView);
		mProductListView = (ListView) result
				.findViewById(R.id.productmanager_listView);
		mProductAttrListView = (ListView) result
				.findViewById(R.id.product_attr_listView);
		mProductOnsaleListView = (ListView) result
				.findViewById(R.id.product_onsale_listView);

		//4个添加按钮
		addNewAttrBtn = (Button) result
				.findViewById(R.id.productmanager_add_attr);
		addCate = (Button) result
				.findViewById(R.id.productmanager_add_goods_kinds);
		addProduct = (Button) result.findViewById(R.id.add_product);
		addNewOnsaleBtn = (Button) result
				.findViewById(R.id.productmanager_add_onsale);

		addNewOnsaleBtn.setOnClickListener(this);
		addCate.setOnClickListener(this);
		addProduct.setOnClickListener(this);
		addNewAttrBtn.setOnClickListener(this);

		
		//列表上面的工具栏
		mViewFlipperTop = (ViewFlipper) result
				.findViewById(R.id.prodcutmanagerViewFlipper);
		mViewFlipperTop.setDisplayedChild(0);

		//商品列表界面后退按钮，可以后退到商品种类界面
		
		mPrdMg_BackToallBtn = (Button) result
				.findViewById(R.id.prdmg_backtoall);
		mPrdMg_BackToallBtn.setOnClickListener(this);
		
		mViewFlipper = (ViewFlipper) result
				.findViewById(R.id.prodect_manager_list);

		mProductCategoryManagerAdapter = new ProductCategoryManagerAdapter(
				this, ProductManager.getAllProductCategory());
		mCateListView.setAdapter(mProductCategoryManagerAdapter);

		mProductAttrManagerAdapter = new ProductAttrManagerAdapter(this,
				ProductManager.getAllProductAttribute());
		mProductAttrListView.setAdapter(mProductAttrManagerAdapter);

		mProductOnsaleManagerAdapter = new ProductOnsaleManagerAdapter(this,
				OrderManager.getAllOnSaleList());
		mProductOnsaleListView.setAdapter(mProductOnsaleManagerAdapter);

		mCateListView.setOnItemClickListener(this);
		mProductListView.setOnItemClickListener(this);
		mProductAttrListView.setOnItemClickListener(this);
		mProductOnsaleListView.setOnItemClickListener(this);

		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		//商品分类点击事件
		case R.id.catemanager_listView:
			mItemPressFlag = true;
			mViewFlipper.setDisplayedChild(1);
			mViewFlipperTop.setDisplayedChild(1);
			
			//获取选中了哪个商品种类
			int type = (int) arg0.getItemIdAtPosition(arg2);
			int cate = -1;
			if (type > 0) {
				ProductCategory mProductCategory = (ProductCategory) arg0
						.getItemAtPosition(arg2);
				cate = mProductCategory.getProductCategoryId();
				mSecondProdectTextVIew.setText(mProductCategory
						.getProductCategoryName());

			} else {
				mSecondProdectTextVIew.setText(R.string.allname);
			}
			protectCate = cate;
			mProductInfoAdapter = new ProductManagerAdapter(this, cate);
			mProductListView.setAdapter(mProductInfoAdapter);
			this.deleteFragment();
			break;
		}
	}
	
	void showRightFragment(int fragtype) {
		currentRightType = fragtype;
		switch(fragtype)
		{
		case PRODUCT_CATE:
			mkm = new ProductManagerAddKindFragment();
			addNewFragment(mkm);
			break;
		case PRODUCT_GOODS:
			mfm = new ProductManagerAddGoodsFragment();
			addNewFragment(mfm);
			break;
		case PRODUCT_ATTR:
			mam = new ProductManagerAttrFragment();
			addNewFragment(mam);
			break;
		case PRODUCT_ONSALE:
			msm = new ProductManagerOnSaleFragment();
			addNewFragment(msm);
			break;
		default:
			break;
		}
	}
	
	void hideRightFragment(int fragtype)
	{
		if(((currentRightType == PRODUCT_CATE || currentRightType == PRODUCT_GOODS) && fragtype == BottomBar.GOODS_CATE) ||
				(currentRightType == PRODUCT_ATTR && fragtype == BottomBar.GOODS_ATTR) ||
				(currentRightType == PRODUCT_ONSALE && fragtype == BottomBar.GOODS_ONSALE) )
		{
			return;
		}
		deleteFragment();
	}
	
	void addNewFragment(Fragment fragment)
	{
		currentRightFragment = fragment;
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, 
				R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
		fragmentTransaction.replace(R.id.productShowItemFragment,fragment);
		fragmentTransaction.commit();
	}
	
	void deleteFragment()
	{
		if(currentRightFragment != null)
		{
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.obj_push_left_in, 
					R.anim.obj_push_left_out,R.anim.obj_push_right_in, R.anim.obj_push_right_out); 
			fragmentTransaction.remove(currentRightFragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
	}
	
	public void setEditProdect(int productID) {
		ProductData mProductData = new ProductData();
		mProductData
				.setProduct(ProductManager.getProductByProductId(productID));
		showRightFragment(PRODUCT_GOODS);
		mfm.setProductData(mProductData);
	}

	public void setEditProductCate(int productCate) {
		ProductCategory mProductCategory = ProductManager
				.getProductCategory(productCate);
		showRightFragment(PRODUCT_CATE);
		mkm.setProductCategory(mProductCategory);
	}

	public void setEditProductAttr(int productattrid) {
		ProductAttribute mProductAttribute = ProductManager
				.getProductAttributeByAttrId(productattrid);
		showRightFragment(PRODUCT_ATTR);
		mam.setProductAttr(mProductAttribute);
	}

	public void setEditProductOndale(int onsaleID) {
		Onsale mOnsale = ProductManager.getOnSaleByid(onsaleID);
		showRightFragment(PRODUCT_ONSALE);
		msm.setOnSale(mOnsale);
	}

	private void setParamsFragment(Fragment fragment){
		Bundle bundle = new Bundle();
		bundle.putInt("result", 1);
		fragment.setArguments(bundle);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prdmg_backtoall:
			mItemPressFlag = false;
			backtofirstkind();
			break;
		case R.id.add_product:
			showRightFragment(PRODUCT_GOODS);
			mfm.addNew(protectCate);
			setParamsFragment(mfm);
			break;
		case R.id.productmanager_add_goods_kinds:
			showRightFragment(PRODUCT_CATE);
			mkm.addNew();
			setParamsFragment(mkm);
			break;
		case R.id.productmanager_add_attr:
			showRightFragment(PRODUCT_ATTR);
			setParamsFragment(mam);
			break;
		case R.id.productmanager_add_onsale:
			showRightFragment(PRODUCT_ONSALE);
			msm.addNewOnsale();
			setParamsFragment(msm);
			break;
		default:
			break;
		}

	}

	/**
	 * 更新产品信息
	 */
	public void updateProduct() {
		mProductInfoAdapter.updateProductList();
	}

	/**
	 * 更新分类信息
	 */
	public void updateCate() {
		mProductCategoryManagerAdapter.updateCategory(ProductManager
				.getAllProductCategory());
	}

	/**
	 * 更新属性信息
	 */
	public void updateAttr() {
		mProductAttrManagerAdapter.updateAttr(ProductManager
				.getAllProductAttribute());
	}

	/**
	 * 更新优惠信息
	 */
	public void updateOnsale() {
		mProductOnsaleManagerAdapter.updateOnsale(OrderManager
				.getAllOnSaleList());
	}

	private void backtofirstkind() {		
		mViewFlipper.setDisplayedChild(0);
		mViewFlipperTop.setDisplayedChild(0);
		this.deleteFragment();
	}
	
	
	void ButtonHasBeenSelected(int index) {
		hideRightFragment(index);
		switch (index) {
		case BottomBar.GOODS_CATE:
			if (mItemPressFlag) {
				mViewFlipperTop.setDisplayedChild(1);
				mViewFlipper.setDisplayedChild(1);

			} else {
				mViewFlipper.setDisplayedChild(0);
				mViewFlipperTop.setDisplayedChild(0);
			}
			break;
		case BottomBar.GOODS_ATTR:
			mViewFlipper.setDisplayedChild(2);
			mViewFlipperTop.setDisplayedChild(2);
			break;
		case BottomBar.GOODS_ONSALE:
			mViewFlipper.setDisplayedChild(3);
			mViewFlipperTop.setDisplayedChild(3);
			break;

		default:
			break;
		}
	}
	
	class BottomBar
	{
		public static final int GOODS_CATE = 0;
		public static final int GOODS_ATTR = 1;
		public static final int GOODS_ONSALE = 2;
		
		int fristOnDown = R.drawable.goods_list_button0;
		int firstOnUp = R.drawable.goods_list_button1;
		int secondOnDown = R.drawable.keyboard_button1;
		int secondOnUp = R.drawable.keyboard_button0;
		int thirdOnDown = R.drawable.goods_list_button0;
		int thirdOnUp = R.drawable.goods_list_button1;
		
		MyImageButton1 mKindBtn;
		MyImageButton1 mAttrBtn;
		MyImageButton1 mOnsaleBtn;
		
		BottomBar(View v)
		{
			mKindBtn = (MyImageButton1) result.
					findViewById(R.id.productmanagergood_kind);
			mAttrBtn = (MyImageButton1) result.
					findViewById(R.id.productmanager_goodsattr);
			mOnsaleBtn = (MyImageButton1) result.
					findViewById(R.id.productmanager_goodonsale);
			
			mKindBtn.setFlag(true);
			
			mKindBtn.setText(GOODS_CATE);
			mAttrBtn.setText(GOODS_ATTR);
			mOnsaleBtn.setText(GOODS_ONSALE);
			
			init_imageButton(mKindBtn, mAttrBtn, mOnsaleBtn, fristOnDown,
					firstOnUp, secondOnDown, thirdOnDown, GOODS_CATE);
			init_imageButton(mAttrBtn, mKindBtn, mOnsaleBtn, secondOnDown,
					secondOnUp, fristOnDown, thirdOnDown, GOODS_ATTR);
			init_imageButton(mOnsaleBtn, mKindBtn, mAttrBtn, thirdOnDown,
					thirdOnUp, fristOnDown, secondOnDown, GOODS_ONSALE);
		}
		
		public void setClicked(int index)
		{
			deal_button(index);
		}
		
		private void deal_button(int whichone) {
			switch (whichone) {
			case GOODS_CATE:
				setImageButtonFlag(true, false, false);
				break;
			case GOODS_ATTR:
				setImageFlag(false, true, false);
				break;
			case GOODS_ONSALE:
				setImageFlag(false, false, true);
				break;
			default:
				break;
			}
			ButtonHasBeenSelected(whichone);
		}
		
		private void setImageButtonFlag(boolean flag1, boolean flag2, boolean flag3) {
			setImageFlag(flag1, flag2, flag3);
			mKindBtn.setBackgroundResource(firstOnUp);
			mAttrBtn.setBackgroundResource(secondOnDown);
			mOnsaleBtn.setBackgroundResource(thirdOnDown);
		}

		private void setImageFlag(boolean flag1, boolean flag2, boolean flag3) {
			mKindBtn.setFlag(flag1);
			mAttrBtn.setFlag(flag2);
			mOnsaleBtn.setFlag(flag3);
		}
		
		private void init_imageButton(final MyImageButton1 first,
				final MyImageButton1 second, final MyImageButton1 third,
				final int OnDown, final int OnUp, final int onUp1, final int onUp2,
				final int whichone) {
			first.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						first.setBackgroundResource(OnDown);
						if (!first.getFlag()) {
							second.setBackgroundResource(onUp1);
							third.setBackgroundResource(onUp2);
							first.setFlag(true);
							second.setFlag(false);
							third.setFlag(false);
							deal_button(whichone);
						}
					} else if (event.getAction() == MotionEvent.ACTION_UP) {

						first.setBackgroundResource(OnUp);
					}
					return false;
				}
			});
		}
	}

}
