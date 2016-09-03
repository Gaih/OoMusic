package com.gaih.oomusic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showCamera() {
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("文件权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//再次执行请求
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForCamera() {
        Toast.makeText(this, "拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForCamera() {
        Toast.makeText(this, "彻底拒绝", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private static TextView tSong;
    private static TextView tSinger;
    private static ImageView mPlay;
    private ImageView mNext;
    private static ImageView mAlbum;
    private static ProgressBar mProgressbar;
    private static int mPosition;
    private static boolean isPlaying;
    private Myconn myconn;
    private Iservice iservice;
    private FragmentAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolBar;
    private static ArrayList<Music> musicList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MainActivityPermissionsDispatcher.showCameraWithCheck(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        myconn = new Myconn();
        bindService(intent, myconn, BIND_AUTO_CREATE);
//        Window window = this.getWindow();
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

        pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        tSong = (TextView) findViewById(R.id.tv_song);
        tSinger = (TextView) findViewById(R.id.tv_singer);
        mAlbum = (ImageView) findViewById(R.id.iv_album);
        mPlay = (ImageView) findViewById(R.id.iv_play);
        mNext = (ImageView) findViewById(R.id.iv_next);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mProgressbar = (ProgressBar) findViewById(R.id.mProgressBar);
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
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            musicList = bundle.getParcelableArrayList("newList");
            isPlaying = bundle.getBoolean("isPlaying");

            mProgressbar.setMax(duration);
            mProgressbar.setProgress(currentPosition);
            tSinger.setText(musicList.get(mPosition).getSinger());
            tSong.setText(musicList.get(mPosition).getName());
            mAlbum.setImageBitmap(musicList.get(mPosition).getBitmap());
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
            default:
                break;

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
