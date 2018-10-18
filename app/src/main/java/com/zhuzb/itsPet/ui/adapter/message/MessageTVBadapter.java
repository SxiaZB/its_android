package com.zhuzb.itsPet.ui.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.model.message.MessageTVBitem;
import com.zhuzb.itsPet.utils.XCRoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/5/13 0013
 * @email zhuzhibo2017@163.com
 */

public class MessageTVBadapter  extends BaseAdapter {
    private Context mContext;
    private List<MessageTVBitem> mList;

    public MessageTVBadapter(Context context, List<MessageTVBitem> list) {
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
            convertView = LayoutInflater.from( mContext ).inflate( R.layout.son_message_tvb, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
////        holder.petUserImgView.setImageURI(Uri.parse(mList.get( position ).getPetUserImg()));//头像
//        holder.petUserNameView.setText( mList.get( position ).getPetUserName() );
//        holder.PetUserTitleView.setText( mList.get( position ).getPetUserTitle() );
////        holder.dyImgView.setImageURI(Uri.parse(mList.get( position ).getDyImg()));
//        holder.petLeaveView.setText( mList.get( position ).getPetLeave() );
//        LogUtil.e(  mList.get( position ).getPetLeave() );
        return convertView;
    }

    static class ViewHolder {

        @ViewInject(value = R.id.message_tvb_imageView)
        XCRoundImageView messageUserImgView;
        @ViewInject(value = R.id.message_tvb_username)
        TextView messageUserNameView;
        @ViewInject(value = R.id.message_tvb_time)
        TextView messageTVAtime;
        @ViewInject(value = R.id.message_tvb_sx)
        TextView messageTVApl;

        ViewHolder(View view) {
            x.view().inject( this, view );
        }
    }
}