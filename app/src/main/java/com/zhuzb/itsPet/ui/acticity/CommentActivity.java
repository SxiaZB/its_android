package com.zhuzb.itsPet.ui.acticity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.its.NoteDataItem;
import com.zhuzb.itsPet.model.message.CommentItem;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.adapter.message.MessageTVAadapter;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.ScrollListView;
import com.zhuzb.itsPet.utils.XCRoundImageView;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/5/29 0029
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.note_activity)
public class CommentActivity extends Activity {

    @ViewInject(value = R.id.note_userimg)
    XCRoundImageView petUserImgView;
    @ViewInject(value = R.id.note_usertv)
    TextView petUserNameView;
    @ViewInject(value = R.id.note_dj_tv)
    TextView PetUserTitleView;
    @ViewInject(value = R.id.list_note_img)
    ImageView dyImgView;
    @ViewInject(value = R.id.note_tv_liuyan)
    TextView petLeaveView;
    @ViewInject(value = R.id.note_fs_tv)
    TextView followView;
    @ViewInject(value = R.id.note_tv_shijian)
    TextView timeView;
    @ViewInject(value = R.id.note_fengxiang0)
    LinearLayout fxLLView;
    @ViewInject(value = R.id.note_shouchang0)
    LinearLayout scLLView;
    @ViewInject(value = R.id.note_dianzan0)
    LinearLayout dzLLView;
    @ViewInject(value = R.id.note_pinglun0)
    LinearLayout plLLView;
    @ViewInject(value = R.id.note_pinlun_et)
    EditText editTextPL;
    @ViewInject( value = R.id.note_pinlun_list)
    ScrollListView listView;

    ProgressDialog pDialog;//进度窗口

    private NoteDataItem noteData;

    private List<CommentItem> commentItemList;
    private MessageTVAadapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Intent i = getIntent();
        noteData = (NoteDataItem) i.getSerializableExtra( "noteData" );//用户信息
        x.view().inject( this );
        initData( noteData );
    }

    /**
     * 加载数据
     *
     * @param note
     */
    private void initData(NoteDataItem note) {
        Picasso.with( getBaseContext() ).load( note.getPet_user_img_url() ).into( petUserImgView );//头像
        petUserNameView.setText( note.getPet_name() );
        PetUserTitleView.setText( note.getPet_grade() );
        Picasso.with( getBaseContext() ).load( note.getNote_img_url() ).into( dyImgView );//主图
        petLeaveView.setText( note.getNote_text() );
        if (note.getFollow_status() == 1.0) {
            followView.setText( note.getFollow_name() );
        } else {
            followView.setText( "＋ 粉丝团" );
        }
        timeView.setText( note.getLastmodify_time() );
        String json5 = "{\"petNoteId\":"+(int) noteData.getPet_note_id() +"}";
        getPLlist( json5,CONSTS.COMMENT_NOTE_URL,"加载历史评论" );
    }

    @Event(value = {R.id.note_bt, R.id.note_fs_tv, R.id.note_fengxiang0, R.id.note_shouchang0, R.id.note_dianzan0, R.id.note_pinglun0})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.note_fs_tv:
                if ("＋ 粉丝团".equals( ((TextView) view).getText() )) {
                    ((TextView) view).setText( noteData.getFollow_name() );
                    String json0 = "{\"userId\":\"" + Login.getUserId( getApplication() )
                            + "\",\"petId\":" + (int) noteData.getPet_id() + "}";
                    sendCZ( json0, CONSTS.ADD_FOLLOW_URL, "关注" );
                } else {
                    ((TextView) view).setText( "＋ 粉丝团" );
                    String json1 = "{\"userId\":\"" + Login.getUserId( getApplication() )
                            + "\",\"petId\":" + (int) noteData.getPet_id() + "}";
                    sendCZ( json1, CONSTS.DEL_FOLLOW_URL, "取消关注" );
                }
                break;
            case R.id.note_fengxiang0:
                showShare( "来一波分享", noteData.getNote_img_url() );
                break;
            case R.id.note_shouchang0:
                String json2 = "{\"userId\":\"" + Login.getUserId( getApplication() )
                        + "\",\"petNoteId\":" + (int) noteData.getPet_note_id() + "}";
                sendCZ( json2, CONSTS.ADD_LOVENOTE_URL, "收藏" );
                break;
            case R.id.note_dianzan0:
                String json3 = "{\"userId\":\"" + Login.getUserId( getApplication() )
                        + "\",\"petNoteId\":" + (int) noteData.getPet_note_id() + "}";
                sendCZ( json3, CONSTS.ADD_UP_URL, "点赞" );
                break;
            case R.id.note_pinglun0:
                String plText = editTextPL.getText().toString().trim();
                if (plText.length() > 0) {
                    String json4 = "{\"userId\":\"" + Login.getUserId( getApplication() ) +
                            "\",\"userName\":\"" + Login.getUserName( getApplication() ) +
                            "\",\"petNoteId\":" + (int) noteData.getPet_note_id() + ",\"comment\":\"评论："+plText+"\"}";
                    sendPl( json4, CONSTS.ADD_COMMENT_URL, "评论" );
                }else {
                    errorToast( "请输入评论内容！" );
                }
                String json5 = "{\"petNoteId\":"+(int) noteData.getPet_note_id() +"}";
                getPLlist( json5,CONSTS.COMMENT_NOTE_URL,"刷新历史评论" );
                break;
            case R.id.note_userimg:
                Toast.makeText( getApplication(), "头像", Toast.LENGTH_SHORT ).show();
                break;
            case R.id.note_bt:
                CommentActivity.this.finish();
                break;
        }
    }

    /**
     * 评论列表
     */
    private void getPLlist(final String json, final String url, final String toast) {
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
                                        commentItemList = data.getData();
                                        CommentActivity.this.runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                commentAdapter = new MessageTVAadapter( getApplication(), commentItemList );
                                                listView.setAdapter( commentAdapter );
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

    /**
     * 评论
     */
    private void sendPl(final String json, final String url, final String toast) {
        showDialg( "正在添加评论..." );
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequest( url, json,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                pDialog.dismiss();
                                errorToast( toast + "失败！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        pDialog.dismiss();
                                        editTextPL.setText( "" );
                                        errorToast( toast + "成功！" );
                                    } else {
                                        pDialog.dismiss();
                                        errorToast( data.getErrorMessage() );
                                    }
                                } else {
                                    pDialog.dismiss();
                                    errorToast( toast + "失败！" );
                                }
                            }
                        } );
            }
        } ).start();
    }

    /**
     * 用户操作
     */
    private void sendCZ(final String json, final String url, final String toast) {
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
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
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

    /**
     * 分享操作
     *
     * @param text
     * @param url
     */
    private void showShare(String text, String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle( getString( R.string.share ) );
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl( "http://sharesdk.cn" );
        // text是分享文本，所有平台都需要这个字段
        oks.setText( text );
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl( url );//网络图片rul
//        oks.setImagePath("/sdcard/itspic/img0.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show( getApplication() );
    }

    /**
     * 错误提示
     *
     * @param error
     */
    private void errorToast(final String error) {
        CommentActivity.this.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( CommentActivity.this, error, Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    private void showDialg(String message) {
        if (null == pDialog) {
            pDialog = new ProgressDialog( this );
        }
        pDialog.setMessage( message );
        pDialog.show();
    }
}
