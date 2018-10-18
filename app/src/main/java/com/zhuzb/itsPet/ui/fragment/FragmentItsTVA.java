package com.zhuzb.itsPet.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.acticity.LeftMainActivity;
import com.zhuzb.itsPet.ui.adapter.its.ItsTVAadapter;
import com.zhuzb.itsPet.ui.child.its.ItsGZfragment;
import com.zhuzb.itsPet.ui.child.its.ItsMCBfragment;
import com.zhuzb.itsPet.ui.child.its.ItsRMfragment;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * its页面
 *
 * @author zhuzb
 * @date on 2018/4/10 0010
 * @email zhuzhibo2017@163.com
 */

@ContentView(value = R.layout.main_its_tva)
public class FragmentItsTVA extends Fragment {
    //加载控件
    @ViewInject(value = R.id.its_tva_content)
    private ViewPager itsViewPager;
    @ViewInject(value = R.id.its_reMen_0)
    private TextView itsTVaTv10;
    @ViewInject(value = R.id.its_reMen_1)
    private TextView itsTVaTv11;
    @ViewInject(value = R.id.its_guanZhu_0)
    private TextView itsTVaTv20;
    @ViewInject(value = R.id.its_guanZhu_1)
    private TextView itsTVaTv21;
    @ViewInject(value = R.id.its_paiHangBang_0)
    private TextView itsTVaTv30;
    @ViewInject(value = R.id.its_paiHangBang_1)
    private TextView itsTVaTv31;
    //回调父级页面
    private LeftMainActivity leftMainActivity;
    //viewPager设置
    private int isVP;
    private ItsTVAadapter itsTVAadapter;
    private List<Fragment> mList;
    private ItsRMfragment itsRMfragment = new ItsRMfragment();
    private ItsGZfragment itsGZfragment = new ItsGZfragment();
    private ItsMCBfragment itsMCBfragment = new ItsMCBfragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.main_its_tva, null );
        x.view().inject( this, view );
        leftMainActivity = (LeftMainActivity) getActivity();
        initData();
        return view;
    }

    /**
     * 说明： 加载数据
     * 作者： 朱志波
     * 时间： 2018/4/22 0022 21:37
     * 邮箱： zhuzhibo2017@163.com
     */
    private void initData() {
        setTVcolor( 0 );
        mList = new ArrayList<>();
        mList.add( itsRMfragment );
        mList.add( itsGZfragment );
        mList.add( itsMCBfragment );
        itsTVAadapter = new ItsTVAadapter( getChildFragmentManager(), mList );
        itsViewPager.setAdapter( itsTVAadapter );
        itsViewPager.setOffscreenPageLimit( -1 );
        initVp();
    }

    /**
     * 说明： 滑到最后一个跳转到下一fragment
     * 作者： 朱志波
     * 时间： 2018/4/22 0022 21:36
     * 邮箱： zhuzhibo2017@163.com
     */
    private void initVp() {
        itsViewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener()

        {
            /**
             * 滑动监听器OnPageChangeListener
             *  OnPageChangeListener这个接口需要实现三个方法：onPageScrollStateChanged，onPageScrolled ，onPageSelected
             *      1、onPageScrollStateChanged(int arg0) 此方法是在状态改变的时候调用。
             *          其中arg0这个参数有三种状态（0，1，2）
             *              arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，arg0==0的时表示什么都没做
             *              当页面开始滑动的时候，三种状态的变化顺序为1-->2-->0
             *      2、onPageScrolled(int arg0,float arg1,int arg2) 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。
             *          其中三个参数的含义分别为：
             *              arg0 :当前页面，及你点击滑动的页面
             *              arg1:当前页面偏移的百分比
             *              arg2:当前页面偏移的像素位置
             *      3、onPageSelected(int arg0) 此方法是页面跳转完后被调用，arg0是你当前选中的页面的Position（位置编号）
             */
            // 页面选中调用该方法
            @Override
            public void onPageSelected(int arg0) {
//                isVP = arg0;
                setTVcolor( arg0 );
            }

            // 页面滚动调用该方法
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                isVP = arg0;
            }

            // 页面滚动过程中的状态
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        } );
        itsViewPager.setOnTouchListener( new View.OnTouchListener() {
            float x1 = 0;
            float x2 = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下时监听
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    //抬起时监听
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        if (isVP == 2 && x2 - x1 < 0
                                && (Math.abs( x2 - x1 ) > 200)) {
                            leftMainActivity.isShowa( 0, 1 );
                        }
                        break;
                }
                return false;
            }
        } );
    }

    /**
     * 说明： 设置二级菜单选中状态
     * 作者： 朱志波
     * 时间： 2018/4/22 0022 17:06
     * 邮箱： zhuzhibo2017@163.com
     */

    private void setTVcolor(int arg) {
        itsTVaTv10.setSelected( false );
        itsTVaTv11.setSelected( false );
        itsTVaTv20.setSelected( false );
        itsTVaTv21.setSelected( false );
        itsTVaTv30.setSelected( false );
        itsTVaTv31.setSelected( false );
        switch (arg) {
            case 0:
                itsTVaTv10.setSelected( true );
                itsTVaTv11.setSelected( true );
                break;
            case 1:
                itsTVaTv20.setSelected( true );
                itsTVaTv21.setSelected( true );
                break;
            case 2:
                itsTVaTv30.setSelected( true );
                itsTVaTv31.setSelected( true );
                break;
            default:
                break;
        }
    }

    /**
     * 说明： 三级菜单监听
     * 作者： 朱志波
     * 时间： 2018/4/22 0022 22:20
     * 邮箱： zhuzhibo2017@163.com
     */
    @Event(value = {R.id.its_reMen_0, R.id.its_guanZhu_0, R.id.its_paiHangBang_0})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.its_reMen_0:
                itsViewPager.setCurrentItem( 0 );
                setTVcolor( 0 );
                break;
            case R.id.its_guanZhu_0:
                itsViewPager.setCurrentItem( 1 );
                setTVcolor( 1 );
                break;
            case R.id.its_paiHangBang_0:
                itsViewPager.setCurrentItem( 2 );
                setTVcolor( 2 );
                break;
        }
    }

    /**
     * 刷新
     */
    public void updataViewPager() {
        LogUtil.e( "····刷新 发现页面····" );
        itsRMfragment.initData( Login.getUserId( getActivity() ) );
        itsGZfragment.initData( Login.getUserId( getActivity() ) );
        itsMCBfragment.initData( Login.getUserId( getActivity() ) );
    }
}
