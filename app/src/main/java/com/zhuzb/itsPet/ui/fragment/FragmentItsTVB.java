package com.zhuzb.itsPet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.its.ItsMYitem;
import com.zhuzb.itsPet.model.its.NoteDataItem;
import com.zhuzb.itsPet.model.its.NoteListItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.acticity.AddPetNoteActivity;
import com.zhuzb.itsPet.ui.acticity.CommentActivity;
import com.zhuzb.itsPet.ui.acticity.LeftMainActivity;
import com.zhuzb.itsPet.ui.adapter.its.ItsTVBadapter;
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

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/4/20 0020
 * @email zhuzhibo2017@163.com
 */

@ContentView(value = R.layout.main_its_tvb)
public class FragmentItsTVB extends Fragment {
    @ViewInject(value = R.id.its_tvb_item)
    private ListView listView;
    @ViewInject(value = R.id.its_tvb_add_bt)
    private Button addNoteBt;

    private List<NoteListItem> itsMYitemList;
    private ItsTVBadapter itsTVBadapter;

    //回调父级页面
    private LeftMainActivity leftMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.main_its_tvb, null );
        x.view().inject( this, view );
        leftMainActivity = (LeftMainActivity) getActivity();
        initData();
        initData( Login.getUserId( getActivity() ) );
        return view;
    }

    private void initData() {
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startComment(i);
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
                            leftMainActivity.isShowb( 1 );
                        } else if (x2 - x1 > 0
                                && (Math.abs( x2 - x1 ) > 200)) {
                            leftMainActivity.isShowa( 0, 0 );
                        }
                        break;
                }
                return false;
            }
        } );
        addNoteBt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 给bnt1添加点击响应事件
                Intent intent = new Intent( getActivity(), AddPetNoteActivity.class );
                //启动
                startActivity( intent );
            }
        } );
    }
    /**
     * 跳转评论区
     * @param position
     */
    private void startComment(int position) {
        NoteDataItem noteData = new NoteDataItem( itsMYitemList.get( position ));
        Intent intent = new Intent( getActivity(), CommentActivity.class );
        intent.putExtra("noteData", noteData);
        startActivity( intent );
    }
    /**
     * 加载列表数据
     * @param userId
     */
    public void initData(final String userId) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                LogUtil.e( "关注数据加载中" );
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                OkhttpUtils.sendOkHttpRequest( CONSTS.MY_DATA_URL,
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
                                        itsMYitemList = data.getData();
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                itsTVBadapter = new ItsTVBadapter( getActivity(), itsMYitemList, mListener );
                                                listView.setAdapter( itsTVBadapter );
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
     * 错误提示
     * @param error
     */
    private void errorToast(final String error) {
        getActivity().runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( getActivity(), error, Toast.LENGTH_LONG ).show();
            }
        } );
    }


    /**
     * 实现类，响应按钮点击事件
     */
    private ItsTVBadapter.MyClickListener mListener = new ItsTVBadapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()) {
                case R.id.its_tvb_fb_tv:
                    startComment(position);
                    break;
                case R.id.its_tvb_bianji0:
                    Toast.makeText(
                            getActivity(),
                            "你点击了假删除！", Toast.LENGTH_SHORT )
                            .show();
                    break;
                case R.id.its_tvb_userimg:
                    Toast.makeText(
                            getActivity(),
                            "头像" + position, Toast.LENGTH_SHORT )
                            .show();
                    break;
            }
        }
    };
    /**
     * 刷新
     */
    public void updataMyFragment(int i){
        LogUtil.e("····刷新 我的····");
        initData( Login.getUserId( getActivity() ) );
    }
}