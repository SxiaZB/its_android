package com.zhuzb.itsPet.ui.acticity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.hh.timeselector.timeutil.datedialog.DateListener;
import com.hh.timeselector.timeutil.datedialog.TimeConfig;
import com.hh.timeselector.timeutil.datedialog.TimeSelectorDialog;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.XCRoundImageView;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/5/25 0025
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.add_pet_activity)
public class AddPetActivity extends Activity {
    @ViewInject(value = R.id.add_pet_baocun_bt)
    private Button baocunBT;
    @ViewInject(value = R.id.add_pet_userimg)
    private XCRoundImageView petImgView;
    @ViewInject(value = R.id.add_pet_name_et)
    private EditText etName;
    @ViewInject(value = R.id.add_pet_xingbie_sp)
    private Spinner spinnerView;
    @ViewInject(value = R.id.add_pet_pz_et)
    private EditText etPinzhong;
    @ViewInject(value = R.id.add_pet_time)
    private TextView tvTime;
    @ViewInject(value = R.id.add_pet_time_bt)
    private Button timeBT;
    @ViewInject(value = R.id.add_pet_beizu_et)
    private EditText etBeiZu;
    @ViewInject(value = R.id.add_pet_follow_et)
    private EditText etFengSi;

    ProgressDialog pDialog;//进度窗口

    private String yearNow = "2018";//出生年
    private String monthNow = "01";//出生月
    private String dayNow = "01";//出生日
    private String petName;//姓名
    private String petXB;//性别
    private String petPZ;//品种
    private String petBZ;//宠物备注
    private String petFS;//粉丝名

    private boolean isSend = false;//图片是否选择标识位
    private File fileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        x.view().inject( this );
        baocunBT.setEnabled( true );
//        initData();
    }

    @Event(value = {R.id.add_pet_baocun_bt, R.id.add_pet_time_bt, R.id.add_pet_userimg0, R.id.add_pet_bt})
    private void addPet(View view) {
        switch (view.getId()) {
            case R.id.add_pet_baocun_bt:
                addPetBT();
                break;
            case R.id.add_pet_time_bt:
                getTime();
                break;
            case R.id.add_pet_userimg0:
                //打开手机的图库，选择图片;
                Intent intent;
                intent = new Intent();
                intent.setType( "image/*" );
                intent.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( intent, 0x1 );
                break;
            case R.id.add_pet_bt:
                AddPetActivity.this.finish();
                break;
        }
    }

    /**
     * 选择时间
     */
    private void getTime() {
        //时间选择控件
        TimeSelectorDialog dialog = new TimeSelectorDialog( this );
        //设置标题
        dialog.setTimeTitle( "选择时间:" );
        //显示类型
        dialog.setIsShowtype( TimeConfig.YEAR_MONTH_DAY );
        //默认时间
        dialog.setCurrentDate( "2018-01-11　00:00" );
        //隐藏清除按钮
        dialog.setEmptyIsShow( false );
        //设置起始时间
        dialog.setStartYear( 1990 );
        dialog.setDateListener( new DateListener() {
            @Override
            public void onReturnDate(String time, int year, int month, int day, int hour, int minute, int isShowType) {
                Toast.makeText( getApplication(), time, Toast.LENGTH_SHORT ).show();
                yearNow = String.valueOf( year );
                monthNow = String.valueOf( month + 1 );
                dayNow = String.valueOf( day );
                tvTime.setText( yearNow + "年" + monthNow + "月" + dayNow + "日" );
            }

            @Override
            public void onReturnDate(String empty) {
            }
        } );
        dialog.show();
    }

    /**
     * 添加宠物
     */
    private void addPetBT() {
        petName = etName.getText().toString().trim();
        petPZ = etPinzhong.getText().toString().trim();
        petXB = spinnerView.getSelectedItem().toString();
        petBZ = etBeiZu.getText().toString().trim();
        petFS = etFengSi.getText().toString().trim();
        if (!isSend) {
            errorToast( "请添加一张图片！" );
        } else if (petPZ.length() == 0) {
            errorToast( "请输入宠物品种！" );
        } else if (petName.length() == 0) {
            errorToast( "请输入宠物姓名！" );
        } else {
            if (petBZ.length() == 0) {
                petBZ = "宠物界最可爱";
            }
            if (petFS.length() == 0) {
                petFS = "宠粉";
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put( "userId", Login.getUserId( getApplication() ));
            map.put( "petName", petName );
            map.put( "petSex", petXB );
            map.put( "petBreed", petPZ );
            map.put( "petYear", yearNow );
            map.put( "petMonth", monthNow );
            map.put( "petDay", dayNow );
            map.put( "petGrade", "潜力新宠" );
            map.put( "petValue", "0" );
            map.put( "followName", petFS );
            map.put( "petBZ", petBZ );
            sendPet( fileImg,map );
        }
    }

    /**
     * 发表宠物笔记
     */
    private void sendPet(final File file, final Map<String, String> params) {
        showDialg( "正在添加新成员..." );
        baocunBT.setEnabled( false );
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequestFromdata( CONSTS.ADD_PET_URL,
                        file,
                        params,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                pDialog.dismiss();
                                errorToast( "添加失败！" );
                                baocunBT.setEnabled( true );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        pDialog.dismiss();
                                        errorToast( "添加成功！" );
                                        AddPetActivity.this.finish();
                                    } else {
                                        pDialog.dismiss();
                                        baocunBT.setEnabled( true );
                                        errorToast( data.getErrorMessage() );
                                    }
                                } else {
                                    pDialog.dismiss();
                                    errorToast( "添加失败！" );
                                    baocunBT.setEnabled( true );
                                }
                            }
                        } );
            }
        } ).start();
    }

    private void showDialg(String message) {
        if (null == pDialog) {
            pDialog = new ProgressDialog( this );
        }
        pDialog.setMessage( message );
        pDialog.show();
    }

    /**
     * 调用系统控件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1) {
            if (data != null) {
                Uri uri = data.getData();
                cutImg( uri );
            } else {
                return;
            }
        }
        if (requestCode == 0x2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                //得到图片
                Bitmap bitmap = bundle.getParcelable( "data" );
                //保存到图片到本地
                fileImg = saveImg( bitmap );
                //设置图片
                petImgView.setImageBitmap( bitmap );
                //更新标识位
                isSend = true;
            } else {
                return;
            }
        }
    }


    //保存图片到本地
    private File saveImg(Bitmap mBitmap) {
        File filePath = new File( "sdcard/itspic" );
        filePath.mkdirs();
        File file = new File( "sdcard/itspic/img1.jpg" );//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( file ) );
            mBitmap.compress( Bitmap.CompressFormat.JPEG, 100, bos );
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //裁剪图片
    private void cutImg(Uri uri) {
        if (uri != null) {
            Intent cutIntent = new Intent( "com.android.camera.action.CROP" );
            cutIntent.setDataAndType( uri, "image/*" );
            //true:出现裁剪的框
            cutIntent.putExtra( "crop", "true" );
            //裁剪宽高时的比例
            cutIntent.putExtra( "aspectX", 1 );
            cutIntent.putExtra( "aspectY", 1 );
            //裁剪后的图片的大小
            cutIntent.putExtra( "outputX", 300 );
            cutIntent.putExtra( "outputY", 300 );
            cutIntent.putExtra( "return-data", true );  // 返回数据
            cutIntent.putExtra( "output", uri );
            cutIntent.putExtra( "scale", true );
            startActivityForResult( cutIntent, 0x2 );
        } else {
            return;
        }
    }

    /**
     * 错误提示
     *
     * @param error
     */
    private void errorToast(final String error) {
        AddPetActivity.this.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( AddPetActivity.this, error, Toast.LENGTH_SHORT ).show();
            }
        } );
    }
}
