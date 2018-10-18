package com.zhuzb.itsPet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mob.jimu.biz.ReadOnlyProperty;
import com.mob.jimu.biz.ReadWriteProperty;
import com.mob.jimu.query.data.DataType;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.zhuzb.itsPet.dao.LoginDao;
import com.zhuzb.itsPet.model.login.LoginBean;
import com.zhuzb.itsPet.model.login.UserDataBean;
import com.zhuzb.itsPet.ui.acticity.LeftMainActivity;

import java.util.HashMap;

/**
 * @author zhuzb
 * @date on 2018/5/25 0025
 * @email zhuzhibo2017@163.com
 */

public class Login {
    /**
     * 登录
     * @param context
     */
    public static void loginUP(final Context context){
        UMSGUI.showLogin( new OperationCallback<User>() {
            public void onSuccess(User var1) {
                try {
                    DataType<ReadOnlyProperty<String>> id = (DataType<ReadOnlyProperty<String>>) var1.getCustomField( "userId" );
                    HashMap<String, ReadWriteProperty<?>> userData = var1.getDefaultFields();
                    UserDataBean userDataBean = new UserDataBean();
                    userDataBean.setUserId( String.valueOf( id.value() ) );
                    userDataBean.setUserName( String.valueOf( userData.get( "nickname" ).get() ) );
                    userDataBean.setUserTx( String.valueOf( userData.get( "signature" ).get() ) );
                    userDataBean.setUserImg( ((ReadWriteProperty<String[]>) userData.get( "avatar" )).get()[0] );
                    //保存登录信息
                    LoginDao loginDao =new LoginDao( context );
                    loginDao.addLogin( new Object[]{String.valueOf( id.value() ) ,String.valueOf( userData.get( "nickname" ).get() )} );

                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递userData参数为userDataBean
                    bundle.putSerializable( "userData", userDataBean );
                    Intent intent = new Intent( context, LeftMainActivity.class );
                    intent.putExtras( bundle );
                    context.startActivity( intent );
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } );
    }

    /**
     * 获取登录ID
     */
    public static String getUserId(final Context context){
        LoginDao loginDao =new LoginDao( context );
        LoginBean loginBean = loginDao.getLogin();
        return loginBean.getUserName();
    }
    /**
     * 获取登录名
     */
    public static String getUserName(final Context context){
        LoginDao loginDao =new LoginDao( context );
        LoginBean loginBean = loginDao.getLogin();
        return loginBean.getPassword();
    }
}
