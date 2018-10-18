package com.zhuzb.itsPet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author zhuzb
 * @date on 2018/5/23 0023
 * @email zhuzhibo2017@163.com
 */

public class ItsDB extends SQLiteOpenHelper {
    private final static int version = 1;
    private final static String DBNAME = "its_localhost.db";

    public ItsDB(Context context) {
        super( context, DBNAME, null, version );

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "create table tb_user (user nvarchar(60) primary key, paw varchar(30))" ); //用户表
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


    }
}