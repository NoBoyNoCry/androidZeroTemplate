package com.video.listen;

/**
 * @author : yzf
 * time   : 2019/08/13
 * description：
 */
public interface VideoPlayListener {

    /**
     * 全部播放完成
     */
    void onAllComplete();

    /**
     * 完成一条
     * @param url 完成的URL
     */
    void onComplete(String url);

    /**
     * 加载中的
     * @param isFinish 是否在加载模式
     */
    void isLoading(Boolean isFinish);

    /**
     * 出错
     * @param error 出错
     */
    void onError(String error);

    /**
     * 播放中的URl
     * @param url 播放中的URl
     */
    void onStart(String url);
}
