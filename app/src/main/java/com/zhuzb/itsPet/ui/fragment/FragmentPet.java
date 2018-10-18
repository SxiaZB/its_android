package com.zhuzb.itsPet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.model.pet.PetUserItem;
import com.zhuzb.itsPet.model.pet.UpdataPetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.ui.acticity.AddPetActivity;
import com.zhuzb.itsPet.ui.acticity.LeftMainActivity;
import com.zhuzb.itsPet.ui.acticity.UpdataPetActivity;
import com.zhuzb.itsPet.ui.adapter.pet.PetAdapter;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 宠物页面
 * @author zhuzb
 * @date on 2018/4/10 0010
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.main_pet_index)
public class FragmentPet extends Fragment {
    @ViewInject( value = R.id.pet_add_bt)
    private Button btAddPet;
    @ViewInject( value = R.id.pet_item)
    private ListView listView;

    private List<PetItem> petUserItemList;
    private PetAdapter petAdapter;


    //回调父级页面
    private LeftMainActivity leftMainActivity ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_pet_index,null);
        x.view().inject(this, view);
        leftMainActivity = (LeftMainActivity) getActivity();
        initData();
        initData( Login.getUserId( getActivity() ));
        return view;
    }

    /**
     * 加载监听器
     */
    private void initData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              startComment( i );
            }
        });
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
                        if ( x2 - x1 < 0
                                && (Math.abs( x2 - x1 ) > 200)) {
                            leftMainActivity.isShowa( 2,0);
                        }else if( x2 - x1 > 0
                                && (Math.abs( x2 - x1 ) > 200)) {
                            leftMainActivity.isShowa( 0,1);
                        }
                        break;
                }
                return false;
            }
        } );
    }

    /**
     * 加载数据
     * @param userId
     */
    public void initData(final String userId){
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequest( CONSTS.LIST_CW_USER_URL,
                        "{\"userId\":\"" + userId + "\"}",
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                errorToast( "加载宠物失败！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        petUserItemList = data.getData();
                                        getActivity().runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                petAdapter = new PetAdapter( getActivity(), petUserItemList );
                                                listView.setAdapter( petAdapter );
                                            }
                                        } );
                                    } else {
                                        errorToast( "加载宠物失败！" );
                                    }
                                } else {
                                    errorToast( "加载宠物失败！" );
                                }
                            }
                        } );
            }
        } ).start();
    }

    @Event( value = R.id.pet_add_bt)
    private void addPet(View view){
//        Toast.makeText(getActivity(), "点击添加按钮", Toast.LENGTH_LONG).show();
        // 给bnt1添加点击响应事件
        Intent intent = new Intent( getActivity(), AddPetActivity.class );
        //启动
        startActivity( intent );
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
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    /**
     * 跳转修改页面
     * @param position
     */
    private void startComment(int position) {
        UpdataPetItem noteData = new UpdataPetItem(  petUserItemList.get( position ) );
        Intent intent = new Intent( getActivity(), UpdataPetActivity.class );
        intent.putExtra("data", noteData);
        startActivity( intent );
    }
}
