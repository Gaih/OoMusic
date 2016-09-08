package com.gaih.oomusic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gaih.oomusic.Adapter.FragmentAdapter;
import com.gaih.oomusic.Adapter.Music;
import com.gaih.oomusic.Adapter.MusicAdapter;
import com.gaih.oomusic.Fragment.Fragment01;
import com.gaih.oomusic.Fragment.Fragment02;
import com.gaih.oomusic.Fragment.Fragment03;
import com.gaih.oomusic.Service.Iservice;
import com.gaih.oomusic.Service.MusicService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    private static TextView tSong;
    private static TextView tSinger;
    private static ImageView mPlay;
    public static int duration;
    public static int currentPosition;

    private ImageView mNext;
    private static ImageView mAlbum;
    private static ProgressBar mProgressbar;
    public static int mPosition;
    private static boolean isPlaying;
    private Myconn myconn;
    private Iservice iservice;
    private FragmentAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolBar;
    public static ArrayList<Music> musicList = new ArrayList<>();
    private FrameLayout frameLayout;
    private static ImageView sideImg;
    private static TextView side_name;
    private static TextView side_singer;
    private MusicAdapter musicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

//        Window window = this.getWindow();
//        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        getWindow().setStatusBarColor(R.color.gray);

    }

    private void init() {


        toolBar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolBar.setNavigationIcon(R.drawable.toolbar_left);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        sideImg = (ImageView) headerView.findViewById(R.id.side_img);
        side_singer = (TextView) headerView.findViewById(R.id.side_singer);
        side_name = (TextView)headerView.findViewById(R.id.side_name);


        pagerAdapter = new FragmentAdapter(getFragmentManager(), this);

        viewPager = (ViewPager) findViewById(R.id.mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        tSong = (TextView) findViewById(R.id.tv_song);
        tSinger = (TextView) findViewById(R.id.tv_singer);
        mAlbum = (ImageView) findViewById(R.id.iv_album);
        mPlay = (ImageView) findViewById(R.id.iv_play);
        mNext = (ImageView) findViewById(R.id.iv_next);
        frameLayout = (FrameLayout)findViewById(R.id.mFrameLayout);
        frameLayout.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mProgressbar = (ProgressBar) findViewById(R.id.mProgressBar);
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        myconn = new Myconn();
        bindService(intent, myconn, BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            mPosition = bundle.getInt("mPosition");
            duration = bundle.getInt("duration");
            currentPosition = bundle.getInt("currentPosition");
            musicList = bundle.getParcelableArrayList("newList");
            isPlaying = bundle.getBoolean("isPlaying");

            mProgressbar.setMax(duration);
            mProgressbar.setProgress(currentPosition);
            tSinger.setText(musicList.get(mPosition).getSinger());
            tSong.setText(musicList.get(mPosition).getName());
            mAlbum.setImageBitmap(musicList.get(mPosition).getBitmap());

            sideImg.setImageBitmap(musicList.get(mPosition).getBitmap());
            side_singer.setText(musicList.get(mPosition).getSinger());
            side_name.setText(musicList.get(mPosition).getName());

            if (isPlaying) {
                mPlay.setImageResource(R.drawable.ic_play_bar_btn_pause);
            } else if (!isPlaying) {
                mPlay.setImageResource(R.drawable.ic_play_bar_btn_play);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                iservice.callPauseMusic();
                break;
            case R.id.iv_next:
                iservice.callPlayMusic(mPosition + 1, musicList);
                break;
            case R.id.mFrameLayout:
//                Fragment02 fragment02 = new Fragment02();
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction tx = fm.beginTransaction();
//                tx.add(R.id.activity_main,fragment02,"ONE");
//                tx.addToBackStack(null);
//                tx.commit();
                Intent intent=new Intent(MainActivity.this,Playing.class);
                startActivity(intent);


                Toast.makeText(MainActivity.this,"aaaaaa",Toast.LENGTH_SHORT).show();
            default:
                break;

        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
//                ScanMusic scanMusic = new ScanMusic();
//                scanMusic.scanMusic(MainActivity.this, musicList);
//
//
//                Fragment01 fragment01 = new Fragment01();
//                MusicAdapter musicAdapter = new MusicAdapter(musicList);
//                getFragmentManager().findFragmentByTag().getView().findViewById(R.id.recyclerView);
                Toast.makeText(getApplicationContext(), "扫描完成", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                //unbindService(myconn);
                Intent intent = new Intent(this, MusicService.class);
                stopService(intent);
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_singer) {
            // Handle the camera action
        } else if (id == R.id.nav_album) {

        } else if (id == R.id.nav_song) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
