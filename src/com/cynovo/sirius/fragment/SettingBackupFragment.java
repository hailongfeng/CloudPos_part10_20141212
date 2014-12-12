package com.cynovo.sirius.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.order.Onsale;
import com.cynovo.sirius.core.order.OrderContent;
import com.cynovo.sirius.core.order.OrderContentData;
import com.cynovo.sirius.core.order.OrderManager;
import com.cynovo.sirius.core.product.Product;
import com.cynovo.sirius.core.product.ProductAttribute;
import com.cynovo.sirius.core.product.ProductCategory;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.core.product.ProductSubAttribute;
import com.cynovo.sirius.core.product.ProductToAttribute;
import com.cynovo.sirius.sqlite.BackupDatabase;
import com.cynovo.sirius.sqlite.LocalDatabase;
import com.cynovo.sirius.sqlite.MySharedPreferencesEdit;
import com.cynovo.sirius.util.CloudPosApp;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.CynovoConstant;
import com.cynovo.sirius.util.ExcelHelper;
import com.cynovo.sirius.util.JavaUtil;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.util.XZip;

public class SettingBackupFragment extends AbstractLoadingFragment implements
		OnClickListener {
	View result = null;
	Button mExportBtn = null;
	Button mImportBtn = null;
	Button btn_salse_export = null;
	String mBackupTime = null;
	TextView mTimeTextView = null;
	boolean mImport = true;
	boolean mExport = true;
	int retval = -1;
	int retval1 = -1;
	final static int importsuccess = 0;
	final static int importfilefail = 1;
	final static int importdevicefail = 2;
	final static int importfilefail2 = 3;
	final static int importdbnotnull = 4;
	final static int importfail = 5;

	final static int exportsuccess = 10;
	final static int exportfilefail = 11;
	final static int exportdevicefail = 12;
	final static int exportfail = 13;

	ProgressDialog pd = null;

	String export_excel_dir = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		result = inflater.inflate(R.layout.update_backup, container, false);
		mExportBtn = (Button) result.findViewById(R.id.checkexport_btn);
		mImportBtn = (Button) result.findViewById(R.id.checkimport_btn);
		btn_salse_export = (Button) result.findViewById(R.id.btn_salse_export);
		mTimeTextView = (TextView) result
				.findViewById(R.id.checkbackup_time_show);
		mExportBtn.setOnClickListener(this);
		mImportBtn.setOnClickListener(this);
		btn_salse_export.setOnClickListener(this);
		mBackupTime = MySharedPreferencesEdit.getInstancePublic(getActivity())
				.getDBBackupTime();
		if (mBackupTime != null)
			mTimeTextView.setText(mBackupTime);
		return result;
	}

	private static final int excel_failure = 0;
	private static final int excel_success = 1;
	private static final int excel_error_file_not_found = 2;
	private static final int excel_error_unknow_error = 3;
	private static final int excel_error_database_not_empty = 4;

	private static final int excel_type_import = 1;
	private static final int excel_type_export = 2;

	Handler myExcelHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			Toast.makeText(CloudPosApp.getInstance(), (String) msg.obj,
					Toast.LENGTH_LONG).show();
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkimport_btn:
			if (mImport) {
				// StartImportDB();
				SQLiteDatabase db = LocalDatabase.getInstance()
						.getSQLiteDatabase();
				boolean isNull = CheckDBisNull(db);
				if (!isNull) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage("数据库不为空，是否覆盖掉目前的数据？")
							.setCancelable(false)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
											mImport = false;
											pd = ProgressDialog.show(
													getActivity(), "提示",
													"正在导入数据...", true, false);
											if (ProductManager
													.deleteAllProduct()) {
												startImportBusinessData();
											} else {
												mImport = false;
												Toast.makeText(
														CloudPosApp
																.getInstance(),
														"数据清除失败",
														Toast.LENGTH_LONG)
														.show();
											}
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				} else {
					mImport = false;
					pd = ProgressDialog.show(getActivity(), "提示", "正在导入数据...",
							true, false);
					ProductManager.deleteAllProduct();
					startImportBusinessData();
				}

			}
			break;
		case R.id.checkexport_btn:
			if (mExport) {
				mExport = false;
				StartExportDB();
			}
		case R.id.btn_salse_export:
			if (mExport) {
				mExport = false;
				pd = ProgressDialog.show(getActivity(), "提示", "正在导出数据...",
						true, false);
				startExportBusinessOrders();
			}
			break;
		}
	}

	// private boolean exportDB() {
	// CloudPosApp app = CloudPosApp.getInstance();
	// boolean isExist = DBHelper.isDataBaseExist(app);
	// if(isExist != true)
	// return false;
	//
	// File storage = new File(CynovoConstant.STORE_BASE_PATH);
	// if(storage.canWrite()) {
	// File backup = new File(storage, "backup.sql");
	// if(!backup.exists())
	// try {
	// backup.createNewFile();
	// } catch (IOException e) {
	// return false;
	// }
	//
	// String cmd = "sqlite3 " + DBHelper.getDataBaseFilePath() +
	// " .dump > /mnt/sdcard/DCIM/backup.sql";
	// Log.i("checkexport_btn", cmd);
	// try {
	// Runtime.getRuntime().exec(cmd);
	// } catch (IOException e) {
	// return false;
	// }
	// return true;
	// }
	// else
	// return false;
	// }
	//
	// private boolean importDB() {
	//
	// File storage = new File(CynovoConstant.STORE_BASE_PATH);
	// if(storage.canRead()) {
	// File backup = new File(storage, "backup.sql");
	// if(!backup.exists())
	// return false;
	//
	// CloudPosApp app = CloudPosApp.getInstance();
	// boolean isExist = DBHelper.isDataBaseExist(app);
	// if(isExist == true) {
	// String dbname = DBHelper.getDataBaseFilePath();
	// File file = new File(dbname);
	// if (file.isFile() && file.exists()) {
	// // 为文件时调用删除文件方法
	// file.delete();
	// }
	// }
	//
	// String cmd = "sqlite3 " + DBHelper.getDataBaseFilePath() +
	// " < /mnt/sdcard/DCIM/backup.sql";
	// Log.i("checkimport_btn", cmd);
	// try {
	// Runtime.getRuntime().exec(cmd);
	// } catch (IOException e) {
	// return false;
	// }
	// return true;
	// }
	// else
	// return false;
	// }

	private int exportDB() {

		File storage = new File(CynovoConstant.STORE_BACKUP_PATH);
		SQLiteDatabase DB = null;
		if (!storage.canWrite()) {
			return -2;// Device not found!
		} else {
			File backup = new File(storage, CynovoConstant.DB_NAME);
			if (backup.exists())
				backup.delete();

			BackupDatabase backupDB = new BackupDatabase();
			try {
				backupDB.SetBackupDatabaseToNull();
				DB = backupDB.getAnInstance().getSQLiteDatabase();
			} catch (SQLiteException e) {
				e.printStackTrace();
				return -3;
			}
		}

		SQLiteDatabase db = LocalDatabase.getInstance().getSQLiteDatabase();
		List<Onsale> onsaleList = OrderManager.getAllOnSaleListForBackup(db);
		SaveOnSale(DB, onsaleList);

		List<Product> productList = ProductManager.getALLProductForBackup(db);
		SaveProduct(DB, productList);

		List<ProductAttribute> productAttrList = ProductManager
				.getAllProductAttributeForBackup(db);
		SaveProductAttribute(DB, productAttrList);

		List<ProductCategory> productCateList = ProductManager
				.getAllProductCategoryForBackup(db);
		SaveProductCategory(DB, productCateList);

		List<ProductSubAttribute> productSubAttrList = ProductManager
				.getAllProductsSubAttributesForBackup(db);
		SaveProductSubAttribute(DB, productSubAttrList);

		List<ProductToAttribute> productToAttrList = ProductManager
				.getAllProductToAttributeForBackUp(db);
		SaveProductToAttribute(DB, productToAttrList);

		try {
			XZip.ZipFolder(CynovoConstant.STORE_BACKUP_FOLDER,
					CynovoConstant.STORE_BASE_PATH
							+ CynovoConstant.STORE_BACKUP_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -4;
		}

		return 0;
	}

	void startImportBusinessData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retval = importBusinessData();
				Message message = new Message();
				if (retval == excel_success) {
					MyLog.i("checkimport_btn", "OK");
					message.what = excel_success;
					message.obj = Common
							.getString(R.string.importsuccess_prompt);
				} else if (retval == excel_error_database_not_empty) {
					message.what = excel_error_database_not_empty;
					message.obj = Common
							.getString(R.string.importfail_DBnotNull);
				} else if (retval == excel_error_file_not_found) {
					message.what = excel_error_file_not_found;
					message.obj = Common.getString(R.string.importfail_file);
				} else {
					message.what = excel_error_unknow_error;
					message.obj = Common.getString(R.string.importfail_prompt);
				}
				SettingBackupFragment.this.myExcelHandler.sendMessage(message);
				mImport = true;
			}
		}).start();
	}

	void startExportBusinessOrders() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int result = exportBusinessOrders();
				Message message = new Message();
				if (result == excel_success) {
					MyLog.i("checkimport_btn", "OK");
					message.what = excel_success;
					message.obj = Common
							.getString(R.string.exportsuccess_prompt)
							+ "目录为："
							+ export_excel_dir;
				} else if (result == excel_error_database_not_empty) {
					message.what = excel_error_database_not_empty;
					message.obj = Common
							.getString(R.string.importfail_DBnotNull);
				} else if (result == excel_error_file_not_found) {
					message.what = excel_error_file_not_found;
					message.obj = Common.getString(R.string.importfail_file);
				} else {
					message.what = excel_error_unknow_error;
					message.obj = Common.getString(R.string.importfail_prompt);
				}
				SettingBackupFragment.this.myExcelHandler.sendMessage(message);
				mExport = true;
			}
		}).start();
	}

	/**
	 * 导出销售记录
	 * 
	 * @return
	 */
	private int exportBusinessOrders() {

		SQLiteDatabase db = LocalDatabase.getInstance().getSQLiteDatabase();
		Workbook workbook = new HSSFWorkbook();
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);// 表格内容靠左对齐
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
		// 创建第一张表单
		Sheet sheet1 = workbook.createSheet("销售记录");
		List<OrderContentData> orderdatalist = OrderManager
				.getAllBusinessOrders(db);
		/*
		 * 商家，产品，单价,数量,订单金额，优惠名称，优惠内容，订单时间
		 */
		Object[] valuesTitle = { "订单ID", "商家名称", "商品名称", "单价", "数量", "总金额",
				"优惠名称", "订单时间" };
		ExcelHelper.insertRow(sheet1, 0, valuesTitle, cellStyle);
		for (int i = 0; i < orderdatalist.size(); i++) {
			OrderContentData ocd = orderdatalist.get(i);
			Product p = ocd.getProduct();
			OrderContent oc = ocd.getOrderContent();
			List<Onsale> onSales = ocd.getOnsale();
			StringBuilder onSaleContent = new StringBuilder();
			for (Onsale onsale : onSales) {
				onSaleContent.append(onsale.getOnsaleName() + "，");
			}
			if (onSaleContent.toString().isEmpty()) {
				onSaleContent.append("无优惠");
			} else {
				onSaleContent.deleteCharAt(onSaleContent.length() - 1);
			}
			MyLog.d("oc.getCreateTime():" + oc.getCreateTime());
			Object[] values0 = { oc.getOrderId(), p.getProductCategoryName(),
					p.getName(), p.getPrice(), oc.getCount(), oc.getAmount(),
					onSaleContent, new Date(oc.getCreateTime()) };
			ExcelHelper.insertRow(sheet1, i + 1, values0, cellStyle);
		}
		// 保存文档
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "sales" + formatter.format(d) + ".xls";
		File file = new File(CynovoConstant.STORE_BUSINESSDATA_EXPORT_SALES,
				fileName);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			workbook.write(fos);
			export_excel_dir = file.getAbsolutePath();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return excel_error_unknow_error;
		}
		return excel_success;
	}

	/**
	 * 导入商家和商家产品
	 * 
	 * @return
	 */
	private int importBusinessData() {

		SQLiteDatabase db = LocalDatabase.getInstance().getSQLiteDatabase();
		File business = new File(
				CynovoConstant.STORE_BUSINESSDATA_BACKUP_PRODUCT);
		if (!business.exists()) {
			return excel_error_file_not_found;
		} else {
			InputStream inStream;
			Workbook wb = null;
			try {
				inStream = new FileInputStream(business);
				// HSSF
				wb = new HSSFWorkbook(inStream);
				inStream.close();
			} catch (IOException e) {
				MyLog.d(e.getMessage());
			}
			if (wb == null) {
				MyLog.d("读取Excel文档出错");
				return excel_error_unknow_error;
			}
			// DBAdapter 封装了SQLite的所有操作
			Sheet sProduct = wb.getSheetAt(0);// 第一张表单
			Sheet sBusiness = wb.getSheetAt(1);
			if (sProduct == null) {
				return excel_error_unknow_error;
			}
			if (sBusiness == null) {
				return excel_error_unknow_error;
			}

			List<ProductCategory> productCateList = new ArrayList<ProductCategory>();
			Iterator<Row> rit = sBusiness.rowIterator();
			if (rit.hasNext()) {
				rit.next();
			}
			for (; rit.hasNext();) {
				Row row = rit.next();
				ProductCategory pt = new ProductCategory(((int) row.getCell(
						ExcelHelper.ProductCategorySheet.productCategoryId)
						.getNumericCellValue()), row.getCell(
						ExcelHelper.ProductCategorySheet.name)
						.getStringCellValue(), (int) row.getCell(
						ExcelHelper.ProductCategorySheet.enable)
						.getNumericCellValue());
				productCateList.add(pt);
			}

			List<Product> productList = new ArrayList<Product>();
			Iterator<Row> rit1 = sProduct.rowIterator();
			if (rit1.hasNext()) {
				rit1.next();
			}
			for (; rit1.hasNext();) {
				Row row = rit1.next();
				Product p = new Product((int) row.getCell(
						ExcelHelper.ProductSheet.productID)
						.getNumericCellValue(), (int) row.getCell(
						ExcelHelper.ProductSheet.productCategoryID)
						.getNumericCellValue(), row.getCell(
						ExcelHelper.ProductSheet.name).getStringCellValue(),
						row.getCell(ExcelHelper.ProductSheet.price)
								.getNumericCellValue() + "", row.getCell(
								ExcelHelper.ProductSheet.photo)
								.getStringCellValue(), (int) row.getCell(
								ExcelHelper.ProductSheet.enable)
								.getNumericCellValue());
				productList.add(p);
			}
			saveExcelData(db, productCateList, productList);
			//开始导入图片
			ProductManager.InitPinyinTable();
		}
		return excel_success;
	}

	private void saveExcelData(SQLiteDatabase DB,
			List<ProductCategory> productCateList, List<Product> productList) {
		DB.beginTransaction();
		try {
			Iterator<ProductCategory> iterator = productCateList.iterator();
			while (iterator.hasNext()) {
				ProductCategory pc = iterator.next();
				ContentValues values = new ContentValues();

				int productCategoryId = pc.getProductCategoryId();
				values.put("productCategoryID", productCategoryId);

				String name = pc.getProductCategoryName();
				values.put("name", name);

				values.put("enable", 1);

				DB.insertWithOnConflict("ProductCategory", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}

			Iterator<Product> iterator1 = productList.iterator();
			while (iterator1.hasNext()) {
				Product p = iterator1.next();
				ContentValues values = new ContentValues();

				int productID = p.getProductID();
				values.put("productID", productID);

				int productCategoryID = p.getProductCategoryID();
				values.put("productCategoryID", productCategoryID);

				String name = p.getName();
				values.put("name", name);

				String price = p.getPrice();
				values.put("price", price);

				String photo = p.getProductPhoto();
				values.put("productPhoto", photo);

				values.put("enable", 1);

				DB.insertWithOnConflict("Product", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}
	}

	/**
	 * 导入数据库
	 * 
	 * @return
	 */
	private int importDB() {
		SQLiteDatabase db = LocalDatabase.getInstance().getSQLiteDatabase();
		boolean isNull = CheckDBisNull(db);
		if (!isNull)
			return -5;

		try {
			XZip.UnZipFolder(CynovoConstant.STORE_BASE_PATH
					+ CynovoConstant.STORE_BACKUP_NAME,
					CynovoConstant.STORE_BASE_PATH);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -4;
		}

		File storage = new File(CynovoConstant.STORE_BACKUP_PATH);
		if (!storage.canRead()) {
			return -2;
		} else {
			File backup = new File(storage, CynovoConstant.DB_NAME);
			if (!backup.exists())
				return -1;

			BackupDatabase backupDB = new BackupDatabase();
			SQLiteDatabase DB = null;
			try {
				DB = backupDB.getAnInstance().getSQLiteDatabase();
			} catch (SQLiteException e) {
				e.printStackTrace();
				return -3;
			}

			List<Onsale> onsaleList = OrderManager
					.getAllOnSaleListForBackup(DB);
			SaveOnSale(db, onsaleList);

			List<Product> productList = ProductManager
					.getALLProductForBackup(DB);
			SaveProduct(db, productList);

			List<ProductAttribute> productAttrList = ProductManager
					.getAllProductAttributeForBackup(DB);
			SaveProductAttribute(db, productAttrList);

			List<ProductCategory> productCateList = ProductManager
					.getAllProductCategoryForBackup(DB);
			SaveProductCategory(db, productCateList);

			List<ProductSubAttribute> productSubAttrList = ProductManager
					.getAllProductsSubAttributesForBackup(DB);
			SaveProductSubAttribute(db, productSubAttrList);

			List<ProductToAttribute> productToAttrList = ProductManager
					.getAllProductToAttributeForBackUp(DB);
			SaveProductToAttribute(db, productToAttrList);

			ProductManager.InitPinyinTable();

			return 0;
		}
	}

	private int copyStoD(String sFilePath, String dFilePath) {
		if (sFilePath == null || dFilePath == null)
			return -3;// Parameters error!

		FileInputStream is = null;
		try {
			is = new FileInputStream(sFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return -4;// Read file error!
		}// 得到数据库文件的数据流

		FileOutputStream os = null;
		try {
			os = new FileOutputStream(dFilePath);// 得到数据库文件的写入流
		} catch (FileNotFoundException e) {
			return -5;// Write file error!
		}

		try {
			JavaUtil.copyStream(is, os);
		} catch (IOException e) {
			e.printStackTrace();
			return -6;// Copy file error!
		} finally {
			// close file stream
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return 0;// success
	}

	public void SaveOnSale(SQLiteDatabase DB, List<Onsale> onsaleList) {

		DB.beginTransaction();
		try {
			Iterator<Onsale> iterator = onsaleList.iterator();
			while (iterator.hasNext()) {
				Onsale os = iterator.next();
				ContentValues values = new ContentValues();

				int onsaleID = os.getOnsaleID();
				values.put("onsaleID", onsaleID);

				int onsaleType = os.getOnsaleType();
				values.put("onsaleType", onsaleType);

				String onsaleName = os.getOnsaleName();
				values.put("onsaleName", onsaleName);

				String value = os.getValue();
				values.put("value", value);

				values.put("enable", 1);

				DB.insertWithOnConflict("Onsale", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}

	}

	public void SaveProduct(SQLiteDatabase DB, List<Product> productList) {

		DB.beginTransaction();
		try {
			Iterator<Product> iterator = productList.iterator();
			while (iterator.hasNext()) {
				Product p = iterator.next();
				ContentValues values = new ContentValues();

				int productID = p.getProductID();
				values.put("productID", productID);

				int productCategoryID = p.getProductCategoryID();
				values.put("productCategoryID", productCategoryID);

				String name = p.getName();
				values.put("name", name);

				String price = p.getPrice();
				values.put("price", price);

				String photo = p.getProductPhoto();
				values.put("productPhoto", photo);

				values.put("enable", 1);

				DB.insertWithOnConflict("Product", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}
	}

	public void SaveProductAttribute(SQLiteDatabase DB,
			List<ProductAttribute> productAttrList) {

		DB.beginTransaction();
		try {
			Iterator<ProductAttribute> iterator = productAttrList.iterator();
			while (iterator.hasNext()) {
				ProductAttribute pa = iterator.next();
				ContentValues values = new ContentValues();

				int productAttributeID = pa.getProductAttributeID();
				values.put("productAttributeID", productAttributeID);

				int choiceType = pa.getChoiceType();
				values.put("choiceType", choiceType);

				String name = pa.getName();
				values.put("name", name);

				int defaultChoice = pa.getDefaultChoice();
				values.put("defaultChoice", defaultChoice);

				values.put("enable", 1);

				DB.insertWithOnConflict("ProductAttribute", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}
	}

	public void SaveProductCategory(SQLiteDatabase DB,
			List<ProductCategory> productCateList) {

		DB.beginTransaction();
		try {
			Iterator<ProductCategory> iterator = productCateList.iterator();
			while (iterator.hasNext()) {
				ProductCategory pc = iterator.next();
				ContentValues values = new ContentValues();

				int productCategoryId = pc.getProductCategoryId();
				values.put("productCategoryID", productCategoryId);

				String name = pc.getProductCategoryName();
				values.put("name", name);

				values.put("enable", 1);

				DB.insertWithOnConflict("ProductCategory", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}
	}

	public void SaveProductSubAttribute(SQLiteDatabase DB,
			List<ProductSubAttribute> productSAList) {

		DB.beginTransaction();
		try {
			Iterator<ProductSubAttribute> iterator = productSAList.iterator();
			while (iterator.hasNext()) {
				ProductSubAttribute pc = iterator.next();
				ContentValues values = new ContentValues();

				int productSubAttributeId = pc.getProductSubAttributeId();
				values.put("productSubAttributeId", productSubAttributeId);

				int productAttributeID = pc.getProductAttributeID();
				values.put("productAttributeID", productAttributeID);

				String name = pc.getName();
				values.put("name", name);

				String priceAffect = pc.getPriceAffect();
				values.put("priceAffect", priceAffect);

				values.put("enable", 1);

				DB.insertWithOnConflict("ProductSubAttribute", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}
	}

	public void SaveProductToAttribute(SQLiteDatabase DB,
			List<ProductToAttribute> productTAList) {

		DB.beginTransaction();
		try {
			Iterator<ProductToAttribute> iterator = productTAList.iterator();
			while (iterator.hasNext()) {
				ProductToAttribute pc = iterator.next();
				ContentValues values = new ContentValues();

				int productID = pc.getProductCategoryID();
				values.put("productID", productID);

				int productAttributeID = pc.getProductAttributeID();
				values.put("productAttributeID", productAttributeID);

				values.put("enable", 1);

				DB.insertWithOnConflict("ProductToAttribute", null, values,
						SQLiteDatabase.CONFLICT_IGNORE);
			}
			DB.setTransactionSuccessful();
		} finally {
			DB.endTransaction();
		}
	}

	public boolean CheckDBisNull(SQLiteDatabase sqlitedb) {
		String sql = "select case when c1 = 0 and c2 = 0 and c3 = 0 and c4 = 0 and c5 = 0 and c6 = 0 then 1 else 0 end result "
				+ "from (select count(*) c1 from `Onsale`), (select count(*) c2 from `Product`), "
				+ "(select count(*) c3 from `ProductAttribute`), (select count(*) c4 from `ProductCategory`), "
				+ "(select count(*) c5 from `ProductSubAttribute`), (select count(*) c6 from `ProductToAttribute`);";
		Cursor result = sqlitedb.rawQuery(sql, null);
		result.moveToFirst();
		int retval = result.getInt(0);
		if (retval == 1)
			return true;
		else
			return false;
	}

	private void StartImportDB() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retval = importDB();
				Message message = new Message();
				if (retval == 0) {
					MyLog.i("checkimport_btn", "OK");
					message.what = SettingBackupFragment.importsuccess;
				} else if (retval == -1) {
					message.what = SettingBackupFragment.importfilefail;
				} else if (retval == -2) {
					message.what = SettingBackupFragment.importdevicefail;
				} else if (retval == -4) {
					message.what = SettingBackupFragment.importfilefail2;
				} else if (retval == -5) {
					message.what = SettingBackupFragment.importdbnotnull;
				} else {
					message.what = SettingBackupFragment.importfail;
				}
				SettingBackupFragment.this.myHandler.sendMessage(message);
				mImport = true;
			}
		}).start();
	}

	private void StartExportDB() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				retval1 = exportDB();
				Message message = new Message();
				if (retval1 == 0) {
					MyLog.i("checkexport_btn", "OK");
					message.what = SettingBackupFragment.exportsuccess;
				} else if (retval1 == -4) {
					message.what = SettingBackupFragment.exportfilefail;
				} else if (retval1 == -2) {
					message.what = SettingBackupFragment.exportdevicefail;
				} else {
					message.what = SettingBackupFragment.exportfail;
				}
				SettingBackupFragment.this.myHandler.sendMessage(message);
				mExport = true;
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SettingBackupFragment.importsuccess:
				Toast.makeText(CloudPosApp.getInstance(),
						Common.getString(R.string.importsuccess_prompt),
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.importfilefail:
				Toast.makeText(
						CloudPosApp.getInstance(),
						Common.getString(R.string.importfail_file)
								+ CynovoConstant.STORE_BACKUP_PATH
								+ CynovoConstant.DB_NAME, Toast.LENGTH_SHORT)
						.show();
				break;
			case SettingBackupFragment.importdevicefail:
				Toast.makeText(
						CloudPosApp.getInstance(),
						Common.getString(R.string.importfail_device)
								+ CynovoConstant.STORE_BACKUP_PATH,
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.importfilefail2:
				Toast.makeText(
						CloudPosApp.getInstance(),
						Common.getString(R.string.importfail_file)
								+ CynovoConstant.STORE_BASE_PATH
								+ CynovoConstant.STORE_BACKUP_NAME,
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.importdbnotnull:
				Toast.makeText(CloudPosApp.getInstance(),
						Common.getString(R.string.importfail_DBnotNull),
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.importfail:
				Toast.makeText(CloudPosApp.getInstance(),
						Common.getString(R.string.importfail_prompt),
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.exportsuccess:
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						Common.DATE_TIME_FORMAT_WITH_SECONDS,
						Locale.getDefault());
				mBackupTime = sDateFormat.format(new java.util.Date());
				if (mBackupTime != null) {
					mTimeTextView.setText(mBackupTime);
					MySharedPreferencesEdit.getInstancePublic(getActivity())
							.setDBBackupTime(mBackupTime);
				}
				Toast.makeText(
						CloudPosApp.getInstance(),
						Common.getString(R.string.exportsuccess_prompt)
								+ CynovoConstant.STORE_BASE_PATH
								+ CynovoConstant.STORE_BACKUP_NAME,
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.exportfilefail:
				Toast.makeText(
						CloudPosApp.getInstance(),
						Common.getString(R.string.exportfail_file)
								+ CynovoConstant.STORE_BASE_PATH
								+ CynovoConstant.STORE_BACKUP_NAME,
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.exportdevicefail:
				Toast.makeText(
						CloudPosApp.getInstance(),
						Common.getString(R.string.exportfail_device)
								+ CynovoConstant.STORE_BACKUP_PATH,
						Toast.LENGTH_SHORT).show();
				break;
			case SettingBackupFragment.exportfail:
				Toast.makeText(CloudPosApp.getInstance(),
						Common.getString(R.string.exportfail_prompt),
						Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
}
