package com.lib.common.base.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yzf
 * time : 2019/08/05
 * description：
 */
public class BaseAndroidViewModel extends AndroidViewModel implements LifecycleObserver {

    private UIChangeLiveData uc;

    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    public void showDialog() {
        showDialog("请稍后...");
    }

    public void showDialog(String title) {
        getUC().getShowDialogEvent().setValue(title);
    }

    public void dismissDialog() {
        getUC().getDismissDialogEvent().setValue(null);
    }

    public void showToast(String value) {
        getUC().getShowToastEvent().setValue(value);
    }

    public void showToast(@StringRes int valueId) {
        getUC().getShowToastEvent().setValue(getApplication().getResources().getString(valueId));
    }

    public void setStateLiveData(int netError) {
        getUC().getStateLiveData().setValue(netError);
    }

    /**
     * 关闭页面
     *
     * @param bundle 跳转所携带的信息
     */
    public void finish(Bundle bundle){
        Map<String, Object> params = new HashMap<>();
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        getUC().getFinishParamsEvent().postValue(params);
    }

    public void finish(){
        Map<String, Object> params = new HashMap<>();
        getUC().getFinishParamsEvent().postValue(params);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        getUC().getStartActivityEvent().postValue(params);
    }

    public void startActivity(Class<?> clz) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        getUC().getStartActivityEvent().postValue(params);
    }

    public final class UIChangeLiveData extends MutableLiveData {
        private MutableLiveData<String> showDialogEvent;
        private MutableLiveData<Void> dismissDialogEvent;
        private MutableLiveData<Void> finishEvent;
        private MutableLiveData<Void> onBackPressedEvent;
        private MutableLiveData<Map<String, Object>> startActivityEvent;
        private MutableLiveData<Map<String, Object>> finishParamsEvent;
        private MutableLiveData<String> showToastEvent;
        //发送列表状态使用
        private MutableLiveData<Integer> stateLiveData;

        public MutableLiveData<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public MutableLiveData<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public MutableLiveData<Void> getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public MutableLiveData<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        public MutableLiveData<String> getShowToastEvent() {
            return showToastEvent = createLiveData(showToastEvent);
        }

        public MutableLiveData<Integer> getStateLiveData() {
            return stateLiveData = createLiveData(stateLiveData);
        }

        public MutableLiveData<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public MutableLiveData<Map<String, Object>> getFinishParamsEvent() {
            return finishParamsEvent = createLiveData(finishParamsEvent);
        }

        private MutableLiveData createLiveData(MutableLiveData liveData) {
            if (liveData == null) {
                liveData = new MutableLiveData();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
