package com.gaih.oomusic.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by gaih on 2016/8/20.
 */

public class Fragment02 extends Fragment implements View.OnClickListener {
    private static SeekBar sbar;
    private Button stop;
    private Myconn myconn;
    private Button next;
    private Button last;
    private static TextView name;
    private static TextView singer;
    private static ImageView album;
    private static ArrayList<Music> newList;
    private Iservice iservice;
    private static int mPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.title, container, false);
        Intent intent = new Intent(this.getContext(), MusicService.class);
        getActivity().startService(intent);
        myconn = new Fragment02.Myconn();
        getActivity().bindService(intent, myconn, BIND_AUTO_CREATE);



        stop = (Button) view.findViewById(R.id.palying);
        last = (Button) view.findViewById(R.id.before);
        next = (Button) view.findViewById(R.id.after);
        name = (TextView) view.findViewById(R.id.fg2_name);
        singer = (TextView) view.findViewById(R.id.fg2_singer);
        album = (ImageView) view.findViewById(R.id.fg2_album);

        stop.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
        sbar = (SeekBar) view.findViewById(R.id.seekBar1);
        sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iservice.callSeekToPosition(seekBar.getProgress());
                Log.d("aaaa" + seekBar.getProgress(), "aaaa");
            }
        });
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.palying:
                iservice.callPauseMusic();
                break;
            case R.id.before:
                iservice.callPlayMusic(mPosition - 1,newList);
                break;
            case R.id.after:
                iservice.callPlayMusic(mPosition + 1,newList);
                break;
            default:
                break;

        }

    }

    private class Myconn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iservice = (Iservice) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            mPosition = bundle.getInt("mPosition");
            newList = bundle.getParcelableArrayList("newList");

            name.setText(newList.get(mPosition).getName());
            singer.setText(newList.get(mPosition).getSinger());
            album.setImageBitmap(newList.get(mPosition).getBitmap());

            sbar.setMax(duration);
            sbar.setProgress(currentPosition);
        }
    };
}
