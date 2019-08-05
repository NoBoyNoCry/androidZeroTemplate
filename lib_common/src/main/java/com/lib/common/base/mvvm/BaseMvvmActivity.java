package com.lib.common.base.mvvm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lib.common.base.BaseActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author : yzf
 * time : 2019/08/05
 * description：
 */
public abstract class BaseMvvmActivity<VB extends ViewDataBinding, VM extends BaseAndroidViewModel> extends BaseActivity {

    protected VB mBinding;
    protected VM mViewModel;
    private AlertDialog  dialog;

    @Override
    protected void beforeInitOnCreate(Bundle savedInstanceState) {
        if (mViewModel == null) {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Class presenterClazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                mViewModel = (VM) ViewModelProviders.of(this).get(presenterClazz);
            }
        }
    }

    @Override
    protected void setContentView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, getLayoutResId());
    }

    @Override
    protected boolean parseAndInitIntentData() {
        //添加viewmodel关联
        int viewModelId = initVariableId();
        if (viewModelId != 0) {
            //关联ViewModel
            mBinding.setVariable(viewModelId, mViewModel);
        }
        //关联ViewModel
        //绑定生命周期
        getLifecycle().addObserver(mViewModel);
        //初始化一些常用关系
        observerNormal();
        return true;
    }

    /**
     * 如果要和activity绑定直接调用activity时间把BRID 传入
     * @return
     */
    protected int initVariableId() {
        return 0;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void afterInit() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑生命周期
        getLifecycle().removeObserver(mViewModel);
        if (mBinding != null) {
            mBinding.unbind();
        }
    }

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
        //关闭界面
        mViewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                finish();
            }
        });
        //带参关闭界面
        mViewModel.getUC().getFinishParamsEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> stringObjectMap) {
                Bundle bundle = (Bundle) stringObjectMap.get(BaseAndroidViewModel.ParameterField.BUNDLE);
                Intent intent = getIntent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //带参启动页面
        mViewModel.getUC().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> stringObjectMap) {
                Class<?> clz = (Class<?>) stringObjectMap.get(BaseAndroidViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) stringObjectMap.get(BaseAndroidViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });
        //关闭上一层
        mViewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                onBackPressed();
            }
        });
        //toast显示
        mViewModel.getUC().getShowToastEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String v) {
                showLongToast(v);
            }
        });
    }

    public void showDialog(String title) {
        if (dialog != null) {
            dialog.show();
        } else {
            //创建对话框
            dialog = new AlertDialog.Builder(this).create();
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
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
