package com.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.video.listen.MediaPlayListener;
import com.video.utils.MediaPlayUtils;

/**
 * @author : yzf
 * time   : 2019/08/13
 * description：
 */
public class MediaPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_main);
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            initPlayer();
        });
        findViewById(R.id.btn_stop).setOnClickListener(v -> {

        });
    }

    private void initPlayer() {
        String url1 = "http://10.10.64.108/group1/M00/00/66/CgpAbF1LltGAJ4i2AAx9rPff5qY893.mp3";
        String url2 = "https://uathengnan.purang.com/group1/M00/00/E3/CgqAgl0etQiAeiyEAAVow2FG_OE486.mp3";
        String url3 = "https://uathengnan.purang.com/group1/M00/00/E3/CgqAgl0etOqAC5TvAAi7yVwVgoM528.mp3";
        String url4 = "https://uathengnan.purang.com/group1/M00/00/E3/CgqAgl0etR-AN0a0AANAhZWCI7o562.mp3";
        MediaPlayUtils.getInstance().addUrls(url1)
                .addUrls(url2)
                .addUrls(url3)
                .addUrls(url4)
                .setListener(new MediaPlayListener() {
                    @Override
                    public void onAllComplete() {
                        Log.d("loggerVideo", "onAllComplete");
                    }

                    @Override
                    public void onComplete(String url) {
                        Log.d("loggerVideo", "onComplete"+ url);
                    }

                    @Override
                    public void onLoading(String url) {
                        Log.d("loggerVideo", "onLoading" + url);
                    }

                    @Override
                    public void onError(String url) {
                        Log.d("loggerVideo", "onError" + url);
                    }
                }).init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayUtils.getInstance().onDestroy();
    }
}
