package com.zhuzb.itsPet.ui.adapter.pet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.model.pet.PetUserItem;
import com.zhuzb.itsPet.ui.adapter.its.GZitemAdapter;
import com.zhuzb.itsPet.utils.XCRoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/5/13 0013
 * @email zhuzhibo2017@163.com
 */

public class PetAdapter extends BaseAdapter {
    private Context mContext;
    private List<PetItem> mList;
    public PetAdapter(Context context, List<PetItem> list)
    {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from( mContext ).inflate( R.layout.son_pet_cw, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(mList.get( position ).getPet_user_img_url()).into(holder.petUserImgView);//头像
        holder.petUserNameView.setText( mList.get( position ).getPet_name() );
        holder.petUserSexView.setText( mList.get( position ).getPet_sex() );
        holder.petUserBreedView.setText( mList.get( position ).getPet_breed() );
        holder.petUserSRyear.setText( mList.get( position ).getPet_year() );
        holder.petUserSRmonth.setText( mList.get( position ).getPet_month() );
        holder.petUserSRday.setText( mList.get( position ).getPet_day() );
        holder.petUserBZ.setText( mList.get( position ).getPet_bz() );
        holder.petUserCode.setText( mList.get( position ).getPet_code() );
        return convertView;
    }

    static class ViewHolder {

        @ViewInject(value = R.id.pet_user_name)
        TextView petUserNameView;
        @ViewInject(value = R.id.pet_user_sex)
        TextView petUserSexView;
        @ViewInject(value = R.id.pet_user_breed)
        TextView petUserBreedView;
        @ViewInject(value = R.id.pet_user_sr_year)
        TextView petUserSRyear;
        @ViewInject(value = R.id.pet_user_sr_month)
        TextView petUserSRmonth;
        @ViewInject(value = R.id.pet_user_sr_day)
        TextView petUserSRday;
        @ViewInject(value = R.id.pet_user_bz)
        TextView petUserBZ;
        @ViewInject(value = R.id.pet_userimg)
        XCRoundImageView petUserImgView;
        @ViewInject(value = R.id.pet_user_code)
        TextView petUserCode;

        ViewHolder(View view) {
            x.view().inject( this, view );
        }
    }
}