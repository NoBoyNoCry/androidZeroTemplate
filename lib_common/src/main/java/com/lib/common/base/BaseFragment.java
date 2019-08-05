package com.lib.common.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lib.common.base.mvvm.BaseAndroidViewModel;
import com.lib.common.utils.ToastUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : yzf
 * time : 2019/08/05
 * description：
 */
public abstract class BaseFragment <V extends ViewDataBinding, VM extends BaseAndroidViewModel> extends Fragment {

    protected V mBinding;
    protected VM mViewModel;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        if (mViewModel == null) {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Class presenterClazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                mViewModel = (VM) ViewModelProviders.of(this).get(presenterClazz);
            }
        }
        observerNormal();
        //初始化
        initIntentData();
        initView();
        initEvent();
        return mBinding.getRoot();
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    @LayoutRes
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    /**
     * ----------------------------------------------------------
     */
    //初始化常用绑定关系
    private void observerNormal() {
        //加载对话框显示
        mViewModel.getUC().getShowDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                showDialog(title);
            }
        });
        //加载对话框消失
        mViewModel.getUC().getDismissDialogEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissDialog();
            }
        });
        //toast显示
        mViewModel.getUC().getShowToastEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String v) {
                ToastUtils.showLong(v);
            }
        });
    }

    public void showDialog(String title) {
        if (dialog != null) {
            dialog.show();
        } else {
            //创建对话框
            dialog = new AlertDialog.Builder(getContext()).create();
            //设置对话框标题
            dialog.setTitle(title);
            //分别设置三个button
            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();//关闭对话框
                }
            });
            dialog.show();//显示对话框
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * -------------------------------------------------------------------
     */

    public void initIntentData() {
    }

    /**
     * 初始化参数
     */
    protected abstract void initView();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

}
