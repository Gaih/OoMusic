package com.gaih.oomusic.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.gaih.oomusic.DividerItemDecoration;
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
    private Iservice iservice;
    private ArrayList<Music> musicList = new ArrayList<Music>();

    private MusicAdapter musicAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        musicAdapter = new MusicAdapter(musicList);
        mRecyclerView.setAdapter(musicAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this.getContext(),DividerItemDecoration.VERTICAL_LIST
        ));
        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.animate()
                            .translationZ(15f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        view.animate()
                                                .translationZ(1f)
                                                .setDuration(500)
                                                .start();
                                    }
                                }
                            }).start();
                }
                iservice.callPlayMusic(position, musicList);
            }
        });
//        MusicAdapter adapter = new MusicAdapter(this.getContext(), R.layout.music_item, musicList);

//        lv = (ListView) view.findViewById(R.id.lv);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                iservice.callPlayMusic(position,musicList);
//            }
//        });


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
