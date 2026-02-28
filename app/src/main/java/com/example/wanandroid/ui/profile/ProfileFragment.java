package com.example.wanandroid.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.common.LoginEvent;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.CoinInfoBean;
import com.example.wanandroid.data.bean.UserBean;
import com.example.wanandroid.data.remote.RetrofitManager;
import com.example.wanandroid.databinding.FragmentProfileBinding;
import com.example.wanandroid.ui.collect.CollectActivity;
import com.example.wanandroid.ui.login.LoginActivity;
import com.example.wanandroid.ui.rank.RankActivity;
import com.example.wanandroid.utils.UserManager;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 点击卡片去登录
        binding.llUserInfo.setOnClickListener(v -> {
            if (!UserManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        // 退出登录
        binding.btnLogout.setOnClickListener(v -> {
            if (UserManager.getInstance().isLogin()) {
                // 1. 清除我们自己维护的本地用户信息
                UserManager.getInstance().logout();
                // 2. [关键修复] 彻底清除底层持久化的网络 Cookie！
                com.example.wanandroid.data.remote.RetrofitManager.getInstance().clearCookie();
                // 3. 更新当前页面的 UI
                updateUI();
                // 4. 通知全网：退出登录
                EventBus.getDefault().post(new LoginEvent(false));
            }
        });
        binding.llMyCollect.setOnClickListener(v -> {
            if (UserManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), CollectActivity.class));
            } else {
                startActivity(new Intent(getContext(), LoginActivity.class));
                // showToast("请先登录");
            }
        });
        binding.llRank.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), RankActivity.class)); // 等下创建这个Activity
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        // [新增] 如果已登录，获取最新积分
        if (UserManager.getInstance().isLogin()) {
            fetchMyCoin();
        }
    }

    private void updateUI() {
        if (UserManager.getInstance().isLogin()) {
            UserBean user = UserManager.getInstance().getUser();
            binding.tvUsername.setText(user.getUsername());
            binding.tvId.setText("ID: " + user.getId());
            binding.tvId.setVisibility(View.VISIBLE);
            binding.btnLogout.setVisibility(View.VISIBLE);
        } else {
            binding.tvUsername.setText("去登录");
            binding.tvId.setVisibility(View.GONE);
            binding.tvCoin.setVisibility(View.GONE); // 未登录隐藏积分
            binding.btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchMyCoin() {
        RetrofitManager.getInstance().getApiService().getMyCoin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<CoinInfoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(BaseResponse<CoinInfoBean> response) {
                        if (response.getErrorCode() == 0 && response.getData() != null) {
                            CoinInfoBean coinInfo = response.getData();
                            binding.tvCoin.setVisibility(View.VISIBLE);
                            binding.tvCoin.setText(
                                    "等级: " + coinInfo.getLevel() +
                                            "  |  排名: " + coinInfo.getRank() +
                                            "  |  积分: " + coinInfo.getCoinCount()
                            );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }
}
