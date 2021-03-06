package com.zhuzb.itsPet.ui.acticity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.jimu.biz.ReadWriteProperty;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.squareup.picasso.Picasso;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.model.login.UserDataBean;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.fragment.FragmentItsTVA;
import com.zhuzb.itsPet.ui.fragment.FragmentItsTVB;
import com.zhuzb.itsPet.ui.fragment.FragmentMessageTVA;
import com.zhuzb.itsPet.ui.fragment.FragmentMessageTVB;
import com.zhuzb.itsPet.ui.fragment.FragmentPet;
import com.zhuzb.itsPet.utils.XCRoundImageView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Method;
import java.util.HashMap;

import static com.mob.wrappers.UMSSDKWrapper.logout;

@ContentView(value = R.layout.left_main_activity)
public class LeftMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    //当前页面标识
    private static int oldFragment = 0;
    private static int oldTv = 0;
    //跳转fragment页面标识
    private static int isFragment = 0;
    private static int isTv1 = 0;
    private static int isTv2 = 0;
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    //底部导航栏
    @ViewInject(value = R.id.left_main_navigation)
    private BottomNavigationView navigation;
    @ViewInject(value = R.id.left_main_content)
    private FrameLayout frameLayout;
    //标题栏两个text按钮
    @ViewInject(value = R.id.left_main_its_tv1)
    private TextView textViewa;
    @ViewInject(value = R.id.left_main_its_tv2)
    private TextView textViewb;
    //标题栏和侧拉菜单
    @ViewInject(value = R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(value = R.id.drawer_layout)
    private DrawerLayout drawer;
    @ViewInject(value = R.id.nav_view)
    private NavigationView navigationView;

    //侧拉栏信息
    private View viewNavigation;
    private XCRoundImageView imageView;
    private TextView userName;
    private TextView userText;

    private FragmentManager fm;//管理fragment的类
    private FragmentItsTVA fragmentItsTVA;
    private FragmentItsTVB fragmentItsTVB;
    private FragmentPet fragmentPet;
    private FragmentMessageTVA fragmentMessageTVA;
    private FragmentMessageTVB fragmentMessageTVB;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            isExit = false;
        }
    };

    /**
     * 说明： 底部导航栏监听
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:44
     * 邮箱： zhuzhibo2017@163.com
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (isFragment == 0) {
                        if (fragmentItsTVA!=null){
                            fragmentItsTVA.updataViewPager();
                        }
                        if(fragmentItsTVB!=null){
                            fragmentItsTVB.updataMyFragment( 0 );
                        }
                    } else {
                        isFragment = 0;
                        eventItsTv( isTv1 );
                        showFragment( isFragment, isTv1 );
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (isFragment == 1) {
                        if( fragmentPet!=null){
                            fragmentPet.initData(  Login.getUserId( getApplication() ));
                        }
                    } else {
                        isFragment = 1;
                        showFragment( isFragment, -1 );
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (isFragment == 2) {
                        if (fragmentMessageTVA!=null){
                            fragmentMessageTVA.initData( Login.getUserId( getApplication() ) );
                        }
                        if(fragmentMessageTVB!=null){
                        }
                    } else {
                        isFragment = 2;
                        eventItsTv( isTv2 );
                        showFragment( isFragment, isTv2 );
                    }
                    return true;
            }
            return false;
        }

    };

    private void showFragment(int fraNum, int tabTv) {
        FragmentTransaction ft = fm.beginTransaction();
        // fragment切换动画选择
        if (oldFragment > fraNum) {
            ft.setCustomAnimations( R.animator.slide_from_left, R.animator.slide_to_right );
        } else if (oldFragment == fraNum) {
            if (fraNum != 1 && oldTv > tabTv) {
                ft.setCustomAnimations( R.animator.slide_from_left, R.animator.slide_to_right );
            } else if (fraNum != 1 && oldTv < tabTv) {
                ft.setCustomAnimations( R.animator.slide_from_right, R.animator.slide_to_left );
            }
        } else if (oldFragment < fraNum) {
            ft.setCustomAnimations( R.animator.slide_from_right, R.animator.slide_to_left );
        }
        oldFragment = fraNum;
        oldTv = tabTv;

        hideFragment( ft );
        switch (fraNum) {
            case 0:
                textViewa.setText( R.string.main_pets );
                textViewb.setText( R.string.main_mypet );
                textViewa.setVisibility( View.VISIBLE );
                textViewb.setVisibility( View.VISIBLE );
                if (tabTv == 0) {
                    if (fragmentItsTVA != null) {
                        ft.show( fragmentItsTVA );
                    } else {
                        fragmentItsTVA = new FragmentItsTVA();
                        ft.add( R.id.left_main_content, fragmentItsTVA );
                    }
                } else if (tabTv == 1) {
                    if (fragmentItsTVB != null) {
                        ft.show( fragmentItsTVB );
                    } else {
                        fragmentItsTVB = new FragmentItsTVB();
                        ft.add( R.id.left_main_content, fragmentItsTVB );
                    }
                }
                break;
            case 1:
                textViewa.setVisibility( View.GONE );
                textViewb.setVisibility( View.GONE );
                if (fragmentPet != null) {
                    ft.show( fragmentPet );
                } else {
                    fragmentPet = new FragmentPet();
                    ft.add( R.id.left_main_content, fragmentPet );
                }
                break;
            case 2:
                textViewa.setText( R.string.comment );
                textViewb.setText( R.string.message );
                textViewa.setVisibility( View.VISIBLE );
                textViewb.setVisibility( View.VISIBLE );
                if (tabTv == 0) {
                    if (fragmentMessageTVA != null) {
                        ft.show( fragmentMessageTVA );
                    } else {
                        fragmentMessageTVA = new FragmentMessageTVA();
                        ft.add( R.id.left_main_content, fragmentMessageTVA );
                    }
                } else if (tabTv == 1) {
                    if (fragmentMessageTVB != null) {
                        ft.show( fragmentMessageTVB );
                    } else {
                        fragmentMessageTVB = new FragmentMessageTVB();
                        ft.add( R.id.left_main_content, fragmentMessageTVB );
                    }
                }
                break;
            default:
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (fragmentItsTVA != null) {
            ft.hide( fragmentItsTVA );
        }
        if (fragmentItsTVB != null) {
            ft.hide( fragmentItsTVB );
        }
        if (fragmentPet != null) {
            ft.hide( fragmentPet );
        }
        if (fragmentMessageTVA != null) {
            ft.hide( fragmentMessageTVA );
        }
        if (fragmentMessageTVB != null) {
            ft.hide( fragmentMessageTVB );
        }
    }

    /**
     * 说明： 加载页面调用
     * 作者： 朱志波
     * 时间： 2018/4/20 0020 22:35
     * 邮箱： zhuzhibo2017@163.com
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Intent i = getIntent();
        UserDataBean user = (UserDataBean) i.getSerializableExtra( "userData" );//用户信息
        x.view().inject( this );
        //初始化页面
        setSupportActionBar( toolbar );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle( "" );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener( this );
        setGestureListener();
        fm = getSupportFragmentManager();
        eventItsTv( isTv1 );
        showFragment( isFragment, isTv1 );
        navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

        viewNavigation = navigationView.getHeaderView( 0 );
        imageView = viewNavigation.findViewById( R.id.user_img );
        userName = viewNavigation.findViewById( R.id.user_name );
        userText = viewNavigation.findViewById( R.id.user_text );
        Picasso.with( getBaseContext() ).load( user.getUserImg() ).into( imageView );
        userName.setText( user.getUserName() );
        userText.setText( user.getUserTx() );
    }

    /**
     * 说明：标题栏text按钮监听
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:40
     * 邮箱： zhuzhibo2017@163.com
     */
    @Event(value = {R.id.left_main_its_tv1, R.id.left_main_its_tv2})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.left_main_its_tv1:
                if (isFragment == 0) {
                    isTv1 = 0;
                    eventItsTv( isTv1 );
                    showFragment( isFragment, isTv1 );
                } else if (isFragment == 2) {
                    isTv2 = 0;
                    eventItsTv( isTv2 );
                    showFragment( isFragment, isTv2 );
                }
                break;
            case R.id.left_main_its_tv2:
                if (isFragment == 0) {
                    isTv1 = 1;
                    eventItsTv( isTv1 );
                    showFragment( isFragment, isTv1 );
                } else if (isFragment == 2) {
                    isTv2 = 1;
                    eventItsTv( isTv2 );
                    showFragment( isFragment, isTv2 );
                }
                break;
        }
    }

    private void eventItsTv(int i) {
        switch (i) {
            case 0:
                textViewa.setSelected( true );
                textViewb.setSelected( false );
                break;
            case 1:
                textViewb.setSelected( true );
                textViewa.setSelected( false );
                break;
        }
    }

    /**
     * 说明： 右按钮监听
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:45
     * 邮箱： zhuzhibo2017@163.com
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_you_caidan, menu );//弹出菜单界面显示
        return true;
    }

    /**
     * 说明： 弹出菜单监听
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:46
     * 邮箱： zhuzhibo2017@163.com
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_a) {
            return true;
        } else if (id == R.id.action_settings_b) {
            // 给bnt1添加点击响应事件
            Intent intent = new Intent( LeftMainActivity.this, AddPetNoteActivity.class );
            //启动
            startActivity( intent );
            return true;
        } else if (id == R.id.action_settings_c) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase( "MenuBuilder" )) {
                try {
                    Method method = menu.getClass().getDeclaredMethod( "setOptionalIconsVisible", Boolean.TYPE );
                    method.setAccessible( true );
                    method.invoke( menu, true );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened( featureId, menu );
    }

    /**
     * 说明： 侧拉菜单监听器
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:37
     * 邮箱： zhuzhibo2017@163.com
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            //打开我的资料页面。如果此前没有登录，则会先执行登录，在登录完成后才显示资料页面
            UMSGUI.showProfilePage();
//        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_fs_pets) {
            // 给bnt1添加点击响应事件
            Intent intent = new Intent( LeftMainActivity.this, FollowActivity.class );
            //启动
            startActivity( intent );
        } else if (id == R.id.nav_share) {
            // 给bnt1添加点击响应事件
            Intent intent = new Intent( LeftMainActivity.this, LoveActivity.class );
            //启动
            startActivity( intent );
        } else if (id == R.id.nav_send) {
            AlertDialog.Builder guanyu = new AlertDialog.Builder(LeftMainActivity.this);
            guanyu.setTitle("关于 its养宠-1.0版@zb");
            guanyu.setMessage( "\n您的爱宠可以成为小明星\n" +
                    "您可以认识更多有趣的宠物\n" +
                    "给您带来一种新式养宠体验\n"+
                    "感谢使用 祝您人丁兴旺\n\n联系QQ:1959873506");
            guanyu.setPositiveButton("确定", null);
            guanyu.show();
//            Toast.makeText( getApplicationContext(),"····ID····"+Login.getUserId( getApplicationContext() ), Toast.LENGTH_LONG ).show();
        } else if (id == R.id.nav_manage) {
            Login.loginUP( getApplicationContext() );
        }
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    /**
     * 设置左右滑动作监听器
     *
     * @author jczmdeveloper
     */
    private void setGestureListener() {
        frameLayout.setOnTouchListener( new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下时监听
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        x2 = event.getX();
//                        y2 = event.getY();
//                        break;
                    //抬起时监听
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        if (x2 - x1 > 0
                                && (Math.abs( x2 - x1 ) > 100)) {
                            //从左往右滑
//                            Toast.makeText(LeftMainActivity.this, "向右滑", Toast.LENGTH_SHORT).show()
                            if (oldFragment == 0 && oldTv == 0) {
                            } else if (oldFragment == 0 && oldTv == 1) {
                                isFragment = 0;
                                isTv1 = 0;
                                eventItsTv( isTv1 );
                                showFragment( isFragment, isTv1 );
                            } else if (oldFragment == 1) {
                                isFragment = 0;
                                isTv1 = 1;
                                eventItsTv( isTv1 );
                                showFragment( isFragment, isTv1 );
                            } else if (oldFragment == 2 && oldTv == 0) {
                                isFragment = 1;
                                showFragment( isFragment, -1 );
                            } else if (isFragment == 2 && isTv2 == 1) {
                                isFragment = 2;
                                isTv2 = 0;
                                eventItsTv( isTv2 );
                                showFragment( isFragment, isTv2 );
                            }
                        } else if (x2 - x1 < 0
                                && (Math.abs( x2 - x1 ) > 100)) {
                            //从右往左滑
//                            Toast.makeText(LeftMainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
                            if (oldFragment == 0 && oldTv == 0) {
                                isFragment = 0;
                                isTv1 = 1;
                                eventItsTv( isTv1 );
                                showFragment( isFragment, isTv1 );
                            } else if (oldFragment == 0 && oldTv == 1) {
                                isFragment = 1;
                                showFragment( isFragment, -1 );
                            } else if (oldFragment == 1) {
                                isFragment = 2;
                                isTv2 = 0;
                                eventItsTv( isTv2 );
                                showFragment( isFragment, isTv2 );
                            } else if (oldFragment == 2 && oldTv == 0) {
                                isFragment = 2;
                                isTv2 = 1;
                                eventItsTv( isTv2 );
                                showFragment( isFragment, isTv2 );
                            } else if (isFragment == 2 && isTv2 == 1) {

                            }
                        }
                        break;
                }
                navigation.getMenu().getItem( isFragment ).setChecked( true );
                return true;
            }

        } );
    }

    /**
     * 说明： TVA外部页面通知跳转
     * 作者： 朱志波
     * 时间： 2018/4/22 0022 18:08
     * 邮箱： zhuzhibo2017@163.com
     */
    public void isShowa(int fraNum, int tabTv) {
        isFragment = fraNum;
        isTv1 = tabTv;
        eventItsTv( isTv1 );
        showFragment( isFragment, isTv1 );
        navigation.getMenu().getItem( isFragment ).setChecked( true );
    }

    /**
     * 说明： TVB外部页面通知跳转
     * 作者： 朱志波
     * 时间： 2018/5/8 0008 22:13
     * 邮箱： zhuzhibo2017@163.com
     */
    public void isShowb(int fraNum) {
        isFragment = fraNum;
        showFragment( isFragment, 0 );
        navigation.getMenu().getItem( isFragment ).setChecked( true );
    }

    /**
     * 说明： 返回键监听
     * 作者： 朱志波
     * 时间： 2018/4/19 0019 22:40
     * 邮箱： zhuzhibo2017@163.com
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            exit();
        }
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText( getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT ).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed( 0, 2000 );
        } else {
            System.exit( 0 );
        }
    }

    /**
     * fragment回调接口
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String data);
    }
}
