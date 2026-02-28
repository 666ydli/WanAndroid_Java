package com.example.wanandroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wanandroid.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 使用 Handler 延时 2000 毫秒（2秒）后执行跳转
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 跳转到 MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                // 销毁启动页，防止用户按返回键又回到启动页
                finish();
            }
        }, 2000);
    }
}
