package com.example.wanandroid.base;

/**
 * View 层的基础接口
 * 定义所有 V 层都应该具备的 UI 操作
 */
public interface IBaseView {
    // 显示加载框
    void showLoading();

    // 隐藏加载框
    void hideLoading();

    // 显示错误信息 (比如网络请求失败)
    void showToast(String msg);
}
