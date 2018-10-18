package com.zhuzb.itsPet.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.its.NoteDataItem;
import com.zhuzb.itsPet.model.its.NoteListItem;
import com.zhuzb.itsPet.model.message.CommentItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.acticity.CommentActivity;
import com.zhuzb.itsPet.ui.acticity.LeftMainActivity;
import com.zhuzb.itsPet.ui.adapter.message.MessageTVAadapter;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 消息页面
 *
 * @author zhuzb
 * @date on 2018/4/10 0010
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.main_message_tva)
public class FragmentMessageTVA extends Fragment {
    @ViewInject(value = R.id.message_tva_item)
    private ListView listView;

    private List<CommentItem> messageTVAitemList;
    private MessageTVAadapter messageTVAadapter;

    //回调父级页面
    private LeftMainActivity leftMainActivity;

    ProgressDialog pDialog;//进度窗口

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.main_message_tva, null );
        x.view().inject( this, view );
        leftMainActivity = (LeftMainActivity) getActivity();
        initData();
        initData( Login.getUserId( getActivity() ) );
        return view;
    }

    public void initData(String UserId) {
        String json = "{\"userId\":\"" + UserId + "\"}";
        getPLlist( json, CONSTS.COMMENT_USER_URL, "加载评论" );
    }

    /**
     * 加载评论列表
     *
     * @param json
     * @param url
     * @param toast
     */
    public void getPLlist(final String json, final String url, final String toast) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequest( url, json,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                errorToast( toast + "失败！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<CommentItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        messageTVAitemList = data.getData();
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                messageTVAadapter = new MessageTVAadapter( getActivity(), messageTVAitemList );
                                                listView.setAdapter( messageTVAadapter );
                                            }
                                        } );
                                        errorToast( toast + "成功！" );
                                    } else {
                                        errorToast( data.getErrorMessage() );
                                    }
                                } else {
                                    errorToast( toast + "失败！" );
                                }
                            }
                        } );
            }
        } ).start();
    }

    private void initData() {
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialg( "系统转入该评论页..." );
                getNoteData( Integer.parseInt( messageTVAitemList.get( i ).getPet_note_id() ) );
            }
        } );
        listView.setOnTouchListener( new View.OnTouchListener() {
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
                        if (x2 - x1 < 0
                                && (Math.abs( x2 - x1 ) > 200)) {
                            leftMainActivity.isShowa( 2, 1 );
                        } else if (x2 - x1 > 0
                                && (Math.abs( x2 - x1 ) > 200)) {
                            leftMainActivity.isShowb( 1 );
                        }
                        break;
                }
                return false;
            }
        } );
    }

    /**
     * 错误提示
     *
     * @param error
     */
    private void errorToast(final String error) {
        getActivity().runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( getActivity(), error, Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    /**
     * 加载评论记录数据
     *
     * @param petNoteId
     */
    private void getNoteData(final int petNoteId) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                String json = "{\"petNoteId\":"+petNoteId+"}";
                OkhttpUtils.sendOkHttpRequest( CONSTS.COMMENT_USER_NOTE_URL,
                        json,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                pDialog.dismiss();
                                errorToast( "转入失败"+e );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    final JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<NoteListItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                List<NoteListItem> noteListItem = data.getData();
                                                startComment( noteListItem.get( 0 ) );
                                            }
                                        } );
                                        pDialog.dismiss();
                                    } else {
                                        pDialog.dismiss();
                                        errorToast( data.getErrorMessage() );
                                    }
                                } else {
                                    pDialog.dismiss();
                                    errorToast( "转入失败" );
                                }
                            }
                        } );
            }
        } ).start();
    }

    /**
     * 跳转评论区
     *
     * @param note
     */
    private void startComment(NoteListItem note) {
        NoteDataItem noteData = new NoteDataItem( note );
        Intent intent = new Intent( getActivity(), CommentActivity.class );
        intent.putExtra( "noteData", noteData );
        startActivity( intent );
    }

    private void showDialg(String message) {
        if (null == pDialog) {
            pDialog = new ProgressDialog( getActivity() );
        }
        pDialog.setMessage( message );
        pDialog.show();
    }

}
