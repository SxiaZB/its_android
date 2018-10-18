package com.zhuzb.itsPet.ui.acticity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.its.McbItem;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.adapter.its.MCBitemAdapter;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/5/30 0030
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.follow_activity)
public class FollowActivity extends Activity {
    @ViewInject(value = R.id.follow_item)
    private ListView listView;

    private List<McbItem> followItems;
    private MCBitemAdapter followItemAdapter;

    ProgressDialog pDialog;//进度窗口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        x.view().inject( this );
        initData();
        initData( Login.getUserId( getApplication() ) );
    }

    /**
     * 加载数据
     *
     * @param userId
     */
    public void initData(final String userId) {
        showDialg( "正在加载我的关注..." );
        new Thread( new Runnable() {
            @Override
            public void run() {
                LogUtil.e( "我的关注数据加载中" );
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                OkhttpUtils.sendOkHttpRequest( CONSTS.FOLLOW_USER_URL,
                        "{\"userId\":\"" + userId + "\"}",
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //异常处理
                                pDialog.dismiss();
                                errorToast( "服务器异常！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<McbItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        followItems = data.getData();
                                        FollowActivity.this.runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                followItemAdapter = new MCBitemAdapter( getApplication(), followItems, mListener );
                                                listView.setAdapter( followItemAdapter );
                                            }
                                        } );
                                        pDialog.dismiss();
                                    } else {
                                        pDialog.dismiss();
                                        errorToast( "服务器异常！" );
                                    }
                                } else {
                                    pDialog.dismiss();
                                    errorToast( "服务器异常！" );
                                }
                            }
                        } );
            }
        } ).start();
    }
    @Event( value =R.id.follow_bt )
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.follow_bt:
                FollowActivity.this.finish();
                break;
        }
    }

    /**
     * 加载监听
     */
    private void initData() {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplication(), "第" + i + "行", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    /**
     * 实现类，响应按钮点击事件
     */
    private MCBitemAdapter.MyClickListener mListener = new MCBitemAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()) {
                case R.id.its_mcb_fs_tv:
                    if ("＋ 粉丝团".equals( ((TextView) v).getText() )) {
                        ((TextView) v).setText( followItems.get( position ).getFollow_name() );
                        String json0 = "{\"userId\":\"" + Login.getUserId( getApplication() )
                                + "\",\"petId\":" + followItems.get( position ).getPet_id() + "}";
                        sendCZ( json0, CONSTS.ADD_FOLLOW_URL, "关注" );
                    } else {
                        ((TextView) v).setText( "＋ 粉丝团" );
                        String json1 = "{\"userId\":\"" + Login.getUserId( getApplication() )
                                + "\",\"petId\":" + followItems.get( position ).getPet_id() + "}";
                        sendCZ( json1, CONSTS.DEL_FOLLOW_URL, "取消关注" );
                    }
                    break;
            }
        }
    };

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
     * 错误提示
     *
     * @param error
     */
    private void errorToast(final String error) {
        FollowActivity.this.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( getApplication(), error, Toast.LENGTH_SHORT ).show();
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
