package com.example.wanandroid.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

/**
 * Activity 基类
 * P: 指定该页面对应的 Presenter
 * VB: 指定该页面对应的 ViewBinding
 */
public abstract class BaseActivity<P extends BasePresenter, VB extends ViewBinding> extends AppCompatActivity implements IBaseView {

    protected P mPresenter;
    protected VB binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 初始化 ViewBinding
        binding = getViewBinding();
        setContentView(binding.getRoot());

        // 2. 创建 Presenter
        mPresenter = createPresenter();

        // 3. 关联 View (将当前 Activity 绑定到 Presenter)
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        // 4. 初始化 UI 和 数据
        initView();
        initData();
    }

    /**
     * 必须由子类实现：提供 ViewBinding
     */
    protected abstract VB getViewBinding();

    /**
     * 必须由子类实现：创建具体的 Presenter
     */
    protected abstract P createPresenter();

    /**
     * 初始化界面逻辑
     */
    protected abstract void initView();

    /**
     * 初始化数据逻辑
     */
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 页面销毁时，解绑 Presenter，防止内存泄漏
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    // --- 实现 IBaseView 的通用方法 ---

    @Override
    public void showLoading() {
        // 这里暂时留空，后续可以集成 LoadingDialog
    }

    @Override
    public void hideLoading() {
        // 隐藏 LoadingDialog
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
