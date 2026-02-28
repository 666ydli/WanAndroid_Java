package com.example.wanandroid.base;

import java.lang.ref.WeakReference;

/**
 * Presenter 基类
 * T 指代具体的 View 类型 (必须是 IBaseView 的子类)
 */
public abstract class BasePresenter<T extends IBaseView> {

    // 使用弱引用持有 View，防止内存泄漏
    protected WeakReference<T> mViewRef;

    /**
     * 绑定 View
     * 在 Activity 的 onCreate 中调用
     */
    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
    }

    /**
     * 解绑 View
     * 在 Activity 的 onDestroy 中调用
     */
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 获取 View 引用
     * 子类 Presenter 调用此方法来操作 UI
     */
    protected T getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

    /**
     * 判断 View 是否还挂载着
     * 网络请求回来时，先检查一下 View 还在不在
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }
}
