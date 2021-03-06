package com.gaih.oomusic.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.gaih.oomusic.Fragment.Fragment02;
import com.gaih.oomusic.MainActivity;
import com.gaih.oomusic.Adapter.Music;
import com.gaih.oomusic.Playing;
import com.gaih.oomusic.R;
import com.gaih.oomusic.ScanMusic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gaih on 2016/7/14.
 */

public class MusicService extends Service {
    private MediaPlayer player;
    private int currentPosition;
    private int duration;
    private int mPosition;
    private boolean isPlaying;
    private ArrayList<Music> newList;
    private android.support.v4.app.NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //startForeground(1, mBuilder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }

    public void pauseMusic() {

        if (player.isPlaying()) {
            player.pause();
        } else if (!player.isPlaying()) {
            player.start();
        }
    }

    public void replayMusic() {
        player.stop();
        stopForeground(true);
    }

    public void seekToPosition(int position) {
        player.seekTo(position);
    }

    private void updateSeekBar() {

        if (player.isPlaying()) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (player.isPlaying()) {
                            isPlaying = true;
                        } else if (!player.isPlaying()) {
                            isPlaying = false;
                        }
                        duration = player.getDuration();
                        currentPosition = player.getCurrentPosition();

                        LocalBroadcastManager localBroadcastManager = null;
                        IntentFilter intentFilter;
                        localBroadcastManager = localBroadcastManager.getInstance(getApplicationContext());//获取实例
                        intentFilter = new IntentFilter();
                        intentFilter.addAction("com");
//                        OnReceiver OnReceiver = new OnReceiver();
//                        localBroadcastManager.registerReceiver(OnReceiver, intentFilter);//注册本地广播监听器

                        Intent intent = new Intent("com");
                        intent.putExtra("duration",duration+"");
                        intent.putExtra("current",currentPosition+"");
                        localBroadcastManager.sendBroadcast(intent);//发送本地广播

                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putBoolean("isPlaying", isPlaying);
                        bundle.putParcelableArrayList("newList", newList);
                        bundle.putInt("currentPosition", currentPosition);
                        bundle.putInt("mPosition", mPosition);

                        msg.setData(bundle);
//                        msg2.setData(bundle);
                        MainActivity.handler.sendMessage(msg);
//                        Playing.handler.sendMessage(msg2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            timer.schedule(task, 1000, 300);
        }

    }
    private void notification(String name, String singer, Bitmap bitmap) {
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("" + name)
                .setContentText(name + "--" + singer)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.icon);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    mBuilder.setProgress(duration, currentPosition, false);
                    mNotificationManager.notify(1, mBuilder.build());
                    Log.d(currentPosition + "", "" + duration);
                    while (currentPosition == duration) {
                        mBuilder.setProgress(0, 0, false);
                    }
                }

            }
        };
        timer.schedule(task, 0, 1000);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        1,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        startForeground(1, mBuilder.build());
    }


    public void playMusic(int position, final ArrayList<Music> newList) {
        final int finalPosition = position;
        this.newList = newList;
        try {
            if (player!=null){
                player.release();
            }
            player = new MediaPlayer();
            player.reset();
            player.setDataSource(newList.get(position).getUri());
            player.prepareAsync();

            mPosition = position;
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                    updateSeekBar();
                    notification(newList.get(finalPosition).getName(), newList.get(finalPosition).getSinger(), newList.get(finalPosition).getBitmap());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MusicService.this.player.release();
                MusicService.this.player = null;
                if (finalPosition == newList.size()) {
                    playMusic(0, newList);
                }
                playMusic(finalPosition + 1, newList);
                Log.d("1111", "方法执行" + finalPosition);
            }
        });
    }


    private class MyBinder extends Binder implements Iservice {
        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        @Override
        public void callPlayMusic(final int position, ArrayList<Music> musicList) {

            playMusic(position, musicList);

        }

        @Override
        public void callReplayMusic() {
            replayMusic();
        }

        @Override
        public void callSeekToPosition(int position) {
            seekToPosition(position);
        }


    }

}
