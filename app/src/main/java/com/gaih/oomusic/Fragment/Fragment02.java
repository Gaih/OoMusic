package com.gaih.oomusic.Fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gaih.oomusic.Adapter.Music;
import com.gaih.oomusic.Service.Iservice;
import com.gaih.oomusic.Service.MusicService;
import com.gaih.oomusic.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by gaih on 2016/8/20.
 */

public class Fragment02 extends Fragment  {
    ImageView fm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.tab02, container, false);
         fm = (ImageView) view.findViewById(R.id.FM);
//        fm.setImageURI(Uri.parse("http://img7.doubanio.com/img/fmadmin/medium/26385.jpg"));


        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                String url = "http://img7.doubanio.com/img/fmadmin/medium/26385.jpg";

                Bitmap bmp = getURLimage(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();
        return view;

    }
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    Bitmap bmp=(Bitmap)msg.obj;
                    fm.setImageBitmap(bmp);
                    break;
            }
        };
    };
    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
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
