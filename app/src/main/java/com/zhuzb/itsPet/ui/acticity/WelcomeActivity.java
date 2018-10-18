package com.zhuzb.itsPet.ui.acticity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.mob.jimu.biz.ReadOnlyProperty;
import com.mob.jimu.biz.ReadWriteProperty;
import com.mob.jimu.query.data.DataType;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.dao.LoginDao;
import com.zhuzb.itsPet.model.login.UserDataBean;
import com.zhuzb.itsPet.ui.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mob.wrappers.UMSSDKWrapper.logout;

/**
 * 说明： 启动页页面
 * 作者： 朱志波
 * 时间： 2018/4/5 0005 16:11
 * 邮箱： zhuzhibo2017@163.com
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome );  //加载整体布局
        //欢迎界面全屏倒计时
        new Handler( new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
//                Intent intent = new Intent( getApplicationContext(), LeftMainActivity.class );
//                startActivity( intent );
                Login.loginUP( getApplicationContext() );//登录
                finish();
                return false;
            }
        } ).sendEmptyMessageDelayed( 0, 1500 );//延时2秒执行任务
    }

    /**
     * 说明： 返回键监听
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:40
     * 邮箱： zhuzhibo2017@163.com
     */
    @Override
    public void onBackPressed() {
        return;
    }
}
