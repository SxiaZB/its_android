package com.zhuzb.itsPet.ui.adapter.its;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.model.its.NoteListItem;
import com.zhuzb.itsPet.utils.XCRoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/5/8 0008
 * @email zhuzhibo2017@163.com
 */

public class ItsTVBadapter extends BaseAdapter {
    private Context mContext;
    private List<NoteListItem> mList;
    private MyClickListener mListener;

    public ItsTVBadapter(Context context,List<NoteListItem> list, ItsTVBadapter.MyClickListener listener)
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
            convertView = LayoutInflater.from(mContext).inflate( R.layout.son_its_tvb, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(mList.get( position ).getPet_user_img_url()).into(holder.petUserImgView);//头像
        holder.petUserNameView.setText( mList.get( position ).getPet_name() );
        holder.PetUserTitleView.setText( mList.get( position ).getPet_grade() );
        Picasso.with(mContext).load(mList.get( position ).getNote_img_url()).into(holder.dyImgView);//主图
        holder.petLeaveView.setText( mList.get( position ).getNote_text() );
        holder.timeView.setText( mList.get( position ).getLastmodify_time() );

        holder.petUserImgView.setOnClickListener( mListener );
        holder.petUserImgView.setTag(position);
        holder.seeView.setOnClickListener( mListener );
        holder.seeView.setTag(position);
        holder.delLLView.setOnClickListener( mListener );
        holder.delLLView.setTag(position);

        return convertView;
    }
    static class ViewHolder
    {
        @ViewInject( value = R.id.its_tvb_userimg)
        XCRoundImageView petUserImgView;
        @ViewInject( value = R.id.its_tvb_usertv)
        TextView petUserNameView;
        @ViewInject( value = R.id.its_tvb_dj_tv)
        TextView PetUserTitleView;
        @ViewInject( value = R.id.list_its_tvb_img)
        ImageView dyImgView;
        @ViewInject( value = R.id.its_tvb_tv_liuyan)
        TextView petLeaveView;
        @ViewInject(value =  R.id.its_tvb_tv_shijian )
        TextView timeView;
        @ViewInject( value = R.id.its_tvb_fb_tv)
        TextView seeView;
        @ViewInject( value = R.id.its_tvb_bianji0)
        LinearLayout delLLView;

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
