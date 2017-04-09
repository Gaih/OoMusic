package com.gaih.oomusic.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.gaih.oomusic.Adapter.MainPageAdapter;
import com.gaih.oomusic.Adapter.MainPager;
import com.gaih.oomusic.DividerItemDecoration;
import com.gaih.oomusic.DoubanFM.GetFmList;
import com.gaih.oomusic.DoubanFM.GetMusicList;
import com.gaih.oomusic.Service.Iservice;
import com.gaih.oomusic.Service.MusicService;
import com.gaih.oomusic.R;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.gaih.oomusic.MainActivity.mContext;

/**
 * Created by gaih on 2016/8/20.
 */

public class FragmentFM extends Fragment {

    private View view;
    private Myconn myconn;
    private Iservice iservice;
    private ArrayList<MainPager> musicList = new ArrayList<MainPager>();

    private MainPageAdapter mainAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab01, container, false);
        GetFmList fmList = new GetFmList();
        fmList.getMainList(mContext, mHandler);
        mDialog = new ProgressDialog(mContext);
        mDialog.setTitle("正在登录");
        mDialog.setMessage("正在登陆服务器，请稍后...");
        mDialog.show();//        initView();
        return view;
    }

    private void initView() {
        Intent intent = new Intent(this.getContext(), MusicService.class);
        getActivity().startService(intent);
        myconn = new Myconn();
        getActivity().bindService(intent, myconn, BIND_AUTO_CREATE);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainPageAdapter(musicList);
        mRecyclerView.setAdapter(mainAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this.getContext(), DividerItemDecoration.VERTICAL_LIST
        ));
        mainAdapter.setOnItemClickListener(new MainPageAdapter.OnItemClickListener() {
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
                GetMusicList music = new GetMusicList();
                music.getMainList(position+1, mHandler);            }
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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case 0:
                    ArrayList<MainPager> arrayList = msg.getData().getParcelableArrayList("json");
                    for (MainPager page :
                            arrayList) {
                        musicList.add(page);
                    }
//                    Log.d("11111", "2222" + name + intro + bmp);
//                    MainPager pager = new MainPager(name, intro, bmp);
                    mDialog.dismiss();
                    initView();

                    break;
                case 1:

                    break;
                case 2:

                    break;
            }
        }
    };

}
