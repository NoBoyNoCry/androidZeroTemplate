package com.video.listen;

/**
 * @author : yzf
 * time   : 2019/08/13
 * description：
 */
public interface MediaPlayListener {

    /**
     * 全部播放完成
     */
    void onAllComplete();

    /**
     * 完成一条
     */
    void onComplete(String url);

    /**
     * 加载中的
     */
    void onLoading(String url);

    /**
     * 出错
     */
    void onError(String error);

}
