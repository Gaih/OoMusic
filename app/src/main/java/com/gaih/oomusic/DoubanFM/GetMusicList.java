package com.gaih.oomusic.DoubanFM;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.StringCodec;
import com.gaih.oomusic.Adapter.MainPager;
import com.gaih.oomusic.Adapter.Music;
import com.gaih.oomusic.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gaih on 17-4-7.
 */

public class GetMusicList {
    private ArrayList<MainPager> mainList;
    private String getUrl;
    private Handler mHandler;


    public void getMainList(String channelId, Handler mHandler) {
        this.mHandler = mHandler;
        getUrl =
                "https://api.douban.com/v2/fm/playlist?alt=json&apikey=02646d3fb69a52ff072d47bf23cef8fd&app_name=radio_iphone&channel="+
                        channelId+"&client=s%3Amobile%7Cy%3AiOS%2010.2%7Cf%3A115%7Cd%3Ab88146214e19b8a8244c9bc0e2789da68955234d%7Ce%3AiPhone7%2C1%7Cm%3Aappstore&douban_udid=b635779c65b816b13b330b68921c0f8edc049590&formats=aac&kbps=128&pt=0.0&type=n&udid=b88146214e19b8a8244c9bc0e2789da68955234d&version=115";
        Log.d("url地址","url"+getUrl);
        new Thread(getRunnable).start();
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //创建okHttpClient对象post方法
            OkHttpClient mOkHttpClient = new OkHttpClient();
            //post方法获取播放列表
            RequestBody formBody = new FormBody.Builder()
                    .add("apikey", "02646d3fb69a52ff072d47bf23cef8fd")
                    .add("client_id", "02646d3fb69a52ff072d47bf23cef8fd")
                    .add("client_secret", "cde5d61429abcd7c")
                    .add(" udid", "b88146214e19b8a8244c9bc0e2789da68955234d")
                    .add("douban_udid", "b635779c65b816b13b330b68921c0f8edc049590")
                    .add("device_id", "b88146214e19b8a8244c9bc0e2789da68955234d")
                    .add("grant_type", "password")
                    .add("redirect_uri", "http://www.douban.com/mobile/fm")
                    .add("username", "18612311132")
                    .add("password", "gaiaihuan*1314")
                    .build();


            final Request request = new Request.Builder()
                    .url("https://www.douban.com/service/auth2/token")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build();


            Call call = mOkHttpClient.newCall(request);
//请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("22222", "22222");

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 输出返回结果
                    InputStream input = response.body().byteStream();
                    int resLen = 0;
                    byte[] res = new byte[1024];
                    StringBuilder sb = new StringBuilder();
                    while ((resLen = input.read(res)) != -1) {
                        sb.append(new String(res, 0, resLen));
                    }
//                    Message msg = mHandler.obtainMessage();
//                    if (sb.toString().contains("success")) {
//                        String[] name = sb.toString().split("。");
//                        Log.d("用户名:", name[1]);
//                        username = name[1];
//                        msg.what = 0;
//                        mHandler.sendMessage(msg);
//                    } else {
//                        msg.what = 1;
//                        mHandler.sendMessage(msg);
//                    }
                    Log.d("11111", "11111" + sb.toString());

                }
            });
        }
    };

    Runnable getRunnable = new Runnable() {
        @Override
        public void run() {
            //创建okHttpClient对象post方法
            OkHttpClient mOkHttpClient = new OkHttpClient();


            final Request request = new Request.Builder()
                    .url(getUrl)
//                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .get()
                    .build();


            Call call = mOkHttpClient.newCall(request);
//请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("22222", "22222");

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 输出返回结果
                    InputStream input = response.body().byteStream();
                    int resLen = 0;
                    byte[] res = new byte[1024];
                    StringBuilder sb = new StringBuilder();
                    while ((resLen = input.read(res)) != -1) {
                        sb.append(new String(res, 0, resLen));
                    }
                    String jsonStr = sb.toString();
                    //String转换成JSON
                    //Json的解析类对象
                    JSONObject json;
                    json = JSONObject.parseObject(jsonStr);
//                    Log.d("11111", "11112" + json);
                                        JSONArray array = JSONArray.parseArray(json.get("song").toString());

                    Log.d("11111", "11113" + array.get(0));
                    json =JSONObject.parseObject( array.get(0).toString());
//                    Log.d("11111", "11114" + json.get("singers"));
//                    Log.d("11111", "11114" + json.get("url"));
                    array = JSONArray.parseArray(json.get("singers").toString());

                    JSONObject singers =JSONObject.parseObject(array.get(0).toString());
//                    Log.d("11111", "11115" + singers.get("name"));
                    Bitmap bmp = getURLimage(json.get("picture").toString());
                    Message message = new Message();
                    Looper.prepare();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", (String) json.get("url"));
                    bundle.putString("title", (String) json.get("title"));
                    bundle.putString("name", singers.get("name").toString());
                    bundle.putParcelable("bmp", bmp);
                    message.setData(bundle);//bundle传值，耗时，效率低
                    mHandler.sendMessage(message);//发送message信息
                    message.what = 1;//标志是哪个线程传数据
                    Message message1 = new Message();
                    message1.setData(bundle);
                    MainActivity.handler.sendMessage(message1);

                    //message有四个传值方法，
                    //两个传int整型数据的方法message.arg1，message.arg2
                    //一个传对象数据的方法message.obj
                    //一个bandle传值方法
//                    mHandler.obtainMessage(0, jsonStr).sendToTarget();//向ui线程发送SUCCESS标识和数据
                }
            });
        }
    };

    //根据url获取bitmap图像
    public Bitmap getURLimage(String cover) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(cover);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
