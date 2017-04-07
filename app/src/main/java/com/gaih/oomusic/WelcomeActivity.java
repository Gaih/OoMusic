package com.gaih.oomusic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/9/5.
 */
@RuntimePermissions

public class WelcomeActivity extends AppCompatActivity {

    @NeedsPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE,maxSdkVersion = 24)
    void showToast() {
        final Intent it = new Intent(this, MainActivity.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                finish();
            }
        };
        timer.schedule(task, 400 * 1); //10秒后
        Toast.makeText(this, "获取存储卡权限", Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
//提示用户为什么需要此权限
    void showWhy(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("权限测试")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//再次执行请求
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
//一旦用户拒绝了
    void denied() {
        Toast.makeText(this, "真的不给权限吗", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
//用户选择的不再询问
    void notAsk() {
        Toast.makeText(this, "好的不问了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_main);
        WelcomeActivityPermissionsDispatcher.showToastWithCheck(this);
        int i = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        Log.d("ssss", "" + i);
//        if (i >= 0) {
//            final Intent it = new Intent(this, MainActivity.class); //你要转向的Activity
//            Timer timer = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    startActivity(it); //执行
//                    finish();
//                }
//            };
//            timer.schedule(task, 400 * 1); //10秒后
//        } else {
//            Toast.makeText(WelcomeActivity.this, "权限", Toast.LENGTH_SHORT).show();
//        }
    }

}
