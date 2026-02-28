package com.example.wanandroid.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    // 全局成员变量，方便分享功能调用
    private String mUrl;
    private String mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // 1. 获取传递过来的 URL 和 标题并赋值给成员变量
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");

        // 2. 初始化 Toolbar
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回箭头
        }

        // 3. 注册返回键回调 (替代过时的 onBackPressed，适配 Android 13+)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 如果网页可以后退，则后退网页
                if (mAgentWeb != null && mAgentWeb.back()) {
                    return;
                } else {
                    // 如果网页不能后退了，则关闭当前 Activity
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        // 4. 初始化 AgentWeb 加载网页
        LinearLayout container = findViewById(R.id.container);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator() // 使用默认进度条
                .createAgentWeb()
                .ready()
                .go(mUrl);

        // 5. 动态更新标题 (网页加载完成后，标题可能会改变)
        mAgentWeb.getWebCreator().getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolbar.setTitle(title);
            }
        });
    }

    // ================= 菜单与交互逻辑 =================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载刚才创建的右上角菜单布局
        getMenuInflater().inflate(R.menu.menu_article_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // 点击左上角返回箭头，关闭页面
            finish();
            return true;
        } else if (id == R.id.action_share) {
            // 点击分享
            shareArticle();
            return true;
        } else if (id == R.id.action_browser) {
            // 点击外部浏览器打开
            openInBrowser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 调用系统分享面板
     */
    private void shareArticle() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享文章");
        // 分享内容：标题 + 换行 + 链接
        intent.putExtra(Intent.EXTRA_TEXT, mTitle + "\n" + mUrl);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * 唤起外部浏览器打开链接
     */
    private void openInBrowser() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
        } catch (Exception e) {
            // 捕获异常，防止有些极其精简的手机/模拟器连浏览器都没装导致崩溃
            Toast.makeText(this, "未找到可用的浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    // ================= 生命周期管理 =================

    @Override
    protected void onPause() {
        if (mAgentWeb != null) mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null) mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null) mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
