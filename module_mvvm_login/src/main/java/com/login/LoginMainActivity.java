package com.login;

import android.os.Bundle;

import com.lib.common.base.mvvm.BaseMvvmActivity;
import com.login.viewmodel.LoginMainViewModel;
import com.login.databinding.LoginMainActivityDataBinding;

/**
 * @author : yzf
 * time   : 2019/08/05
 * description：
 */
public class LoginMainActivity extends BaseMvvmActivity<LoginMainActivityDataBinding,LoginMainViewModel> {

    @Override
    public int getLayoutResId() {
        return R.layout.login_activity_main;
    }

    @Override
    protected void initActionBar() {

    }
}
