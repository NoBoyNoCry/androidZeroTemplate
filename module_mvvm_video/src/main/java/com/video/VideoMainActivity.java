package com.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
                .play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoUtils.getInstance().releasePlayer();
    }
}