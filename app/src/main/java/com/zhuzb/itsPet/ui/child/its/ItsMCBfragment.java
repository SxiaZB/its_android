package com.zhuzb.itsPet.ui.child.its;

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
import com.zhuzb.itsPet.model.its.McbItem;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.adapter.its.MCBitemAdapter;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/4/23 0023
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.its_view3)
public class ItsMCBfragment extends Fragment {
    @ViewInject( value = R.id.its_mcb_item)
    private ListView listView;

    private List<McbItem> itsMCBitems;
    private MCBitemAdapter mcBitemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.its_view3, null );
        x.view().inject( this, view );
        initData();
        initData( Login.getUserId( getActivity() ) );
        return view;
    }

    /**
     * 加载数据
     * @param userId
     */
    public void initData(final String userId) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                LogUtil.e( "名宠榜数据加载中" );
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                OkhttpUtils.sendOkHttpRequest( CONSTS.MCB_DATA_URL,
                        "{\"userId\":\"" + userId + "\"}",
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //异常处理
                                errorToast( "服务器异常！" );
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<McbItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        itsMCBitems = data.getData();
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                mcBitemAdapter = new MCBitemAdapter( getActivity(), itsMCBitems, mListener );
                                                listView.setAdapter( mcBitemAdapter );
                                            }
                                        } );
                                    } else {
                                        errorToast( "服务器异常！" );
                                    }
                                } else {
                                    errorToast( "服务器异常！" );
                                }
                            }
                        } );
            }
        } ).start();
    }

    /**
     * 加载监听
     */
    private void initData() {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(getActivity(), "第" + i + "行", Toast.LENGTH_LONG).show();
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
                    if("＋ 粉丝团".equals( ((TextView)v).getText() )){
                        ((TextView)v).setText( itsMCBitems.get( position ).getFollow_name() );
                        String json0 = "{\"userId\":\""+ Login.getUserId( getActivity() )
                                +"\",\"petId\":"+itsMCBitems.get( position ).getPet_id()+"}";
                        sendCZ(json0, CONSTS.ADD_FOLLOW_URL,"关注");
                    }else {
                        ((TextView)v).setText("＋ 粉丝团");
                        String json1 = "{\"userId\":\""+Login.getUserId( getActivity() )
                                +"\",\"petId\":"+itsMCBitems.get( position ).getPet_id()+"}";
                        sendCZ(json1,CONSTS.DEL_FOLLOW_URL,"取消关注");
                    }
                    break;
            }
        }
    };
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
}
