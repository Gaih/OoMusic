package com.gaih.oomusic.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.gaih.oomusic.Adapter.Music;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 */

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        ArrayList<Music> receiver = intent.getParcelableArrayListExtra("gai");
//        Log.d("广播",receiver.get(0).getName());
    }
}
