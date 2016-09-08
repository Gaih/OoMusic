package com.gaih.oomusic;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gaih.oomusic.Service.Iservice;
import com.gaih.oomusic.Service.MusicService;

import java.util.Timer;
import java.util.TimerTask;

import static com.gaih.oomusic.MainActivity.currentPosition;
import static com.gaih.oomusic.MainActivity.duration;
import static com.gaih.oomusic.MainActivity.mPosition;
import static com.gaih.oomusic.MainActivity.musicList;

/**
 * Created by Administrator on 2016/9/7.
 */

public class Playing extends AppCompatActivity implements View.OnClickListener {
//    private static ArrayList<Music> musicList = new ArrayList<>();
    private ImageView mImageView;
    private TextView mSinger;
    private TextView mName;
    private Button mUp;
    private Button mNext;
    private Button mPause;
    private static SeekBar mSeekBar;
    private Myconn myconn;
    private Iservice iservice;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingmusic);


        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        myconn = new Playing.Myconn();
        bindService(intent, myconn, BIND_AUTO_CREATE);


        mSinger = (TextView) findViewById(R.id.singer2);
        mName = (TextView) findViewById(R.id.name2);
        mUp = (Button) findViewById(R.id.before2);
        mPause = (Button) findViewById(R.id.palying2);
        mNext = (Button) findViewById(R.id.after2);
        mUp.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        mImageView = (ImageView) findViewById(R.id.album2);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    mName.setText(musicList.get(mPosition).getName());
                    mSinger.setText(musicList.get(mPosition).getSinger());
                    mImageView.setImageBitmap(musicList.get(mPosition).getBitmap());
                    mSeekBar.setMax(duration);
                    mSeekBar.setProgress(currentPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        timer.schedule(task, 1000, 300);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iservice.callSeekToPosition(mSeekBar.getProgress());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.palying2:
                iservice.callPauseMusic();
                break;
            case R.id.before2:
                iservice.callPlayMusic(mPosition-1,musicList);
                break;
            case R.id.after2:
                iservice.callPlayMusic(mPosition+1,musicList);
                break;
        }

    }

    private class Myconn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iservice = (Iservice) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
