package com.zhuzb.itsPet.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhuzb.itsPet.db.ItsDB;
import com.zhuzb.itsPet.model.login.LoginBean;

/**
 * @author zhuzb
 * @date on 2018/5/23 0023
 * @email zhuzhibo2017@163.com
 */

public class LoginDao {
    private SQLiteDatabase db;
    private ItsDB dbHelper;
    private Context context;
    public LoginDao(Context context){
        this.context = context;
    }

    /**
     * 登录
     * @param parms
     */
    public void addLogin(Object[]  parms){
        dbHelper = new ItsDB(context);
        db = dbHelper.getWritableDatabase();// 只生成数据库
        db.execSQL("delete from tb_user");//清空表
        String sql = "insert into tb_user values(?,?)";
        //execSQL：用来执行增加、修改、删除操作
        db.execSQL(sql, parms);
    }

    /**
     * 获取登录
     * @return
     */
    public LoginBean getLogin(){
        LoginBean loginBean = new LoginBean();
        dbHelper = new ItsDB( context );
        db = dbHelper.getWritableDatabase();
        String sql = " SELECT * FROM tb_user ";
        //查询时需要调用
        Cursor rawQuery = db.rawQuery(sql,null);
        while(rawQuery.moveToNext()){
            loginBean .setUserName( rawQuery.getString( 0 ) );
            loginBean .setPassword( rawQuery.getString( 1 ) );
        }
        return loginBean;
    }

    /**
     * 退出登录
     */
    public void outLogin(){
        dbHelper = new ItsDB( context );
        db = dbHelper.getWritableDatabase();
        //查询时需要调用
        db.execSQL("delete from tb_user");//清空表
    }
}
