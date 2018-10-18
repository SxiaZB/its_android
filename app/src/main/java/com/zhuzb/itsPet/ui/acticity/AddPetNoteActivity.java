package com.zhuzb.itsPet.ui.acticity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhuzb.itsPet.R;
import com.zhuzb.itsPet.config.CONSTS;
import com.zhuzb.itsPet.model.pet.PetItem;
import com.zhuzb.itsPet.ui.Login;
import com.zhuzb.itsPet.utils.OkhttpUtils;
import com.zhuzb.itsPet.utils.json.GsonUtils;
import com.zhuzb.itsPet.utils.json.JsonData;

import org.xutils.common.util.LogUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author zhuzb
 * @date on 2018/5/21 0021
 * @email zhuzhibo2017@163.com
 */
@ContentView(value = R.layout.add_pet_note_activity)
public class AddPetNoteActivity extends Activity {
    @ViewInject(value = R.id.add_pet_note_time)
    private TextView time;//时间
    @ViewInject(value = R.id.add_pet_note_img)
    private ImageView imageView;
    @ViewInject(value = R.id.add_pet_note_chongwu_sp)
    private Spinner spinnerView;
    @ViewInject(value = R.id.add_pet_note_baocun_bt)
    private Button baocunBT;
    @ViewInject(value = R.id.add_pet_note_liuyan_et)
    private EditText editText;

    private List listCWuser;//宠物用户列表
    private List<PetItem> petItemList;

    ArrayAdapter<String> adapter;//宠物下拉加载
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );// HH:mm:ss
    SimpleDateFormat imgDateFormat = new SimpleDateFormat( "yyyyMMddHHmmss" );// HH:mm:ss

    ProgressDialog pDialog;//进度窗口

    private String noteTx;//留言
    private int spinnerNum = 0;//选中第几个
    private boolean isSend = false;//图片是否选择标识位
    private Bitmap imgNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        x.view().inject( this );
        initData();
    }

    private void initData() {
        Date date = new Date( System.currentTimeMillis() );
        time.setText( simpleDateFormat.format( date ) );
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerNum = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                } );
        getListCW(Login.getUserId( getApplication() ));

    }

    /**
     * 监听器
     *
     * @param view
     */
    @Event(value = {R.id.add_pet_note_img, R.id.add_pet_note_bt, R.id.add_pet_note_baocun_bt})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.add_pet_note_img:
                //打开手机的图库;
                Intent intent;
                intent = new Intent();
                intent.setType( "image/*" );
                //打开图片管理
                intent.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( intent, 0x1 );
                break;
            case R.id.add_pet_note_bt:
                AddPetNoteActivity.this.finish();
                break;
            case R.id.add_pet_note_baocun_bt:
                if (isSend) {
                    noteTx = editText.getText().toString().trim();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put( "userId", Login.getUserId( getApplication() ));
                    map.put( "petId", String.valueOf( (int) petItemList.get( spinnerNum ).getPet_id() ) );
                    if (noteTx.length() > 0) {
                        map.put( "noteText", noteTx );
                    } else {
                        map.put( "noteText", "我就静静看着你，你点赞吗？" );
                    }
                    sendNote( saveImg( imgNote ), map);
//                    Toast.makeText( AddPetNoteActivity.this, "发送成功！", Toast.LENGTH_SHORT ).show();
                } else {
                    errorToast( "请添加一张图片！" );
//                    Toast.makeText( AddPetNoteActivity.this, "请添加一张图片！", Toast.LENGTH_SHORT ).show();
                }
                break;
        }
    }

    private void showDialg(String message) {
        if (null == pDialog) {
            pDialog = new ProgressDialog( this );
        }
        pDialog.setMessage( message );
        pDialog.show();
    }

    /**
     * 选择图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            getImg( uri );
        } else {
            return;
        }
    }

    /**
     * 获取图片并设置
     *
     * @param uri
     */
    private void getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream( uri );
            // 从输入流中解码位图
            Bitmap bitmap = BitmapFactory.decodeStream( inputStream );
            // 保存位图
            imgNote = zoomImage( bitmap, 900, 600 );
            imageView.setImageBitmap(imgNote );
            //更新标识位
            isSend = true;
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放大小
     *
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public Bitmap zoomImage(Bitmap bgimage, double newWidth,
                            double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale( scaleWidth, scaleWidth );
        Bitmap bitmap = Bitmap.createBitmap( bgimage, 0, 0, (int) width,
                (int) height, matrix, true );
        return bitmap;
    }

    /**
     * 保存图片文件
     *
     * @param bitmap
     */
    private File saveImg(Bitmap bitmap) {
        File filePath = new File("sdcard/itspic");
        filePath.mkdirs();
        File file = new File( "sdcard/itspic/img0.jpg" );//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new     FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 发表宠物笔记
     */
    private void sendNote(final File file, final Map<String, String> params) {
        showDialg( "正在发表..." );
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequestFromdata( CONSTS.ADD_PET_NOTE_URL,
                        file,
                        params,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                pDialog.dismiss();
                                errorToast( "发表失败！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        pDialog.dismiss();
                                        errorToast( "发表成功！" );
                                        AddPetNoteActivity.this.finish();
                                    } else {
                                        pDialog.dismiss();
                                        errorToast( data.getErrorMessage() );
                                    }
                                } else {
                                    pDialog.dismiss();
                                    errorToast( "发表失败！" );
                                }
                            }
                        } );
            }
        } ).start();
    }


    /**
     * 加载宠物下拉框
     *
     * @param userId
     */
    private void getListCW(final String userId) {
        showDialg( "正在加载您的爱宠..." );
        new Thread( new Runnable() {
            @Override
            public void run() {
                OkhttpUtils.sendOkHttpRequest( CONSTS.LIST_CW_USER_URL,
                        "{\"userId\":\"" + userId + "\"}",
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                errorToast( "服务器异常！" );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //正常时
                                if (response.isSuccessful() && response.code() == 200) {
                                    JsonData data = GsonUtils.fromJson( response.body().string(), new TypeToken<JsonData<PetItem>>() {
                                    } );
                                    if (data.getErrorCode() == 0) {
                                        petItemList = data.getData();
                                        List<String> list = new ArrayList<String>();
                                        for (int i = 0; petItemList.size() > i; i++) {
                                            list.add( petItemList.get( i ).getPet_name() );
                                            LogUtil.e( "_______" + petItemList.get( i ).getPet_name() );
                                        }
                                        final String[] dataName = list.toArray( new String[list.size()] );
                                        AddPetNoteActivity.this.runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter = new ArrayAdapter<String>( AddPetNoteActivity.this,
                                                        android.R.layout.simple_dropdown_item_1line, dataName );
                                                spinnerView.setAdapter( adapter );
                                                LogUtil.e( "加载下拉列表" );
                                                if (dataName.length > 0) {
                                                    baocunBT.setEnabled( true );
                                                } else {
                                                    baocunBT.setEnabled( false );
                                                }
                                            }
                                        } );
                                        pDialog.dismiss();
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
     * 错误提示
     *
     * @param error
     */
    private void errorToast(final String error) {
        AddPetNoteActivity.this.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( AddPetNoteActivity.this, error, Toast.LENGTH_SHORT ).show();
            }
        } );
    }

}

