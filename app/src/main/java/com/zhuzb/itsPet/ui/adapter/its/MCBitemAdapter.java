package com.zhuzb.itsPet.ui.adapter.its;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.model.its.McbItem;
import com.zhuzb.itsPet.utils.XCRoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/4/26 0026
 * @email zhuzhibo2017@163.com
 */

public class MCBitemAdapter extends BaseAdapter
{
    private Context mContext;
    private List<McbItem> mList;
    private MyClickListener mListener;

    public MCBitemAdapter(Context context, List<McbItem> list, MyClickListener listener)
    {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @Override
    public int getCount()
    {
        return mList == null?0:mList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate( R.layout.son_its_mcb, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(mList.get( position ).getPet_user_img_url()).into(holder.petUserImgView);//头像
        holder.petUserNameView.setText( mList.get( position ).getPet_name() );
        holder.PetUserTitleView.setText( mList.get( position ).getPet_grade() );
        holder.upView.setText( mList.get( position ).getUp_sum() );
        holder.foView.setText( mList.get( position ).getFollow_sum() );

        if("1".equals(mList.get( position ).getFollow_status())){
            holder.followView.setText( mList.get( position ).getFollow_name() );
        }else {
            holder.followView.setText( "＋ 粉丝团" );
        }

        holder.followView.setOnClickListener( mListener );
        holder.followView.setTag(position);

        return convertView;
    }
    static class ViewHolder
    {
        @ViewInject( value = R.id.its_mcb_userimg)
        XCRoundImageView petUserImgView;
        @ViewInject( value = R.id.its_mcb_usertv)
        TextView petUserNameView;
        @ViewInject( value = R.id.its_mcb_dj_tv)
        TextView PetUserTitleView;
        @ViewInject( value = R.id.its_mcb_fs_tv)
        TextView followView;

        @ViewInject( value = R.id.its_tva_mcb_hz)
        TextView upView;
        @ViewInject( value = R.id.its_tva_mcb_fs)
        TextView foView;
        ViewHolder(View view)
        {
            x.view().inject( this, view );
        }
    }

    /**
     * 用于回调的抽象类
     * @author Ivan Xu
     * 2014-11-26
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

}
