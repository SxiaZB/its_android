package com.zhuzb.itsPet.ui.child.its;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.its.ItsDTitem;
import com.zhuzb.itsPet.model.its.NoteDataItem;
import com.zhuzb.itsPet.model.its.NoteListItem;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.acticity.CommentActivity;
import com.zhuzb.itsPet.ui.adapter.its.GZitemAdapter;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/4/23 0023
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.its_view2)
public class ItsGZfragment extends Fragment {
    @ViewInject( value = R.id.its_gz_item)
    private ListView listView;

    private List<NoteListItem> itsDTitemList;
    private GZitemAdapter gZitemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.its_view2, null );
        x.view().inject( this, view );
        initData();
        initData( Login.getUserId( getActivity() ));
        return view;
    }

    /**
     * 加载监听器
     */
    private void initData(){
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startComment( i );
            }
        } );
    }

    /**
     * 加载数据
     * @param userId
     */
    public void initData(final String userId) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                LogUtil.e( "关注数据加载中" );
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                OkhttpUtils.sendOkHttpRequest( CONSTS.GZ_DATA_URL,
                        "{\"pageSize\":10,\"model\":{\"userId\":\"" + userId + "\"}}",
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //异常处理
                                errorToast( "客户端异常！" );
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<NoteListItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        itsDTitemList = data.getData();
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                gZitemAdapter = new GZitemAdapter( getActivity(), itsDTitemList, mListener );
                                                listView.setAdapter( gZitemAdapter );
                                            }
                                        } );
                                    } else {
                                        errorToast( "服务器错误！" );
                                    }
                                } else {
                                    errorToast( "服务器错误！" );
                                }
                            }
                        } );
            }
        } ).start();
    }


    /**
     * 用户操作
     */
    private void sendCZ(final String json, final String url,final String toast) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequest( url,json,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                errorToast( toast+"失败！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        errorToast(toast+ "成功！" );
                                    } else {
                                        errorToast( data.getErrorMessage() );
                                    }
                                } else {
                                    errorToast( toast+"失败！" );
                                }
                            }
                        } );
            }
        } ).start();
    }

    /**
     * 错误提示
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
     * 实现类，响应按钮点击事件
     */
    private GZitemAdapter.MyClickListener mListener = new GZitemAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()) {
                case R.id.its_gz_fs_tv:
                    if("＋ 粉丝团".equals( ((TextView)v).getText() )){
                        ((TextView)v).setText( itsDTitemList.get( position ).getFollow_name() );
                        String json0 = "{\"userId\":\""+Login.getUserId( getActivity() )
                                +"\",\"petId\":"+(int)itsDTitemList.get( position ).getPet_id()+"}";
                        sendCZ(json0,CONSTS.ADD_FOLLOW_URL,"关注");
                    }else {
                        ((TextView)v).setText("＋ 粉丝团");
                        String json1 = "{\"userId\":\""+Login.getUserId( getActivity() )
                                +"\",\"petId\":"+(int)itsDTitemList.get( position ).getPet_id()+"}";
                        sendCZ(json1,CONSTS.DEL_FOLLOW_URL,"取消关注");
                    }
                    break;
                case R.id.its_gz_fengxiang0:
                    showShare( "来一波分享",itsDTitemList.get( position ).getNote_img_url() );
                    break;
                case R.id.its_gz_shouchang0:
                    String json2 = "{\"userId\":\""+Login.getUserId( getActivity() )
                            +"\",\"petNoteId\":"+(int)itsDTitemList.get( position ).getPet_note_id()+"}";
                    sendCZ(json2,CONSTS.ADD_LOVENOTE_URL,"收藏");
                    break;
                case R.id.its_gz_dianzan0:
                    String json3 = "{\"userId\":\""+Login.getUserId( getActivity() )
                            +"\",\"petNoteId\":"+(int)itsDTitemList.get( position ).getPet_note_id()+"}";
                    sendCZ(json3,CONSTS.ADD_UP_URL,"点赞");
                    break;
                case R.id.its_gz_pinglun0:
                    startComment( position );
                    break;
                case R.id.its_gz_userimg:
                    Toast.makeText( getActivity(), position + "头像", Toast.LENGTH_SHORT ).show();
                    break;
            }
        }
    };

    /**
     * 跳转评论区
     * @param position
     */
    private void startComment(int position) {
        NoteDataItem noteData = new NoteDataItem( itsDTitemList.get( position ));
        Intent intent = new Intent( getActivity(), CommentActivity.class );
        intent.putExtra("noteData", noteData);
        startActivity( intent );
    }


    /**
     * 分享操作
     * @param text
     * @param url
     */
    private void showShare(String text,String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(url);//网络图片rul
//        oks.setImagePath("/sdcard/itspic/img0.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(getActivity());
    }
}
