package com.example.wanandroid.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.R;
import com.example.wanandroid.base.BaseActivity;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.data.bean.HotKeyBean;
import com.example.wanandroid.databinding.ActivitySearchBinding;
import com.example.wanandroid.ui.ArticleDetailActivity;
import com.example.wanandroid.ui.home.HomeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter, ActivitySearchBinding> implements SearchContract.View {

    private HomeAdapter mAdapter;
    private int curPage = 0;
    private String currentKeyword = "";
    private static final String SP_NAME = "search_history_sp";
    private static final String KEY_HISTORY = "key_history_list";
    private List<String> mHistoryList = new ArrayList<>();


    @Override
    protected ActivitySearchBinding getViewBinding() {
        return ActivitySearchBinding.inflate(getLayoutInflater());
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void initView() {
        // 1. 顶部返回按钮
        binding.ivBack.setOnClickListener(v -> finish());

        // 2. 初始化列表 (完美复用 HomeAdapter)
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter();
        binding.rvSearch.setAdapter(mAdapter);

        // 设置点击和收藏
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(SearchActivity.this, ArticleDetailActivity.class);
                intent.putExtra("url", article.getLink());
                intent.putExtra("title", article.getTitle());
                startActivity(intent);
            }
            @Override
            public void onCollectClick(int position, int articleId, boolean isCollect) {
                mPresenter.collectArticle(articleId, position, isCollect);
            }
        });

        // 3. 上拉加载更多 (搜索接口没有下拉刷新，只有加载更多)
        binding.refreshLayout.setEnableRefresh(false);
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) { }
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                curPage++;
                mPresenter.search(curPage, currentKeyword);
            }
        });

        // 4. 点击搜索按钮或软键盘的回车
        binding.tvSearchBtn.setOnClickListener(v -> doSearch());
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });

        // 5. 监听输入框变化，清空时切回热词页面
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    binding.layoutHotKeys.setVisibility(View.VISIBLE);
                    binding.refreshLayout.setVisibility(View.GONE);
                    // 每次清空输入框退回搜索面板时，刷新一次历史记录
                    loadAndShowHistory();
                }
            }
        });
        binding.tvClearHistory.setOnClickListener(v -> {
            mHistoryList.clear();
            saveHistoryToSp(); // 同步到本地
            loadAndShowHistory(); // 刷新 UI
        });
    }


    @Override
    protected void initData() {
        // 一进来请求热搜词
        mPresenter.getHotKeys();
        loadAndShowHistory();
    }

    private void doSearch() {
        String keyword = binding.etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            showToast("请输入搜索内容");
            return;
        }

        // --- 新增：保存历史记录 ---
        // 1. 如果历史中已经有了这个词，先移除旧的（为了把最新搜的词顶到最前面）
        mHistoryList.remove(keyword);
        // 2. 插入到最前面
        mHistoryList.add(0, keyword);
        // 3. 限制最多保存 10 条历史记录
        if (mHistoryList.size() > 10) {
            mHistoryList.remove(mHistoryList.size() - 1);
        }
        // 4. 同步到 SharedPreferences
        saveHistoryToSp();
        // -------------------------

        currentKeyword = keyword;
        curPage = 0;

        binding.layoutHotKeys.setVisibility(View.GONE);
        binding.refreshLayout.setVisibility(View.VISIBLE);
        mPresenter.search(curPage, currentKeyword);
    }

    // --- View 层接口实现 ---

    @Override
    public void showHotKeys(List<HotKeyBean> hotKeys) {
        binding.flexboxHot.removeAllViews();
        // 动态向 FlexboxLayout 添加 TextView 标签
        for (HotKeyBean hotKey : hotKeys) {
            TextView tvTag = (TextView) LayoutInflater.from(this).inflate(R.layout.item_hot_key, binding.flexboxHot, false);
            tvTag.setText(hotKey.getName());

            // 点击热词，自动填入输入框并搜索
            tvTag.setOnClickListener(v -> {
                binding.etSearch.setText(hotKey.getName());
                binding.etSearch.setSelection(hotKey.getName().length()); // 光标移到末尾
                doSearch();
            });
            binding.flexboxHot.addView(tvTag);
        }
    }

    @Override
    public void showSearchArticles(List<Article> articles) {
        binding.refreshLayout.finishLoadMore();
        if (curPage == 0) mAdapter.setNewData(articles);
        else mAdapter.addData(articles);
    }

    @Override
    public void showSearchFail(String errorMsg) {
        binding.refreshLayout.finishLoadMore();
        showToast("搜索失败: " + errorMsg);
    }

    @Override
    public void showCollectSuccess(int position, boolean isCollect) {
        mAdapter.getData().get(position).setCollect(isCollect);
        showToast(isCollect ? "收藏成功" : "取消收藏");
    }

    @Override
    public void showCollectFail(String errorMsg) {
        showToast("操作失败");
        mAdapter.notifyDataSetChanged();
    }
    private void saveHistoryToSp() {
        SharedPreferences sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(mHistoryList);
        sp.edit().putString(KEY_HISTORY, json).apply();
    }
    private void loadAndShowHistory() {
        SharedPreferences sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_HISTORY, "");

        if (!TextUtils.isEmpty(json)) {
            // 使用 Gson 解析 List
            mHistoryList = new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
        }

        // 控制历史区域的显示或隐藏
        if (mHistoryList == null || mHistoryList.isEmpty()) {
            binding.layoutHistory.setVisibility(View.GONE);
        } else {
            binding.layoutHistory.setVisibility(View.VISIBLE);
            binding.flexboxHistory.removeAllViews();

            // 动态添加 TextView 标签
            for (String history : mHistoryList) {
                // 复用热词的 item_hot_key.xml 布局
                TextView tvTag = (TextView) LayoutInflater.from(this).inflate(R.layout.item_hot_key, binding.flexboxHistory, false);
                tvTag.setText(history);
                // 历史记录通常用灰色系区分，这里为了简单直接覆盖文字颜色
                tvTag.setTextColor(android.graphics.Color.parseColor("#666666"));

                // 点击历史词直接搜索
                tvTag.setOnClickListener(v -> {
                    binding.etSearch.setText(history);
                    binding.etSearch.setSelection(history.length());
                    doSearch();
                });

                binding.flexboxHistory.addView(tvTag);
            }
        }
    }
}
