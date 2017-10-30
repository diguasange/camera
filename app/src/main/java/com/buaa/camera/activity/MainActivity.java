package com.buaa.camera.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.buaa.camera.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int PERMISSION_CODE = 122;
    private ImageButton cameraButton;
    private ImageButton instructionButton;
    private ImageButton setupButton;
    private long exitTime = 0;

    private final int IMAGE_CAMERA = 123;
    //private final int CUT_PHOTO = 124;
    //private final int PERMISSION_CODE = 122;
    //private final int IMAGE_ALBUM = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//加入后Nexus5X闪退
        setContentView(R.layout.activity_main);
        intiView();
    }

    private void intiView() {
        cameraButton = (ImageButton) findViewById(R.id.ImageButton1);
        instructionButton = (ImageButton) findViewById(R.id.ImageButton2);
        setupButton = (ImageButton) findViewById(R.id.Setup);
        cameraButton.setOnClickListener(this);
        instructionButton.setOnClickListener(this);
        setupButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImageButton2:

                //打开说明界面
                Intent intent = new Intent(MainActivity.this,instructionActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton1:
                getPermission();
                break;
            case R.id.Setup:

                break;
        }
    }

    public void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //检查应用是否有相机权限，如果有则返回PERMISSION_GRANTED,否则返回PERMISSION_DENIED
            int checkPermission = ContextCompat.
                    checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //如果未被赋予权限则弹框请求给予权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CODE);
                return;
            } else {
                //打开相机界面
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, IMAGE_CAMERA);
            }
        } else {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, IMAGE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT)
                            .show();   //checkAnswer(true);
                    //已经获得授权，打开相机
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, IMAGE_CAMERA);
                } else {
                    Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT)
                            .show();
                    //未获得授权，结束当前活动
                    this.finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                exitTime = System.currentTimeMillis();
                Toast.makeText(this, "再按退出", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                System.exit(0);
            }
        }
        return true;
    }
}
