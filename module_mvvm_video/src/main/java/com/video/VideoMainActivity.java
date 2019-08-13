package com.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.video.listen.VideoPlayListener;
import com.video.utils.VideoUtils;

/**
 * @author : yzf
 * time   : 2019/08/05
 * description：
 */
public class VideoMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_main);
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            if (VideoUtils.getInstance().isNeedInstall()) {
                initPlayer();
            } else {
                VideoUtils.getInstance().resume();
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(v -> VideoUtils.getInstance().pause());
    }

    private void initPlayer() {
        String url1 = "http://10.10.64.108/group1/M00/00/66/CgpAbF1LltGAJ4i2AAx9rPff5qY893.mp3";
        String url2 = "https://uathengnan.purang.com/group1/M00/00/E3/CgqAgl0etQiAeiyEAAVow2FG_OE486.mp3";
        String url3 = "https://uathengnan.purang.com/group1/M00/00/E3/CgqAgl0etOqAC5TvAAi7yVwVgoM528.mp3";
        String url4 = "https://uathengnan.purang.com/group1/M00/00/E3/CgqAgl0etR-AN0a0AANAhZWCI7o562.mp3";
        VideoUtils.getInstance().with(this)
                .addUrls(url1)
                .addUrls(url2)
                .addUrls(url3)
                .addUrls(url4)
                .setListener(new VideoPlayListener() {
                    @Override
                    public void onAllComplete() {
                        Log.d("loggerVideo", "onAllComplete");
                    }

                    @Override
                    public void onComplete(String url) {
                        Log.d("loggerVideo", "onComplete url:" + url);
                    }

                    @Override
                    public void isLoading(Boolean isFinish) {
                        Log.d("loggerVideo", "onLoading url:" + isFinish);
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("loggerVideo", "onError error:" + error);
                    }

                    @Override
                    public void onStart(String url) {
                        Log.d("loggerVideo", "onStart start:" + url);
                    }
                }).play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoUtils.getInstance().releasePlayer();
    }
}