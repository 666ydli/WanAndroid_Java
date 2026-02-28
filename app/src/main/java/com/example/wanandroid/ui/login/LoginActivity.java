package com.example.wanandroid.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.example.wanandroid.base.BaseActivity;
import com.example.wanandroid.data.bean.UserBean;
import com.example.wanandroid.databinding.ActivityLoginBinding;
import com.example.wanandroid.utils.UserManager;

public class LoginActivity extends BaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginContract.View {

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initView() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    showToast("账号密码不能为空");
                    return;
                }
                // 发起登录
                mPresenter.login(username, password);
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        UserManager.getInstance().saveUser(userBean);
        showToast("登录成功: " + userBean.getUsername());
        org.greenrobot.eventbus.EventBus.getDefault().post(new com.example.wanandroid.common.LoginEvent(true));
        finish();
    }

    @Override
    public void loginFail(String errorMsg) {
        showToast("登录失败: " + errorMsg);
    }
}
