package com.zhuzb.itsPet.utils;

import android.util.Log;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author zhuzb
 * @date on 2018/5/19 0019
 * @email zhuzhibo2017@163.com
 */

public class OkhttpUtils {
    public static final MediaType MEDIA_TYPE_MARKDOWN_0
            = MediaType.parse( "application/json; charset=utf-8" );
    public static final MediaType MEDIA_TYPE_MARKDOWN_1
            = MediaType.parse( "multipart/form-data; charset=utf-8" );

    private static OkHttpClient client = new OkHttpClient();

    /**
     * get请求
     *
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url( address )
                .build();
        client.newCall( request ).enqueue( callback );
    }

    /**
     * post请求
     *
     * @param address
     * @param json
     * @param callback
     */
    public static void sendOkHttpRequest(String address, String json, okhttp3.Callback callback) {
        RequestBody body = RequestBody.create( MEDIA_TYPE_MARKDOWN_0, json );
        Request request = new Request.Builder()
                .url( address )
                .post( body )
                .build();
        client.newCall( request ).enqueue( callback );
    }

    /**
     * fromdata带参数带文件post请求
     *
     * @param address
     * @param file
     * @param params
     * @param callback
     */
    public static void sendOkHttpRequestFromdata(String address, File file, Map<String, String> params, okhttp3.Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType( MultipartBody.FORM );
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create( MediaType.parse( "image/*" ), file );
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            builder.addFormDataPart( "file", file.getName(), body );
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addFormDataPart( entry.getKey(), entry.getValue() );
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url( address )
                .post( body )
                .build();
        client.newCall( request ).enqueue( callback );
    }

    /**
     * fromdata带参数post请求
     *
     * @param address
     * @param params
     * @param params
     * @param callback
     */
    public static void sendOkHttpRequestFromdata0(String address,  Map<String, String> params, okhttp3.Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType( MultipartBody.FORM );
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addFormDataPart( entry.getKey(), entry.getValue() );
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url( address )
                .post( body )
                .build();
        client.newCall( request ).enqueue( callback );
    }
}
