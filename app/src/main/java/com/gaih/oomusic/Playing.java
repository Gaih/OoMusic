package com.gaih.oomusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gaih.oomusic.Adapter.Music;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7.
 */

public class Playing extends AppCompatActivity {
    private ArrayList<Music> musicList;
    private ImageView mImageView;
    private TextView mSinger;
    private TextView mName;
    private Button mUp;
    private Button mNext;
    private Button mPause;
    private SeekBar mSeekBar;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingmusic);

        Intent intent = getIntent();

        musicList = new ArrayList<>();
        String name = intent.getStringExtra("name");
        String singer = intent.getStringExtra("singer");
//        Bitmap bitmap = intent.getExtras("bitmap");


        mSinger = (TextView) findViewById(R.id.singer2);
        mSinger.setText(singer);
        mName = (TextView) findViewById(R.id.name2);
        mName.setText(name);
        mUp = (Button) findViewById(R.id.before2);
        mPause = (Button) findViewById(R.id.palying2);
        mNext = (Button) findViewById(R.id.after2);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        mImageView = (ImageView) findViewById(R.id.album2);


    }
}
