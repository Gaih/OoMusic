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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gaih.oomusic.MainActivity;
import com.gaih.oomusic.Service.Iservice;
import com.gaih.oomusic.Adapter.Music;
import com.gaih.oomusic.Adapter.MusicAdapter;
import com.gaih.oomusic.Service.MusicService;
import com.gaih.oomusic.R;
import com.gaih.oomusic.ScanMusic;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by gaih on 2016/8/20.
 */

public class Fragment01 extends Fragment {

    private View view;
    private Myconn myconn;
    private ListView lv;
    private Iservice iservice;
    private ArrayList<Music> musicList = new ArrayList<Music>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab01, container, false);
        initView();
        return view;
    }

    private void initView() {
        Intent intent = new Intent(this.getContext(), MusicService.class);
        getActivity().startService(intent);
        myconn = new Myconn();
        getActivity().bindService(intent, myconn, BIND_AUTO_CREATE);

        ScanMusic scanMusic = new ScanMusic();
        scanMusic.scanMusic(this.getContext(), musicList);
        MusicAdapter adapter = new MusicAdapter(this.getContext(), R.layout.music_item, musicList);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iservice.callPlayMusic(position,musicList);
            }
        });


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


}
