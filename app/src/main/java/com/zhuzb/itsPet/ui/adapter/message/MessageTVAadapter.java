package com.zhuzb.itsPet.ui.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.model.message.CommentItem;
import com.zhuzb.itsPet.model.message.MessageTVAitem;
import com.zhuzb.itsPet.utils.XCRoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/5/13 0013
 * @email zhuzhibo2017@163.com
 */

public class MessageTVAadapter extends BaseAdapter {
    private Context mContext;
    private List<CommentItem> mList;

    public MessageTVAadapter(Context context, List<CommentItem> list) {
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
            convertView = LayoutInflater.from( mContext ).inflate( R.layout.son_message_tva, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.messageUserNameView.setText( mList.get( position ).getUser_name() );
        holder.messageTVAtime.setText( mList.get( position ).getCreater_time() );
        holder.messageTVApl.setText( mList.get( position ).getComment_text() );
        return convertView;
    }

    static class ViewHolder {

        @ViewInject(value = R.id.message_tva_username)
        TextView messageUserNameView;
        @ViewInject(value = R.id.message_tva_time)
        TextView messageTVAtime;
        @ViewInject(value = R.id.message_tva_pl)
        TextView messageTVApl;

        ViewHolder(View view) {
            x.view().inject( this, view );
        }
    }
}