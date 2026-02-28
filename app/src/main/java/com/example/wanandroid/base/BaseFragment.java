package com.example.wanandroid.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

/**
 * Fragment 基类
 * P: 指定对应的 Presenter
 * VB: 指定对应的 ViewBinding
 */
public abstract class BaseFragment<P extends BasePresenter, VB extends ViewBinding> extends Fragment implements IBaseView {

    protected P mPresenter;
    protected VB binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. 初始化 ViewBinding
        binding = getViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. 创建并绑定 Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            // 注意：这里传的是当前 Fragment
            mPresenter.attachView(this);
        }

        // 3. 初始化 UI 和 数据
        initView();
        initData();
    }

    /**
     * 子类必须实现：获取 ViewBinding
     */
    protected abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract P createPresenter();
    protected abstract void initView();
    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Fragment 视图销毁时解绑
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        binding = null; // 防止内存泄漏
    }

    // --- 实现 IBaseView 的通用方法 ---

    @Override
    public void showLoading() {
        // 留空或统一处理
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showToast(String msg) {
        if (getContext() != null) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
