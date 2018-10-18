package com.zhuzb.itsPet.ui.adapter.its;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/4/23 0023
 * @email zhuzhibo2017@163.com
 */

public class ItsTVAadapter extends FragmentPagerAdapter
{
    private List<Fragment> mList;

    public ItsTVAadapter(FragmentManager fm,List<Fragment> list) {
        super( fm );
        mList=list;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public int getCount()
    {
        return mList == null?0:mList.size();
    }
}
