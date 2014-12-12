package com.cynovo.sirius.core;

import android.database.sqlite.SQLiteDatabase;

import com.cynovo.sirius.sqlite.LocalDatabase;

public class ManagerBase {
	
	protected static SQLiteDatabase msqlitedb;
	protected static LocalDatabase mlocaldb;
	
	static {
		mlocaldb = LocalDatabase.getInstance();
		msqlitedb = mlocaldb.getSQLiteDatabase();
	}

}
