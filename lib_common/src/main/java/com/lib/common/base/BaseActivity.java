package com.lib.common.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

/**
 * @author : yzf
 * time : 2019/08/05
 * description：
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 沉浸式状态栏
     */
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitOnCreate(savedInstanceState);
        setContentView(savedInstanceState);
        initActionBar();
        if (isImmersionBarEnable()) {
            initImmersionBar();
        }
        if (!parseAndInitIntentData()) {
            init();
            afterInit();
        }
    }

    /**
     * 封装下给MVVM使用
     *
     * @param savedInstanceState
     */
    protected void setContentView(Bundle savedInstanceState) {
        setContentView(getLayoutResId());
    }

    /**
     * 用于分析传入参数是否非法
     *
     * @return true表示非法， false表示合法
     */
    protected abstract boolean parseAndInitIntentData();

    /**
     * 所有准入条件(如：登陆限制，权限限制等)全部解除后回调（界面的数据业务初始化动作推荐在此进行）
     */
    protected abstract void init();

    /**
     * onCreate中结束初始化
     */
    protected abstract void afterInit();

    /**
     * 当Activity 变得容易被销毁时 启动时在setContentView之前执行
     *
     * @param savedInstanceState 在setContentView之前
     */
    protected abstract void beforeInitOnCreate(Bundle savedInstanceState);

    /**
     * 获取获取layoutResId
     *
     * @return @layoutId
     */
    public abstract @LayoutRes
    int getLayoutResId();

    /**
     * 设置标题
     */
    protected abstract void initActionBar();

    /**
     * 设置是否需要沉浸式
     *
     * @return true 需要沉浸式 默认 false 不需要沉浸式
     */
    public boolean isImmersionBarEnable() {
        return true;
    }

    /**
     * 初始化沉浸式配置
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true, 1f)
                .keyboardEnable(true);
        int statusBarViewResId = getStatusBarViewId();
        if (statusBarViewResId != -1) {
            mImmersionBar = mImmersionBar.statusBarView(statusBarViewResId);
        }
        mImmersionBar.init();
    }

    /**
     * 设置状态栏view的id
     *
     * @return @id
     */
    public @IdRes
    int getStatusBarViewId() {
        return -1;
    }

    /**
     * 显示toast 复杂的toast 调用toastUtils
     */
    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showShortToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showLongToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
