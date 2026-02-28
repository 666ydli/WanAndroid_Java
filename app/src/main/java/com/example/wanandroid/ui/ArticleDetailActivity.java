package com.example.wanandroid.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wanandroid.R;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;

public class ArticleDetailActivity extends AppCompatActivity {

    private AgentWeb mAgentWeb;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // 1. 获取传递过来的 URL 和 标题
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        // 2. 初始化 Toolbar
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回箭头
        }

        // 3. 初始化 AgentWeb
        LinearLayout container = findViewById(R.id.container);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator() // 使用默认进度条
                .createAgentWeb()
                .ready()
                .go(url); // 加载网页

        // 4. 动态更新标题 (网页加载完成后，标题可能会变)
        mAgentWeb.getWebCreator().getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolbar.setTitle(title);
            }
        });
        // 注册返回键回调
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 如果 WebView 可以后退，则后退网页
                if (mAgentWeb.back()) {
                    // 不需要做其他操作，WebView 会自己处理
                } else {
                    // 如果 WebView 不能后退了，则关闭 Activity
                    // 这里必须把 isEnabled 设为 false，否则会无限循环调自己
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    // 处理 Toolbar 左上角的返回点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 生命周期管理 (防止内存泄漏)
    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
