package com.gaih.oomusic.Fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.StringCodec;
import com.gaih.oomusic.MainActivity;
import com.gaih.oomusic.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.gaih.oomusic.MainActivity.mContext;

/**
 * Created by gaih on 2016/8/20.
 */

public class Fragment03 extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab03,container,false);
        TextView username = (TextView) view.findViewById(R.id.username);
        TextView password = (TextView) view.findViewById(R.id.password);
        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login:
                new Thread(getRunnable).start();
//                Toast.makeText(mContext,"hahah",Toast.LENGTH_SHORT).show();
//                MediaPlayer player = new MediaPlayer();
//                try {
//                    player.setDataSource("http://mr3.doubanio.com/184aa7abff8a307d54af3a1c0df51bd5/0/fm/song/p348246_128k.mp4");
//                    player.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                player.start();
                break;
        }
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
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
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
            //post方法获取播放列表
            RequestBody formBody = new FormBody.Builder()
                    .add("app_name", "radio_android")
                    .add("version", "100")
                    .add("type", "n")
                    .add(" channel", "18")
                    .build();


            final Request request = new Request.Builder()
                    .url("https://api.douban.com/v2/fm/playlist?app_name=radio_android&version=100&type=n&channel=1")
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
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
                    Log.d("11111", "11112" + json);
                    JSONArray jsonArray = JSONArray.parseArray(json.getString("song"));
                    json = (JSONObject) jsonArray.get(0);
                    Log.d("11111", "11111" +json.get("title"));

                }
            });
        }
    };
}
