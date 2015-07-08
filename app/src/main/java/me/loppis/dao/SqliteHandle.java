package me.loppis.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHandle extends SQLiteOpenHelper {
	public static final int VERSION = 1;
	public static final String DBName = "YoGe.db";

	public SqliteHandle(Context context) {
		super(context, DBName, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table todo (id varchar(20) primary key,name varchar(20),time varchar(20),status varchar(20),statuscode varchar(20))");
		db.execSQL("create table finish (id varchar(20) primary key,name varchar(20),time varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
