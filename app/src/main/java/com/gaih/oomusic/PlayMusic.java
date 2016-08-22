package com.gaih.oomusic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;


import com.gaih.oomusic.Adapter.Music;
import com.gaih.oomusic.Service.Iservice;

import java.util.ArrayList;

/**
 * Created by gaih on 2016/8/21.
 */

public class PlayMusic extends AppCompatActivity {

    private Button stop;
    private Button before;
    private Button after;
    private static int currentPosition;
    private static int duration;
    private static SeekBar sbar;
    private static int mPosition;
    private Iservice iservice;
    private ArrayList<Music> musicList = new ArrayList<Music>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmusic);
        sbar = (SeekBar) findViewById(R.id.seekBar1);

    }


    public static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            duration = bundle.getInt("duration");
            currentPosition = bundle.getInt("currentPosition");
            sbar.setMax(duration);
            sbar.setProgress(currentPosition);
            Log.d("bbb2"+duration,""+currentPosition);
        }
    };
}
