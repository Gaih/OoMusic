package com.gaih.oomusic;


import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static com.gaih.oomusic.MainActivity.currentPosition;
import static com.gaih.oomusic.MainActivity.duration;
import static com.gaih.oomusic.MainActivity.mPosition;
import static com.gaih.oomusic.MainActivity.musicList;

/**
 * Created by Administrator on 2016/9/7.
 */

public class Playing extends AppCompatActivity {
//    private static ArrayList<Music> musicList = new ArrayList<>();
    private ImageView mImageView;
    private TextView mSinger;
    private TextView mName;
    private Button mUp;
    private Button mNext;
    private Button mPause;
    private static SeekBar mSeekBar;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingmusic);



        mSinger = (TextView) findViewById(R.id.singer2);
        mName = (TextView) findViewById(R.id.name2);
        mUp = (Button) findViewById(R.id.before2);
        mPause = (Button) findViewById(R.id.palying2);
        mNext = (Button) findViewById(R.id.after2);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        mImageView = (ImageView) findViewById(R.id.album2);


        mName.setText(musicList.get(mPosition).getName());
        mSinger.setText(musicList.get(mPosition).getSinger());
        mImageView.setImageBitmap(musicList.get(mPosition).getBitmap());
        mSeekBar.setMax(duration);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    mSeekBar.setProgress(currentPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        timer.schedule(task, 1000, 300);
    }
}
